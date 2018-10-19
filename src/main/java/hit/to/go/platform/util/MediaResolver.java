package hit.to.go.platform.util;

import hit.to.go.database.dao.FileMapper;
import hit.to.go.database.mybatis.MybatisProxy;
import hit.to.go.platform.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by 班耀强 on 2018/9/20
 */
public class MediaResolver {
    private static final Logger logger = LoggerFactory.getLogger(MediaResolver.class);
    public static final String RESOURCE_SERVER_ADDRESS = "http://localhost:10080/media-server";


    public static String getRealUrl(String id) {
        FileMapper mapper = MybatisProxy.create(FileMapper.class);
        String url = SystemConfig.getMediaAddress() + mapper.queryUrlById(id);
        logger.debug("查询资源地址 {}", url);
        return url;
    }
}
