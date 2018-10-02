package hit.to.go.controller;

import hit.to.go.database.dao.UserMapper;
import hit.to.go.database.dao.ValidateCodeMapper;
import hit.to.go.database.mybatis.MybatisProxy;
import hit.to.go.entity.user.User;
import hit.to.go.entity.user.UserWithPassword;
import hit.to.go.entity.validate.ValidateCode;
import hit.to.go.platform.MailConfig;
import hit.to.go.platform.PlatformAttrKey;
import hit.to.go.platform.SystemConfig;
import hit.to.go.platform.SystemStorage;
import hit.to.go.platform.protocol.RequestResult;
import hit.to.go.platform.protocol.RequestResults;
import hit.to.go.platform.util.MailUtil;
import hit.to.go.platform.util.Validate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
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

    @RequestMapping("/register")
    public String register(@RequestParam Map<String, String> map, HttpServletResponse response) {
        String code = map.get("code");
        String name = map.get("name");
        String password = map.get("password");
        String email = map.get("email");
        if (code != null && name != null && password != null && email != null) {
            String result = validateCode(code, email);
            if (result.equals("success")) {
                UserMapper mapper = MybatisProxy.create(UserMapper.class);
                Integer id = mapper.selectIdByEmail(email);

                if (id != null) return RequestResults.forbidden("该邮箱已被注册, 请重试!");

                Integer rows = mapper.register(map);
                if (rows != null && rows.equals(1))
                    return RequestResults.success(map.get("user_id"));

                return RequestResults.error();
            }
            return RequestResults.forbidden(result) ;
        }

        return RequestResults.forbidden("请确认已填写全部必填信息!");
    }

    @RequestMapping("/modifyInfo")
    public String completeInfo(@RequestParam Map<String, String> paras) {
        UserMapper mapper = MybatisProxy.create(UserMapper.class);
        String id = paras.get("id");
        String password = paras.get("password");

        if (id != null && password != null) {
            Integer rows = mapper.completeInfo(paras);
            if (rows != null && rows.equals(1)) return RequestResults.success();
            return RequestResults.error("保存失败!");
        }

        return RequestResults.needLogin();
    }

    @RequestMapping("/login")
    public String login(Integer id, String email, String password) {
        if (id == null && email == null || password == null) return RequestResults.forbidden("密码不能为空且账号或邮箱必须提供其一");
        UserMapper mapper = MybatisProxy.create(UserMapper.class);

        UserWithPassword user = null;
        if (id != null) user = mapper.selectUserById(id);
        else user = mapper.selectUserByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            if (user.getType() == User.TYPE_FORBIDDEN_USER) return RequestResults.forbidden("该账号已被封禁,请自觉遵守网络规定!");

            SystemStorage.setOnlineUser(user.getId().toString(), user);
            return RequestResults.success(user);
        }
        return RequestResults.forbidden("账号或密码错误!");
    }

    @RequestMapping("/logout")
    public String logout(String id, String password) {
        if (id != null && password != null) {
            UserWithPassword user = SystemStorage.getOnlineUser(id);
            if (user != null && user.getPassword().equals(password)) {
                SystemStorage.removeOnlineUser(id);
                return RequestResults.success();
            }
            return RequestResults.invalidAccountOrPassword();
        }
        return RequestResults.wrongParameters();
    }

    @RequestMapping("/changePassword")
    public String changePassword(Integer id, String oldPassword, String newPassword, String code) {
        if (id != null && oldPassword != null && newPassword != null && code != null) {
            UserWithPassword user = SystemStorage.getOnlineUser(id.toString());
            if (user != null) {
                if (user.getPassword().equals(oldPassword)) {
                    String result = validateCode(code, user.getEmail());
                    if (result.equals("success")) {
                        UserMapper mapper = MybatisProxy.create(UserMapper.class);
                        Map<String, String> paras = new HashMap<>();
                        paras.put("id", id.toString());
                        paras.put("password", newPassword);
                        Integer rows = mapper.changePassword(paras);
                        if (rows != null && rows.equals(1)) {
                            SystemStorage.removeOnlineUser(id.toString());
                            return RequestResults.success();
                        }
                        return RequestResults.error("更新失败!");
                    }
                    return RequestResults.forbidden(result);
                }
                return RequestResults.forbidden("密码错误!");
            }
            return RequestResults.forbidden("用户未登录!");
        }
        return RequestResults.wrongParameters();
    }

    @RequestMapping("/getUserInfo")
    public String getUserInfo(HttpSession session, Integer id) {
        if (id != null) {
            UserMapper mapper = MybatisProxy.create(UserMapper.class);
            User user = mapper.queryById(id);
            if (user == null) return RequestResults.notFound();
            return RequestResults.success(user);
        }
        return RequestResults.success(session.getAttribute(PlatformAttrKey.ATTR_USER));
    }

    @RequestMapping("/sendValidateCode")
    public String sendValidateCode(String email) {
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
                if (vc.getState().equals(ValidateCode.STATE_VALID) && vc.getCode().equals(code)) {
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
