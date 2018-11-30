package hit.go.forward.database.dao;

import hit.go.forward.entity.resource.Resource;

/**
 * Created by 班耀强 on 2018/9/20
 */
public interface FileMapper {
    String queryUrlById(String id);

    Integer addNewFile(Resource file);

    Integer updateFile(Resource file);

    String selectFileIdByUrl(String url);
}
