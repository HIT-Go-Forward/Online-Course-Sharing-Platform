package hit.to.go.controller;

import hit.to.go.database.dao.UserMapper;
import hit.to.go.database.dao.ValidateCodeMapper;
import hit.to.go.database.mybatis.MybatisProxy;
import hit.to.go.entity.user.User;
import hit.to.go.entity.validate.ValidateCode;
import hit.to.go.platform.MailConfig;
import hit.to.go.platform.PlatformAttrKey;
import hit.to.go.platform.SystemConfig;
import hit.to.go.platform.protocol.RequestResult;
import hit.to.go.platform.protocol.RequestResults;
import hit.to.go.platform.util.MailUtil;
import hit.to.go.platform.util.Validate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
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

    @RequestMapping("/completeInfo")
    public String completeInfo(@RequestParam Map<String, String> paras) {
        UserMapper mapper = MybatisProxy.create(UserMapper.class);
        String id = paras.get("id");
        String password = paras.get("password");

        if (id != null && password != null) {
            Integer rows = mapper.completeInfo(paras);
            if (rows != null && rows.equals(1)) return RequestResults.success();
            return RequestResults.error("保存失败!");
        }

        return RequestResults.forbidden("完善账号信息需要账号密码!");
    }

    @RequestMapping("/login")
    public String login(@RequestParam Map<String, String> map) {
        String id, email, password;
        id = map.get("id");
        email = map.get("email");
        password = map.get("password");

        if (id == null && email == null || password == null) return RequestResults.forbidden("密码不能为空, 账号或邮箱必须提供其一");
        UserMapper mapper = MybatisProxy.create(UserMapper.class);

        Map<String, String> result;
        if (id != null) result = mapper.loginById(map);
        else result = mapper.loginByEmail(map);

        if (result != null) return RequestResults.success(result);

        return RequestResults.forbidden("账号或密码错误!");
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(PlatformAttrKey.ATTR_USER);
        return RequestResults.success();
    }

    @RequestMapping("/changePassword")
    public String changePassword(Integer id, String oldPassword, String newPassword, String code) {
        if (id != null && oldPassword != null && newPassword != null && code != null) {
            UserMapper mapper = MybatisProxy.create(UserMapper.class);
            String email = mapper.selectEmailById(id);
            String result = null;
            if (email != null ) {
                result = validateCode(code, email);
                if (result.equals("success")) {
                    String password = mapper.selectPassword(id);
                    if (password != null && password.equals(oldPassword)) {
                        Map<String, String> paras = new HashMap<>();
                        paras.put("id", id.toString());
                        paras.put("password", newPassword);
                        Integer rows = mapper.changePassword(paras);
                        if (rows != null && rows.equals(1)) return RequestResults.success();
                    }
                    return RequestResults.forbidden("原密码错误!");
                }
                return RequestResults.forbidden(result);
            }
            return RequestResults.forbidden("用户不存在!");
        }
        return RequestResults.error("请检查必填信息是否完整!");
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
