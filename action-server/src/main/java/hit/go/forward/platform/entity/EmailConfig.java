package hit.go.forward.platform.entity;

import java.util.HashMap;

public class EmailConfig {
    public static final String TEMPLATE_VALIDATE_MAIL = "validateCodeMail";

    private String account;
    private String password;
    private String authCode;
    private String smtpHost;
    private HashMap<String, String> templates;

    /**
     * @return String return the account
     */
    public String getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @return String return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return String return the authCode
     */
    public String getAuthCode() {
        return authCode;
    }

    /**
     * @param authCode the authCode to set
     */
    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    /**
     * @return String return the smtpHost
     */
    public String getSmtpHost() {
        return smtpHost;
    }

    /**
     * @param smtpHost the smtpHost to set
     */
    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    /**
     * @return HashMap<String, String> return the templates
     */
    public HashMap<String, String> getTemplates() {
        return templates;
    }

    /**
     * @param templates the templates to set
     */
    public void setTemplates(HashMap<String, String> templates) {
        this.templates = templates;
    }
}