package hit.go.forward.platform.entity;


public class ActionServerConfig {
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
}