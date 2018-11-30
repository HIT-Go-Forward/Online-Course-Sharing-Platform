package hit.go.forward.entity;

public class AuthorityJWTData {
    public static final String ACCOUNT = "account";
    public static final String CACHE_KEY = "cacheKey";

    private String account;
    private String cacheKey;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }
}
