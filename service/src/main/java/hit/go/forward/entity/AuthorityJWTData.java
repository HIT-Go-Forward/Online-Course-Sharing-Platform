package hit.go.forward.entity;

import java.util.HashMap;
import java.util.Map;

public class AuthorityJWTData {
    public static final String ACCOUNT = "account";
    public static final String CACHE_KEY = "cacheKey";

    private HashMap<String, String> map;

    private String account;
    private String cacheKey;

    public AuthorityJWTData() {
        this.map = new HashMap<>();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
        map.put(ACCOUNT, account);
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
        map.put(CACHE_KEY, cacheKey);
    }

    public Map<String, String> asHashMap() {
        return map;
    }
}
