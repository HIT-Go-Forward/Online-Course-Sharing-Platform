package hit.go.forward.database.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * Created by 班耀强 on 2018/9/18
 */
public class DatabaseFactory {
    private static SqlSessionFactory sessionFactory = null;

    public static SqlSessionFactory getSqlSessionFactory() {
        try {
            if (sessionFactory == null)
                sessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessionFactory;
    }
}
