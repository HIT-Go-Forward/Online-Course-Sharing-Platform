package hit.go.forward.database.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/8/24
 */
public class MybatisProxy {

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
                        session = sessionFactory.openSession(false);
                        result = method.invoke(session.getMapper(clazz), args);
                        session.commit(true);
                    } catch (Exception e){
                        try {
                            if (session != null) {
                                session.rollback(true);
                            }
                        } catch (Exception exception) {
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
