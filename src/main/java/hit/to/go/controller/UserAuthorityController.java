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
import hit.to.go.platform.SystemVariable;
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
import org.w3c.dom.Attr;

import javax.servlet.http.*;
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
    public String register(@RequestParam Map<String, Object> map, HttpSession session, HttpServletResponse response) {
        Object code = map.get("code");
        Object name = map.get("name");
        Object password = map.get("password");
        Object email = map.get("email");
        if (code == null || name == null || password == null || email == null) return RequestResults.wrongParameters();
        String result = validate(code.toString(), email.toString(), session);
        if (result.equals("success")) {
            UserMapper mapper = MybatisProxy.create(UserMapper.class);
            Integer rows = mapper.register(map);
            if (rows == null) return RequestResults.dataBaseWriteError();
            else if (rows.equals(0)) return RequestResults.forbidden("该邮箱已被注册");

            UserWithPassword user = mapper.selectUserByEmail(email.toString());
            session.setAttribute(AttrKey.ATTR_USER, user);
            response.addCookie(SystemVariable.newIdCookie(user.getId().toString()));
            response.addCookie(SystemVariable.newPasswordCookie(user.getPassword()));

            return RequestResults.success(user);
        }


        return RequestResults.forbidden(result);
    }

    @RequestMapping("/modifyInfo")
    public String modifyInfo(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, @RequestParam Map<String, String> paras, HttpSession session) {
        paras.put("id", user.getId().toString());
        if (paras.get("name") == null) return RequestResults.wrongParameters();
        UserMapper mapper = MybatisProxy.create(UserMapper.class);
        Integer rows = mapper.completeInfo(paras);
        user = mapper.selectUserById(user.getId().toString());

        if (rows != null && rows.equals(1)) {
            session.setAttribute(AttrKey.ATTR_USER, user);
            return RequestResults.success(user);
        }
        return RequestResults.error("保存失败!");
    }

    @RequestMapping("/login")
    public String login(HttpSession session, HttpServletResponse response, String account, String password) {
        if (account != null && password != null) {
            UserWithPassword user = null;
            UserMapper mapper = MybatisProxy.create(UserMapper.class);
            if (account.matches("^\\d+$")) {
                user = mapper.selectUserById(account);
            } else if (account.matches("^\\w+@\\w+$")) {
                user = mapper.selectUserByEmail(account);
            } else return RequestResults.forbidden("请输入正确的账号！");
            if (user != null) {
                if (user.getPassword().equals(password)) {
                    session.setAttribute(AttrKey.ATTR_USER, user);
                    response.addCookie(SystemVariable.newIdCookie(user.getId().toString()));
                    response.addCookie(SystemVariable.newPasswordCookie(user.getPassword()));
                    return RequestResults.success(user);
                }
                return RequestResults.forbidden("账号或密码错误！");
            }
        }
        return RequestResults.forbidden("请填写账号密码！");
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        response.addCookie(SystemVariable.newDeleteIdCookie());
        response.addCookie(SystemVariable.newDeletePasswordCookie());
        session.removeAttribute(AttrKey.ATTR_USER);
        return RequestResults.success();
    }

    @RequestMapping("/changePassword")
    public String changePassword(String oldPassword, String newPassword, String code, HttpSession session, @SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user) {
        if (oldPassword == null || newPassword == null || code == null) return RequestResults.wrongParameters();
        if (!user.getPassword().equals(oldPassword)) return RequestResults.forbidden("原密码错误");

        String result = validate(code, user.getEmail(), session);
        if (!result.equals("success")) return RequestResults.forbidden(result);

        UserMapper mapper = MybatisProxy.create(UserMapper.class);
        Map<String, String> paras = new HashMap<>();
        paras.put("id", user.getId().toString());
        paras.put("password", newPassword);
        Integer rows = mapper.changePassword(paras);
        if (rows == null || rows.equals(0)) return RequestResults.dataBaseWriteError();
        else if (rows.equals(1)) {
            session.removeAttribute(AttrKey.ATTR_USER);
            return RequestResults.success();
        }
        return RequestResults.error();
    }

    @RequestMapping("/getUserInfo")
    public String getUserInfo(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, String id) {
        if (id != null && user.getId() != null && !id.equals(user.getId().toString())) {
            UserMapper mapper = MybatisProxy.create(UserMapper.class);
            User u = mapper.queryById(id);
            if (u == null) return RequestResults.notFound("用户不存在！");
            return RequestResults.success(u);
        }
        return RequestResults.success(user);
    }

    @RequestMapping("/sendValidateCode")
    public String sendValidateCode(String email, HttpSession session) {
        if (email == null) return RequestResults.wrongParameters();
        Date now = new Date();
        ValidateCode code = (ValidateCode) session.getAttribute(AttrKey.ATTR_VALIDATE_CODE);

        if (code != null && now.getTime() - code.getDate().getTime() < SystemVariable.TIME_MS_1_MINUTE)
            return RequestResults.forbidden("请于一分钟之后再试");

        String c = Validate.genValidateCode();
        code = new ValidateCode(email, c);
        boolean flag = MailUtil.send(email, SystemConfig.getMailConfig().getTemplate(MailConfig.TEMPLATE_VALIDATE_MAIL).replaceAll("\\{validateCode}", c));
        if (flag) {
            session.setAttribute(AttrKey.ATTR_VALIDATE_CODE, code);
            return RequestResults.success();
        }

        return RequestResults.error("邮件发送失败, 请稍后再试~");
    }

    @RequestMapping("/sendValidateCodeToCurrentUser")
    public String sendValidateCodeToCurrentUser(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, HttpSession session) {
        String email = user.getEmail();
        if (email == null) return RequestResults.wrongParameters();
        Date now = new Date();
        ValidateCode code = (ValidateCode) session.getAttribute(AttrKey.ATTR_VALIDATE_CODE);

        if (code != null && now.getTime() - code.getDate().getTime() < SystemVariable.TIME_MS_1_MINUTE)
            return RequestResults.forbidden("请于一分钟之后再试");

        String c = Validate.genValidateCode();

        code = new ValidateCode(email, c);
        boolean flag = MailUtil.send(email, SystemConfig.getMailConfig().getTemplate(MailConfig.TEMPLATE_VALIDATE_MAIL).replaceAll("\\{validateCode}", c));
        if (flag) {
            session.setAttribute(AttrKey.ATTR_VALIDATE_CODE, code);
            return RequestResults.success();
        }

        return RequestResults.error("邮件发送失败, 请稍后再试~");
    }

    @RequestMapping("/validateCode")
    public String validateCode(String email, String code, HttpSession session) {
        if (email == null || code == null) return RequestResults.wrongParameters();

        String result = validate(code, email, session);
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

    private String validate(String code, String email, HttpSession session) {
        Date now = new Date();
        ValidateCode vc = (ValidateCode) session.getAttribute(AttrKey.ATTR_VALIDATE_CODE);
        if (vc == null) return "请先获取验证码";
        else if (!vc.getCode().equalsIgnoreCase(code) || !vc.getEmail().equals(email)) return "验证码错误";
        else if (vc.getState().equals(ValidateCode.STATE_INVALID)) return "验证码已失效";
        else if (now.getTime() - vc.getDate().getTime() > SystemVariable.TIME_MS_15_MINUTES) return "验证码已过期";
        else {
//            session.removeAttribute(AttrKey.ATTR_VALIDATE_CODE);
            vc.setState(ValidateCode.STATE_INVALID);
            return "success";
        }
    }
}
