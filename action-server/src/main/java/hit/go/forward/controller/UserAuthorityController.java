package hit.go.forward.controller;

import hit.go.forward.business.database.dao.UserMapper;
import hit.go.forward.common.entity.user.User;
import hit.go.forward.common.entity.user.UserWithPassword;
import hit.go.forward.common.entity.user.UserWithToken;
import hit.go.forward.common.entity.validate.ValidateCode;
import hit.go.forward.common.util.MapperOpResultUtil;
import hit.go.forward.common.util.UserUtil;
import hit.go.forward.platform.*;
import hit.go.forward.platform.entity.EmailConfig;
import hit.go.forward.common.exception.RequestHandleException;
import hit.go.forward.platform.util.MailUtil;
import hit.go.forward.platform.util.Validate;
import hit.go.forward.common.protocol.RequestResult;
import hit.go.forward.common.protocol.RequestResults;
import hit.go.forward.service.UserAuthorityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    private UserMapper userMapper;
    private UserAuthorityService authorityService;

    public UserAuthorityController(UserMapper userMapper, UserAuthorityService authorityService) {
        this.userMapper = userMapper;
        this.authorityService = authorityService;
    }

    @Transactional
    @RequestMapping("/register")
    public RequestResult register(@RequestParam Map<String, Object> map) {
        Object code = map.get("code");
        Object name = map.get("name");
        Object password = map.get("password");
        Object email = map.get("email");
        if (code == null || name == null || password == null || email == null) return RequestResults.lackNecessaryParam("code || name || password || email");
        String result = validate(code.toString(), email.toString());
        if (result.equals("success")) {
            Integer rows = userMapper.register(map);
            if (rows == null) throw new RequestHandleException(RequestResults.requestCausedDBWritesError());
            else if (rows.equals(0)) throw new RequestHandleException(RequestResults.requestCausedDBWritesError("该邮箱已被注册"));

            UserWithToken user = new UserWithToken();
            user.setId(Integer.valueOf(map.get("user_id").toString()));
            user.setType(User.TYPE_STUDENT);
            user.setEmail(email.toString());
            user.setName(name.toString());
            user.setToken(authorityService.generateToken(user));
            return RequestResults.success(user);
        }
        return RequestResults.validateFailed(result);
    }

    @Transactional
    @RequestMapping("/modifyInfo")
    public RequestResult modifyInfo(@RequestParam Map<String, String> paras, String $userId) {
        User user = new User();
        paras.put("id", $userId);
        user.setId(Integer.valueOf($userId));
//        user = userMapper.selectUserById(user.getId().toString());
        if (MapperOpResultUtil.isSucceded(userMapper.completeInfo(paras))) return RequestResults.success(paras);
        throw new RequestHandleException(RequestResults.requestCausedDBWritesError());
    }

    @RequestMapping("/login")
    public RequestResult login(String account, String password) {
        if (account != null && password != null) {
            UserWithPassword user = null;
            if (account.matches("^\\d+$")) {
                user = userMapper.selectUserById(account);
            } else if (account.matches("^.+@.+$")) {
                user = userMapper.selectUserByEmail(account);
            } else return RequestResults.invalidParamValue("请输入正确的账号！");
            if (user != null) {
                if (user.getPassword().equals(password)) {
                    UserWithToken ut = UserUtil.toTokenUser(user);
                    ut.setToken(authorityService.generateToken(user));
                    return RequestResults.success(ut);
                }
                return RequestResults.validateFailed("账号或密码错误！");
            } return RequestResults.notFound("用户不存在！");
        }
        return RequestResults.lackNecessaryParam("account || password");
    }

    @RequestMapping("/logout")
    public RequestResult logout(HttpSession session, HttpServletResponse response) {

        return RequestResults.success();
    }

    @Transactional
    @RequestMapping("/updateUserImg")
    public RequestResult updateUserImg(String fileId, String $userId) {
        if (fileId == null) return RequestResults.lackNecessaryParam("fileId");
        Map<String, Object> paras = new HashMap<>();
        paras.put("id", $userId);
        paras.put("img", fileId);
        Integer rows = userMapper.updateUserImg(paras);
        if (rows != null && rows.equals(1)) {
            User user = userMapper.selectUserById($userId);
            return RequestResults.success(user);
        }
        throw new RequestHandleException(RequestResults.requestCausedDBWritesError());
    }

    @Transactional
    @RequestMapping("/changePassword")
    public RequestResult changePassword(String oldPassword, String newPassword, String code, HttpSession session, String $userId) {
        // TODO 修改密码后让之前的token失效
        
        if (oldPassword == null || newPassword == null || code == null) return RequestResults.lackNecessaryParam("oldPassword || newPassword || code");

        UserWithPassword user = userMapper.selectUserById($userId);
        if (!user.getPassword().equals(oldPassword)) return RequestResults.validateFailed("原密码错误");

        String result = validate(code, user.getEmail());
        if (!result.equals("success")) return RequestResults.validateFailed(result);

        Map<String, String> paras = new HashMap<>();
        paras.put("id", user.getId().toString());
        paras.put("password", newPassword);
        Integer rows = userMapper.changePassword(paras);
        if (rows == null || rows.equals(0)) return RequestResults.requestCausedDBWritesError();
        else if (rows.equals(1)) {
            session.removeAttribute(AttrKey.ATTR_USER);
            return RequestResults.success();
        }
        throw new RequestHandleException(RequestResults.requestCausedDBWritesError());
    }

    @RequestMapping("/getUserInfo")
    public RequestResult getUserInfo(String $userId, String userId) {
        if (userId == null) userId = $userId;
        User u = userMapper.selectUserById(userId);
        if (u == null) return RequestResults.notFound("用户不存在！");
        return RequestResults.success(u);
    }

    @RequestMapping("/sendValidateCode")
    public RequestResult sendValidateCode(String email) {
        if (email == null) return RequestResults.lackNecessaryParam("email");
        Date now = new Date();
        ValidateCode code = SystemStorage.getValidateCode(email);

        if (code != null && now.getTime() - code.getDate().getTime() < SystemVariable.TIME_MS_1_MINUTE)
            return RequestResults.operationDenied("请于一分钟之后再试");

        String c = Validate.genValidateCode();
        code = new ValidateCode(email, c);
        boolean flag = MailUtil.sendEmail(email, SystemConfig.getEmailTemplate(EmailConfig.TEMPLATE_VALIDATE_MAIL).replaceAll("\\{validateCode}", c));
        if (flag) {
            SystemStorage.store(email, code);
            return RequestResults.success();
        }

        return RequestResults.serverError();
    }

    @RequestMapping("/sendValidateCodeToCurrentUser")
    public RequestResult sendValidateCodeToCurrentUser(String $userId) {
        String email = userMapper.selectEmailById($userId);
        if (email == null) return RequestResults.lackNecessaryParam("email");
        Date now = new Date();
        ValidateCode code = SystemStorage.getValidateCode(email);

        if (code != null && now.getTime() - code.getDate().getTime() < SystemVariable.TIME_MS_1_MINUTE)
            return RequestResults.operationDenied("请于一分钟之后再试");

        String c = Validate.genValidateCode();

        code = new ValidateCode(email, c);
        boolean flag = MailUtil.sendEmail(email, SystemConfig.getEmailTemplate(EmailConfig.TEMPLATE_VALIDATE_MAIL).replaceAll("\\{validateCode}", c));
        if (flag) {
            SystemStorage.store(email, code);
            return RequestResults.success();
        }

        return RequestResults.serverError();
    }

    @RequestMapping("/validateCode")
    public RequestResult validateCode(String email, String code) {
        if (email == null || code == null) return RequestResults.lackNecessaryParam("email || code");

        String result = validate(code, email);
        if (result.equals("success")) return RequestResults.success();
        return RequestResults.validateFailed(result);
    }

    @RequestMapping("/uniqueEmail")
    public RequestResult uniqueEmail(String email) {
        if (email != null) {
            Integer id = userMapper.selectIdByEmail(email);
            if (id != null) return RequestResults.queryExisted("该邮箱已被注册!");
            return RequestResults.queryNotExist("邮箱未被注册, 可以使用!");
        }

        return RequestResults.lackNecessaryParam("email");
    }


    @RequestMapping("/autoLogin")
    public RequestResult autoLogin(String $cookiePassword, String $cookieId, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
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

    private String validate(String code, String email) {
        Date now = new Date();
        ValidateCode vc = SystemStorage.getValidateCode(email);
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
