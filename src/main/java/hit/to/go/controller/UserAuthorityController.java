package hit.to.go.controller;

import hit.to.go.database.dao.UserMapper;
import hit.to.go.database.dao.ValidateCodeMapper;
import hit.to.go.database.mybatis.MybatisProxy;
import hit.to.go.entity.user.User;
import hit.to.go.entity.validate.ValidateCode;
import hit.to.go.platform.MailConfig;
import hit.to.go.platform.PlatformAttrKey;
import hit.to.go.platform.SystemConfig;
import hit.to.go.platform.protocol.RequestResults;
import hit.to.go.platform.util.MailUtil;
import hit.to.go.platform.util.Validate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String register(@RequestParam Map<String, String> map) {
        UserMapper mapper = MybatisProxy.create(UserMapper.class);
        Integer id = mapper.signUp(map);
        if (id != null && id.equals(1)) return RequestResults.success(map.get("user_id"));
        return RequestResults.badRequest("请检查必需字段是否为空!");
    }

    @RequestMapping("/login")
    public String login(@RequestParam Map<String, String> map, HttpSession session) {
        String id, password;
        id = map.get("id");
        password = map.get("password");
        if (id != null && password != null) {
            User user = (User) session.getAttribute(PlatformAttrKey.ATTR_USER);
            if (user != null && user.getId().equals(Integer.valueOf(id)))
                return RequestResults.forbidden("请勿重复登录!");
            else {
                UserMapper mapper = MybatisProxy.create(UserMapper.class);
                user = mapper.login(map);
                if (user != null) {
                    session.setAttribute(PlatformAttrKey.ATTR_USER, user);
                    return RequestResults.success(user);
                }
            }
        }
        return RequestResults.badRequest("用户名或密码错误!");
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(PlatformAttrKey.ATTR_USER);
        return RequestResults.success();
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
    public String sendValidateCode(String to) {
        boolean update = false;
        String code;
        Map<String, Object> map;

        Date now = new Date();
        ValidateCodeMapper mapper = MybatisProxy.create(ValidateCodeMapper.class);
        ValidateCode vc = mapper.queryValidateCode(to);
        if (vc != null) {
            // 判断距上次请求是否小于一分钟
            if (now.getTime() - vc.getDate().getTime() < 60000) return RequestResults.forbidden("请于一分钟之后再试!");
            update = true;
        }
        map = new HashMap<>();
        code = Validate.genValidateCode();

        map.put("code", code);
        map.put("date", now);
        map.put("email", to);

        Integer rows = null;
        boolean flag;
        if (update) rows = mapper.updateValidateCode(map);
        else rows = mapper.insertValidateCode(map);
        if (rows != null && rows.equals(1)) {
            flag = MailUtil.send(to, SystemConfig.getMailConfig().getTemplate(MailConfig.TEMPLATE_VALIDATE_MAIL).replaceAll("\\{validateCode}", code));
            if (flag) return RequestResults.success();
        }
        return RequestResults.error("邮件发送失败, 请稍后再试~");
    }

    @RequestMapping("/validateCode")
    public String validateCode(@RequestParam Map<String, String> map) {
        Date now = new Date();

        String email = map.get("email");
        String code = map.get("code");

        if (email != null && code != null) {
            ValidateCodeMapper mapper = MybatisProxy.create(ValidateCodeMapper.class);
            ValidateCode vc = mapper.queryValidateCode(email);

            if (vc != null && (now.getTime() - vc.getDate().getTime() < 900000)) {
                if (vc.getState().equals(ValidateCode.STATE_VALID) && vc.getCode().equals(code)) {
                    mapper.becomeInvalid(email);
                    return RequestResults.success();
                }
                return RequestResults.error("验证码错误或已失效, 请重试!");
            }
        }

        return RequestResults.error("验证码与邮箱均不能为空");
    }
}
