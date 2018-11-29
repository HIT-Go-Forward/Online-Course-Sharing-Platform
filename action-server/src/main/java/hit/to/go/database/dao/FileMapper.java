package hit.to.go.database.dao;

import hit.to.go.entity.resource.Resource;

import java.util.Map;

/**
 * Created by 班耀强 on 2018/9/20
 */
public interface FileMapper {
    String queryUrlById(String id);

    Integer addNewFile(Resource file);

    Integer updateFile(Resource file);

    String selectFileIdByUrl(String url);
}
