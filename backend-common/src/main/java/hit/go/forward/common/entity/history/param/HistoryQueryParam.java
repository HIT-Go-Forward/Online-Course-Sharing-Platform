package hit.go.forward.common.entity.history.param;

import hit.go.forward.common.entity.param.PageParam;

public class HistoryQueryParam extends PageParam {
    private String userId;

    /**
     * @return String return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

}