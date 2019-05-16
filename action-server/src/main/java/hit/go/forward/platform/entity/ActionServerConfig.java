package hit.go.forward.platform.entity;


public class ActionServerConfig {
    private String version;
    private EmailConfig email;
    private MongoConfig mongo;

    /**
     * @return EmailConfig return the email
     */
    public EmailConfig getEmailConfig() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmailConfig(EmailConfig email) {
        this.email = email;
    }

    /**
     * @return MongoConfig return the mongo
     */
    public MongoConfig getMongoConfig() {
        return mongo;
    }

    /**
     * @param mongo the mongoConfig to set
     */
    public void setMongoConfig(MongoConfig mongo) {
        this.mongo = mongo;
    }

    /**
     * @return String return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

}