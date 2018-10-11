package hit.to.go.controller;

import hit.to.go.database.dao.UserMapper;
import hit.to.go.database.dao.ValidateCodeMapper;
import hit.to.go.database.mybatis.MybatisProxy;
import hit.to.go.entity.user.User;
import hit.to.go.entity.user.UserWithPassword;
import hit.to.go.entity.validate.ValidateCode;
import hit.to.go.platform.MailConfig;
import hit.to.go.platform.AttrKey;
import hit.to.go.platform.SystemConfig;
import hit.to.go.platform.protocol.RequestResults;
import hit.to.go.platform.util.MailUtil;
import hit.to.go.platform.util.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/9/22
 */
@Controller
@ResponseBody
@RequestMapping("/authority")
public class UserAuthorityController {
    private static final Logger logger = LoggerFactory.getLogger(UserAuthorityController.class);

    @RequestMapping("/register")
    public String register(@RequestParam Map<String, Object> map, HttpSession session) {
        Object code = map.get("code");
        Object name = map.get("name");
        Object password = map.get("password");
        Object email = map.get("email");
        if (code != null && name != null && password != null && email != null) {
            String result = validateCode(code.toString(), email.toString());
            if (result.equals("success")) {
                UserMapper mapper = MybatisProxy.create(UserMapper.class);
                Integer id = mapper.selectIdByEmail(email.toString());

                if (id != null) return RequestResults.forbidden("该邮箱已被注册, 请重试!");

                Integer rows = mapper.register(map);
                if (rows != null && rows.equals(1)) {
                    Object uid = map.get("user_id");
                    UserWithPassword user = mapper.selectUserById(uid.toString());
                    session.setAttribute(AttrKey.ATTR_USER, user);
                    return RequestResults.success(user);
                }
                return RequestResults.error();
            }
            return RequestResults.forbidden(result) ;
        }
        return RequestResults.forbidden("请确认已填写全部必填信息!");
    }

    @RequestMapping("/modifyInfo")
    public String modifyInfo(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, @RequestParam Map<String, String> paras) {
        paras.put("id", user.getId().toString());
        UserMapper mapper = MybatisProxy.create(UserMapper.class);
        Integer rows = mapper.completeInfo(paras);
        if (rows != null && rows.equals(1)) return RequestResults.success();
        return RequestResults.error("保存失败!");
    }

//    @RequestMapping("/login")
//    public String login(HttpSession session, String id, String email, String password) {
//        if (id == null && email == null || password == null) return RequestResults.forbidden("密码不能为空且账号或邮箱必须提供其一");
//        UserMapper mapper = MybatisProxy.create(UserMapper.class);
//
//        UserWithPassword user = null;
//        if (id != null) user = mapper.selectUserById(id);
//        else user = mapper.selectUserByEmail(email);
//
//        if (user != null && user.getPassword().equals(password)) {
//            if (user.getType() == User.TYPE_FORBIDDEN_USER) return RequestResults.forbidden("该账号已被封禁,请自觉遵守网络规定!");
//
//            SystemStorage.setOnlineUser(user.getId().toString(), user);
//            session.setAttribute(AttrKey.ATTR_USER, user);
//
//            return RequestResults.success(user);
//        }
//        return RequestResults.forbidden("账号或密码错误!");
//    }

    @RequestMapping("/login")
    public String login(HttpSession session, String account, String password) {
        if (account != null && password != null) {
            UserWithPassword user = null;
            UserMapper mapper = MybatisProxy.create(UserMapper.class);
            if (account.matches("^\\d+$")) {
                user = mapper.selectUserById(account);
            } else if (account.matches("^\\w+@\\w+(\\.\\w+)*$")) {
                user = mapper.selectUserByEmail(account);
            } else return RequestResults.forbidden("请输入正确的账号！");
            if (user != null) {
                if (user.getPassword().equals(password)) {
                    session.setAttribute(AttrKey.ATTR_USER, user);
                    return RequestResults.success(user);
                }
                return RequestResults.forbidden("账号或密码错误！");
            }
        }
        return RequestResults.forbidden("请填写账号密码！");
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(AttrKey.ATTR_USER);
        return RequestResults.success();
    }

    @RequestMapping("/changePassword")
    public String changePassword(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, String oldPassword, String newPassword, String code, HttpSession session) {
        // TODO 可优化数据库查询
        if (oldPassword != null && newPassword != null && code != null) {
            if (user.getPassword().equals(oldPassword)) {
                String result = validateCode(code, user.getEmail());
                if (result.equals("success")) {
                    UserMapper mapper = MybatisProxy.create(UserMapper.class);
                    Map<String, String> paras = new HashMap<>();
                    paras.put("id", user.getId().toString());
                    paras.put("password", newPassword);
                    Integer rows = mapper.changePassword(paras);
                    if (rows != null && rows.equals(1)) {
                        session.removeAttribute(AttrKey.ATTR_USER);
                        return RequestResults.success();
                    }
                    return RequestResults.error("更新失败!");
                }
                return RequestResults.forbidden(result);
            }
            return RequestResults.forbidden("密码错误!");
        }
        return RequestResults.wrongParameters();
    }

    @RequestMapping("/getUserInfo")
    public String getUserInfo(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, String id) {
        if (id != null && !id.equals(user.getId())) {
            UserMapper mapper = MybatisProxy.create(UserMapper.class);
            User u = mapper.queryById(id);
            if (u == null) return RequestResults.notFound("用户不存在！");
            return RequestResults.success(u);
        }
        return RequestResults.success(user);
    }

    @RequestMapping("/sendValidateCode")
    public String sendValidateCode(String email) {
        if (email == null) return RequestResults.wrongParameters();
        boolean update = false;
        String code;
        Map<String, Object> map;

        Date now = new Date();
        ValidateCodeMapper mapper = MybatisProxy.create(ValidateCodeMapper.class);
        ValidateCode vc = mapper.queryValidateCode(email);
        if (vc != null) {
            // 判断距上次请求是否小于一分钟
            if (now.getTime() - vc.getDate().getTime() < 60000) return RequestResults.forbidden("请于一分钟之后再试!");
            update = true;
        }
        map = new HashMap<>();
        code = Validate.genValidateCode();

        map.put("code", code);
        map.put("date", now);
        map.put("email", email);

        logger.debug("email :{}", email);
        boolean flag;

        flag = MailUtil.send(email, SystemConfig.getMailConfig().getTemplate(MailConfig.TEMPLATE_VALIDATE_MAIL).replaceAll("\\{validateCode}", code));
        if (flag) {
            if (update) mapper.updateValidateCode(map);
            else mapper.insertValidateCode(map);
            return RequestResults.success();
        }
        return RequestResults.error("邮件发送失败, 请稍后再试~");
    }

    @RequestMapping("/sendValidateCodeToCurrentUser")
    public String sendValidateCodeToCurrentUser(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user) {
        String email = user.getEmail();
        boolean update = false;
        String code;
        Map<String, Object> map;

        Date now = new Date();
        ValidateCodeMapper mapper = MybatisProxy.create(ValidateCodeMapper.class);
        ValidateCode vc = mapper.queryValidateCode(email);
        if (vc != null) {
            // 判断距上次请求是否小于一分钟
            if (now.getTime() - vc.getDate().getTime() < 60000) return RequestResults.forbidden("请于一分钟之后再试!");
            update = true;
        }
        map = new HashMap<>();
        code = Validate.genValidateCode();

        map.put("code", code);
        map.put("date", now);
        map.put("email", email);

        Integer rows = null;
        boolean flag;
        if (update) rows = mapper.updateValidateCode(map);
        else rows = mapper.insertValidateCode(map);
        if (rows != null && rows.equals(1)) {
            flag = MailUtil.send(email, SystemConfig.getMailConfig().getTemplate(MailConfig.TEMPLATE_VALIDATE_MAIL).replaceAll("\\{validateCode}", code));
            if (flag) return RequestResults.success();
        }
        return RequestResults.error("邮件发送失败, 请稍后再试~");
    }

    @RequestMapping("/validateCode")
    public String validateCode(@RequestParam Map<String, String> map) {
        String email = map.get("email");
        String code = map.get("code");

        if (email == null || code == null) return RequestResults.wrongParameters();

        String result = validateCode(code, email);
        if (result.equals("success")) return RequestResults.success();
        return RequestResults.forbidden(result);
    }

    @RequestMapping("/uniqueEmail")
    public String uniqueEmail(String email) {
        if (email != null) {
            UserMapper mapper = MybatisProxy.create(UserMapper.class);
            Integer id = mapper.selectIdByEmail(email);
            if (id != null) return RequestResults.forbidden("该邮箱已被注册!");
            return RequestResults.success("邮箱未被注册, 可以使用!");
        }

        return RequestResults.badRequest("email不能为空");
    }

    private String validateCode(String code, String email) {
        Date now = new Date();

        if (email != null && code != null) {
            ValidateCodeMapper mapper = MybatisProxy.create(ValidateCodeMapper.class);
            ValidateCode vc = mapper.queryValidateCode(email);

            if (vc != null && (now.getTime() - vc.getDate().getTime() < 900000)) {
                if (vc.getState().equals(ValidateCode.STATE_VALID) && vc.getCode().equalsIgnoreCase(code)) {
                    mapper.becomeInvalid(email);
                    return "success";
                }
                return "验证码错误或已失效, 请重试!";
            }
            return "验证码已超时, 请重试!";
        }
        return "验证码与邮箱均不能为空";
    }
}
