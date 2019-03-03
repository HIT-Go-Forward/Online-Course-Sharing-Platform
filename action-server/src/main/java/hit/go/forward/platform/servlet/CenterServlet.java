package hit.go.forward.platform.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.DispatcherServlet;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * Created by 班耀强 on 2018/9/19
 */
public class CenterServlet extends DispatcherServlet {
    private static final Logger logger = LoggerFactory.getLogger(CenterServlet.class);
    

    // 在销毁时关闭数据库驱动
    @Override
    public void destroy() {
        super.destroy();
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        Driver d = null;
        try {
            while (drivers.hasMoreElements()) {
                d = drivers.nextElement();
                DriverManager.deregisterDriver(d);
                logger.info(" 消除数据库连接驱动 --> : Driver {} deregistered", d);
            }
        } catch (SQLException ex) {
            logger.error("Error: deregistering driver {} exceptionName:{} detail:{}", d, ex.getClass().getName(), ex.getMessage());
        }

    }
}
