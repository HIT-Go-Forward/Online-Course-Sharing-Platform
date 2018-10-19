package hit.to.go.database.dao;

import java.util.Map;

/**
 * Created by 班耀强 on 2018/9/20
 */
public interface FileMapper {
    String queryUrlById(String id);

    Integer newFile(Map<String, Object> paras);
}
