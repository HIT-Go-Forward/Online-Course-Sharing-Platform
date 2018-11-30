package hit.go.forward.platform;

import javax.servlet.http.Cookie;

/**
 * Created by 班耀强 on 2018/10/5
 */
public final class SystemVariable {
    public static final String ERR_HAVE_NO_RIGHT_URL = "/error/haveNoRight.action";
    public static final String ERR_NEED_LOGIN_URL = "/error/needLogin.action";


    public static final int TIME_S_7_DAYS = 604800;
    public static final int TIME_MS_15_MINUTES = 900000;
    public static final int TIME_MS_1_MINUTE = 60000;

    public static Cookie newDeleteIdCookie() {
        Cookie cookie = new Cookie("id", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }

    public static Cookie newDeletePasswordCookie() {
        Cookie cookie = new Cookie("password", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }

    public static Cookie newIdCookie(String id) {
        Cookie cookie = new Cookie("id", id);
        cookie.setPath("/");
        cookie.setMaxAge(TIME_S_7_DAYS);
        return cookie;
    }

    public static Cookie newPasswordCookie(String password) {
        Cookie cookie = new Cookie("password", password);
        cookie.setPath("/");
        cookie.setMaxAge(TIME_S_7_DAYS);
        return cookie;
    }
}
