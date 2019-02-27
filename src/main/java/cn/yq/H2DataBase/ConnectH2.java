package cn.yq.H2DataBase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 连接H2数据库，获取连接
 */
public class ConnectH2 {
    private static Logger logger = LoggerFactory.getLogger(ConnectH2.class);
    //数据库连接URL，当前连接的是目录下的gacl数据库
    private static final String JDBC_URL = "jdbc:h2:./config/H2";
    //连接数据库时使用的用户名
    private static final String USER = "sa";
    //连接数据库时使用的密码
    private static final String PASSWORD = "";
    //连接H2数据库时使用的驱动类，org.h2.Driver这个类是由H2数据库自己提供的，在H2数据库的jar包中可以找到
    private static final String DRIVER_CLASS="org.h2.Driver";
    //
    public static Connection conn;
    public static  Connection connectH2() throws ClassNotFoundException{
        // 加载H2数据库驱动
        Class.forName(DRIVER_CLASS);
        // 根据连接URL，用户名，密码获取数据库连接
        try{
         conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        }catch (SQLException e){
        logger.info("连接数据库错误！"+e.getMessage());
        }
        return conn;
    }
}
