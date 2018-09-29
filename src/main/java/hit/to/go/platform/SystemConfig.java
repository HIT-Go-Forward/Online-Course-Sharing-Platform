package hit.to.go.platform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/9/20
 */
public class SystemConfig {
    private static final Logger logger = LoggerFactory.getLogger(SystemConfig.class);

    private static String mediaAddress;
    private static int mediaPort;

    private static MailConfig mailConfig;

    static {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            URL url = SystemConfig.class.getClassLoader().getResource("/server-config.xml");
            Document document = db.parse(url.toString());
            NodeList nodeList = document.getElementsByTagName("server");
            Node server = nodeList.item(0);
            nodeList = server.getChildNodes();
            for (int i = 0;i < nodeList.getLength();i ++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    String name = node.getNodeName();
                    switch (name) {
                        case "media-server":
                            parseMediaServer(node);
                            break;
                        case "mail":
                            parseMail(node);
                            break;
                        default:
                            logger.debug("tag name {}", name);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.debug("系统配置分析完成");
    }


    public static MailConfig getMailConfig() {
        return mailConfig;
    }

    public static String getMediaAddress() {
        return mediaAddress;
    }

    public static int getMediaPort() {
        return mediaPort;
    }

    private static void parseMediaServer(Node mediaServer) {
        NodeList nodeList = mediaServer.getChildNodes();
        for (int i = 0;i < nodeList.getLength();i ++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                logger.debug(node.getNodeName());
                if (node.getNodeName().equals("address")) mediaAddress = node.getFirstChild().getNodeValue();
                else if (node.getNodeName().equals("port")) mediaPort = Integer.valueOf(node.getFirstChild().getNodeValue());
            }
        }
    }

    private static void parseMail(Node mail) throws Exception {
        String account = null, password = null, authCode = null, smtpHost = null;
        Map<String, String> templates = null;

        NodeList nodeList = mail.getChildNodes();
        for (int i = 0;i < nodeList.getLength();i ++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String tagName = node.getNodeName();
                if (tagName.equals("account")) account = node.getFirstChild().getNodeValue();
                else if (tagName.equals("password")) password = node.getFirstChild().getNodeValue();
                else if (tagName.equals("auth-code")) authCode = node.getFirstChild().getNodeValue();
                else if (tagName.equals("smtp-host")) smtpHost = node.getFirstChild().getNodeValue();
                else if (tagName.equals("templates")) {
                    templates = new HashMap<>();
                    NodeList nl = node.getChildNodes();
                    for (int j = 0;j < nl.getLength();j ++) {
                        Node t = nl.item(j);
                        String key = null, value = null;
                        if (t.getNodeType() == Node.ELEMENT_NODE) {
                            NodeList tcl = t.getChildNodes();
                            for (int k = 0;k < tcl.getLength();k ++) {
                                Node kv = tcl.item(k);
                                if (kv.getNodeType() == Node.ELEMENT_NODE) {
                                    if (kv.getNodeName().equals("key")) key = kv.getFirstChild().getNodeValue();
                                    else if (kv.getNodeName().equals("value")) value = kv.getFirstChild().getNodeValue();
                                }
                            }
                        }
                        if (key != null) templates.put(key, value);
                    }
                } else throw new Exception("Invalid config tag found: " + tagName);
            }
        }

        mailConfig = new MailConfig(account, password, smtpHost, authCode, templates);
    }
}

