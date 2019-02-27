package cn.yq.H2DataBase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import static cn.yq.GUI.Datetime.getDatetime;

public class H2Manager {
    private static Logger logger = LoggerFactory.getLogger(H2Manager.class);
    public static List<String> search(String sql){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<String> LIST = new LinkedList<String>();
       try {
           conn = ConnectH2.connectH2();
           stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
           while (rs.next()){
//               System.out.println(rs.getInt("id") + "," + rs.getString("key")+ "," + rs.getString("value"));
                    LIST.add(rs.getString("key"));
           }
//           //释放资源
//           stmt.close();
//           //关闭连接
//           conn.close();
        }catch (SQLException e){
           logger.info("查询信息失败！");
       }catch (ClassNotFoundException e){
           logger.info("未找到类！");
       }
       return LIST;
    }
    public static String searchValue(String sql){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String LIST = "";
        try {
            conn = ConnectH2.connectH2();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()){
                LIST=rs.getString("VALUE");
            }
//           //释放资源
//           stmt.close();
//           //关闭连接
//           conn.close();
        }catch (SQLException e){
            logger.info("查询信息失败！");
        }catch (ClassNotFoundException e){
            logger.info("未找到类！");
        }
        return LIST;
    }
    public static List<String> searchCookie(String sql){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<String> INFO = new LinkedList<String>();
        try {
            conn = ConnectH2.connectH2();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()){
               INFO.add(rs.getString("COOKIE")) ;
               INFO.add(rs.getString("DATE"));
            }
           //释放资源
           stmt.close();
           //关闭连接
           conn.close();
        }catch (SQLException e){
            logger.info("查询信息失败！");
        }catch (ClassNotFoundException e){
            logger.info("未找到类！");
        }
        return INFO;
    }

    public static ResultSet searchS(String sql){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<String> INFO = new LinkedList<String>();
        try {
            conn = ConnectH2.connectH2();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        }catch (SQLException e){
            logger.info("查询信息失败！");
        }catch (ClassNotFoundException e){
            logger.info("未找到类！");
        }
        return rs;
    }

    //更新数据库中的cookie
    public static Boolean update(String Cookie,String type){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String date = getDatetime();
        try {
            conn = ConnectH2.connectH2();
            String sql="UPDATE COOKIES SET COOKIE=?,DATE=? WHERE TYPE=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,Cookie);//为sql语句中第一个问号赋值
            stmt.setString(2,date);//为sql语句中第二个问号赋值
            stmt.setString(3,type);//为sql语句第三个问号赋值
            stmt.execute();
            stmt.close();
            conn.close();
        }catch (SQLException e){
            logger.info("SQL语句错误！");
            return false;
        }catch (ClassNotFoundException e){
            logger.info("未找到类！");
            return false;
        }
        return  true;
    }

    public static void H2insert(String db_name,String company,String table_name,String table){
        Connection conn = null;
        PreparedStatement stmt = null;
//        String tst = "tst";
        try {
            conn = ConnectH2.connectH2();
            String sql="insert into ENV_TST (env,db_name,company,table_name)values(?,?,?,?)";
            logger.info(sql);
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,table_name);//为sql语句中第一个问号赋值
            stmt.setString(2,db_name);//为sql语句中第二个问号赋值
            stmt.setString(3,company);//为sql语句第三个问号赋值
            stmt.setString(4,table);//为sql语句第四个问号赋值
            stmt.execute();
            stmt.close();
            conn.close();
        }catch (SQLException e){
            logger.info("SQL语句错误！");
        }catch (ClassNotFoundException e){
            logger.info("未找到类！");
        }
    }

    /**
     * 接收一个环境的字段，查询相应的表返回
     * @param env
     * @return
     */
    public static ResultSet getDBName(String env){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectH2.connectH2();
            String sql=" SELECT NAME,COMPANY FROM DBNAME WHERE TYPE = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,env);//为sql语句中第一个问号赋值
            logger.info(sql+ ">>"+env);
            rs = stmt.executeQuery();
        }catch (SQLException e){
            logger.info("SQL语句错误！");
        }catch (ClassNotFoundException e){
            logger.info("未找到类！");
        }
        return rs;
    }

    /**
     * 数据库栏的搜索功能
     * @param like
     * @param envflag
     * @return
     */
    public static ResultSet searchLike(String like,String envflag){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String S ="'"+like+"%'";
        String type = "'"+envflag+"'";
        try {
            conn = ConnectH2.connectH2();
            String sql=" SELECT NAME,COMPANY FROM DBNAME WHERE NAME like "+S + "AND TYPE ="+type;
            logger.info(sql);
            stmt = conn.prepareStatement(sql);
//            stmt.setString(1,"'%"+like+"'");//为sql语句中第一个问号赋值
            rs = stmt.executeQuery();
        }catch (SQLException e){
            logger.info("SQL语句错误！");
        }catch (ClassNotFoundException e){
            logger.info("未找到类！");
        }
        return rs;
    }

    /**
     * 传入数据库名，获取相应数据表
     * @param dbname
     * @return
     */
    public static ResultSet searchTB(String env,String dbname,String tbname){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String en ="'"+env+"'";
        String tb = "'"+tbname+"%'";
        String db = "'"+dbname +"'";
        try {
            conn = ConnectH2.connectH2();
            String sql=" SELECT table_name FROM env_tst WHERE TABLE_NAME like"+tb+"AND env ="+en+" AND db_name ="+db;
            logger.info(sql);
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
        }catch (SQLException e){
            logger.info("SQL语句错误！");
        }catch (ClassNotFoundException e){
            logger.info("未找到类！");
        }
        return rs;
    }
    public static ResultSet getTBName(String env,String dbname){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectH2.connectH2();
            String sql=" SELECT TABLE_NAME FROM ENV_TST WHERE ENV = ? AND DB_NAME = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,env);//为sql语句中第一个问号赋值
            stmt.setString(2,dbname);
            logger.info(sql+">>"+env+","+dbname);
            rs = stmt.executeQuery();
        }catch (SQLException e){
            logger.info("SQL语句错误！");
        }catch (ClassNotFoundException e){
            logger.info("未找到类！");
        }
        return rs;
    }

    public static ResultSet table(){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectH2.connectH2();
            String sql=" SELECT * FROM DBNAME ";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
        }catch (SQLException e){
            logger.info("SQL语句错误！");
        }catch (ClassNotFoundException e){
            logger.info("未找到类！");
        }
        return rs;
    }
    public static void main(String []args) throws SQLException{
        String sql = "SELECT * FROM TYPE ";
//        ResultSet  rs=  search(sql);
//        while(rs.next())
////            System.out.println(rs.getString("id") + "," + rs.getString("key")+ "," + rs.getString("value"));
//        rs.close();

    }
}
