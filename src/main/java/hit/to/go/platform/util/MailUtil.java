package hit.to.go.platform.util;

import com.sun.mail.util.MailSSLSocketFactory;
import hit.to.go.platform.MailConfig;
import hit.to.go.platform.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by 班耀强 on 2018/9/29
 */
public class MailUtil {
    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

    public static boolean send(String to, String msg) {
        MailConfig config = SystemConfig.getMailConfig();

        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", config.getHost());
        properties.put("mail.smtp.auth", "true");
        if (sf != null) {
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);
        }


        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.getServerMailAddress(), config.getServerMailPassword());
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(config.getServerMailAddress()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("HIT-Go-Forward团队");
            message.setText(msg);
            Transport transport = session.getTransport("smtp");
            transport.connect(config.getHost(), config.getServerMailAddress(), config.getAuthCode());
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            logger.debug("邮件 to {} 已发送成功!", to);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
