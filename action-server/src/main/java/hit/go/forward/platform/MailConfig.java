package hit.go.forward.platform;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/9/29
 */
@Deprecated
public class MailConfig {
    public static final String TEMPLATE_VALIDATE_MAIL = "validateCodeMail";


    private String serverMailAddress = "15846376962@163.com";
    private String serverMailPassword = "cfmy0714..";
    private String host = "smtp.163.com";
    private String authCode = "byq0714";
    private Map<String, String> templates = new HashMap<>();

    public MailConfig() {}

    public MailConfig(String serverMailAddress, String serverMailPassword, String host, String authCode, Map<String, String> templates) {
        if (serverMailAddress != null) this.serverMailAddress = serverMailAddress;
        if (serverMailPassword != null) this.serverMailPassword = serverMailPassword;
        if (host != null) this.host = host;
        if (authCode != null) this.authCode = authCode;
        if (templates != null) this.templates = templates;
    }

    public String getServerMailAddress() {
        return serverMailAddress;
    }

    public String getServerMailPassword() {
        return serverMailPassword;
    }

    public String getHost() {
        return host;
    }

    public String getAuthCode() {
        return authCode;
    }

    public String getTemplate(String key) {
        return templates.get(key);
    }
}
