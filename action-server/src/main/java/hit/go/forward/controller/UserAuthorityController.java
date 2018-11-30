package hit.go.forward.controller;

import hit.go.forward.database.dao.UserMapper;
import hit.go.forward.common.entity.user.User;
import hit.go.forward.common.entity.user.UserWithPassword;
import hit.go.forward.common.entity.validate.ValidateCode;
import hit.go.forward.platform.AttrKey;
import hit.go.forward.platform.MailConfig;
import hit.go.forward.platform.SystemConfig;
import hit.go.forward.platform.SystemVariable;
import hit.go.forward.platform.exception.RequestHandleException;
import hit.go.forward.platform.util.MailUtil;
import hit.go.forward.platform.util.Validate;
import hit.go.forward.protocol.RequestResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.*;
import java.text.SimpleDateFormat;
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

    private UserMapper userMapper;

    public UserAuthorityController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Transactional
    @RequestMapping("/register")
    public String register(@RequestParam Map<String, Object> map, HttpSession session, HttpServletResponse response) {
        Object code = map.get("code");
        Object name = map.get("name");
        Object password = map.get("password");
        Object email = map.get("email");
        if (code == null || name == null || password == null || email == null) return RequestResults.wrongParameters();
        String result = validate(code.toString(), email.toString(), session);
        if (result.equals("success")) {
            Integer rows = userMapper.register(map);
            if (rows == null) throw new RequestHandleException(RequestResults.dataBaseWriteError());
            else if (rows.equals(0)) throw new RequestHandleException(RequestResults.forbidden("该邮箱已被注册"));

            UserWithPassword user = new UserWithPassword();
            user.setPassword(password.toString());
            user.setId(Integer.valueOf(map.get("user_id").toString()));
            user.setType(User.TYPE_STUDENT);
            user.setEmail(email.toString());
            user.setName(name.toString());
            session.setAttribute(AttrKey.ATTR_USER, user);
            response.addCookie(SystemVariable.newIdCookie(user.getId().toString()));
            response.addCookie(SystemVariable.newPasswordCookie(user.getPassword()));

            return RequestResults.success(user);
        }
        return RequestResults.forbidden(result);
    }

    @Transactional
    @RequestMapping("/modifyInfo")
    public String modifyInfo(@SessionAttribute(AttrKey.ATTR_USER) User user, @RequestParam Map<String, String> paras, HttpSession session) {
        paras.put("id", user.getId().toString());
        Integer rows = userMapper.completeInfo(paras);
//        user = userMapper.selectUserById(user.getId().toString());
        if (rows != null && rows.equals(1)) {
            String edu = paras.get("education");
            String sex = paras.get("sex");
            String birthday = paras.get("birthday");
            String intro = paras.get("intro");
            String note = paras.get("note");
            String phone = paras.get("phone");
            user.setIntro(intro);
            user.setNote(note);
            user.setPhone(phone);
            if (birthday != null) {
                SimpleDateFormat sdm = new SimpleDateFormat();
                try {
                    Date date = sdm.parse(birthday);
                    user.setBirthday(date);
                } catch (Exception e) {
                    user.setBirthday(null);
                }
            }
            if (edu != null) user.setEducation(Integer.valueOf(edu));
            user.setSex(sex);
            return RequestResults.success(user);
        }
        throw new RequestHandleException(RequestResults.error("保存失败!"));
    }

    @RequestMapping("/login")
    public String login(HttpSession session, HttpServletResponse response, String account, String password) {
        if (account != null && password != null) {
            UserWithPassword user = null;
            if (account.matches("^\\d+$")) {
                user = userMapper.selectUserById(account);
            } else if (account.matches("^\\w+@.+$")) {
                user = userMapper.selectUserByEmail(account);
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
            return RequestResults.error();
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

    @Transactional
    @RequestMapping("/updateUserImg")
    public String updateUserImg(String fileId, @SessionAttribute(AttrKey.ATTR_USER) User user, HttpSession session) {
        if (fileId == null) return RequestResults.wrongParameters();
        Map<String, Object> paras = new HashMap<>();
        paras.put("id", user.getId());
        paras.put("img", fileId);
        Integer rows = userMapper.updateUserImg(paras);
        if (rows != null && rows.equals(1)) {
            user = userMapper.selectUserById(user.getId().toString());
            session.setAttribute(AttrKey.ATTR_USER, user);
            return RequestResults.success(user);
        }
        throw new RequestHandleException(RequestResults.dataBaseWriteError());
    }

    @Transactional
    @RequestMapping("/changePassword")
    public String changePassword(String oldPassword, String newPassword, String code, HttpSession session, @SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user) {
        if (oldPassword == null || newPassword == null || code == null) return RequestResults.wrongParameters();
        if (!user.getPassword().equals(oldPassword)) return RequestResults.forbidden("原密码错误");

        String result = validate(code, user.getEmail(), session);
        if (!result.equals("success")) return RequestResults.forbidden(result);

        Map<String, String> paras = new HashMap<>();
        paras.put("id", user.getId().toString());
        paras.put("password", newPassword);
        Integer rows = userMapper.changePassword(paras);
        if (rows == null || rows.equals(0)) return RequestResults.dataBaseWriteError();
        else if (rows.equals(1)) {
            session.removeAttribute(AttrKey.ATTR_USER);
            return RequestResults.success();
        }
        throw new RequestHandleException(RequestResults.error());
    }

    @RequestMapping("/getUserInfo")
    public String getUserInfo(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, String userId) {
        if (userId != null && user.getId() != null && !userId.equals(user.getId().toString())) {
            User u = userMapper.selectUserById(userId);
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
            Integer id = userMapper.selectIdByEmail(email);
            if (id != null) return RequestResults.forbidden("该邮箱已被注册!");
            return RequestResults.success("邮箱未被注册, 可以使用!");
        }

        return RequestResults.badRequest("email不能为空");
    }

    @RequestMapping("/autoLogin")
    public String autoLogin(String $cookiePassword, String $cookieId, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        if ($cookieId != null && $cookiePassword != null) {
            UserWithPassword user = userMapper.selectUserById($cookieId);
            if (user != null && user.getPassword().equals($cookiePassword)) {
                session.setAttribute(AttrKey.ATTR_USER, user);
                logger.debug("用户{}自动登陆成功", user.getName());
            } else {
                logger.debug("用户名或密码错误， 删除cookie");
                response.addCookie(SystemVariable.newDeleteIdCookie());
                response.addCookie(SystemVariable.newDeletePasswordCookie());
            }
            try {
                logger.debug("转发回原请求 {}", request.getRequestURI());
                request.getRequestDispatcher(request.getRequestURI()).forward(request, response);
            } catch (Exception e) {
                logger.debug("转发失败!");
                e.printStackTrace();
            }
        }
        return null;
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
