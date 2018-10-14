package hit.to.go.database.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/8/24
 */
public class MybatisProxy {
    private static final Logger logger = LoggerFactory.getLogger(MybatisProxy.class);

    private static Map<Class<?>, Object> cachedProxy = new HashMap<>();

//    @SuppressWarnings("unchecked")
//    public static <T> T create(Class<T> clazz) {
//        Object p = cachedProxy.get(clazz);
//        if (p == null) {
//            p = Proxy.newProxyInstance(
//                    clazz.getClassLoader(),
//                    new Class<?>[] {clazz},
//                    ((proxy, method, args) -> {
//                        SqlSessionFactory sessionFactory = DatabaseFactory.getSqlSessionFactory();
//                        SqlSession session = null;
//                        Object result = null;
//                        try {
//                            session = sessionFactory.openSession(false);
//                            result = method.invoke(session.getMapper(clazz), args);
//                            session.commit(true);
//                        } catch (Exception e){
//                            if (session != null) session.rollback(true);
//                            e.printStackTrace();
//                            return null;
//                        } finally {
//                            if (session != null) {
//                                session.close();
//                                session = null;
//                            }
//                        }
//                        return result;
//                    })
//            );
//            cachedProxy.put(clazz, p);
//        }
//        return (T) p;
//    }

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[] {clazz},
                ((proxy, method, args) -> {
                    SqlSessionFactory sessionFactory = DatabaseFactory.getSqlSessionFactory();
                    SqlSession session = null;
                    Object result = null;
                    try {
                        session = sessionFactory.openSession(true);
                        result = method.invoke(session.getMapper(clazz), args);
                        session.commit();
                    } catch (Exception e){
                        logger.error("数据库操作出错 {}", e.getMessage());
                        try {
                            if (session != null) {
                                logger.debug("数据库回滚");
                                session.rollback();
                            }
                        } catch (Exception exception) {
                            logger.error("数据库回滚出错 {}", e.getMessage());
                            exception.printStackTrace();
                        }
                        e.printStackTrace();
                        return null;
                    } finally {
                        if (session != null) {
                            session.close();
                            session = null;
                        }
                    }
                    return result;
                })
        );
    }
}
