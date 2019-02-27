package cn.yq.util;

import cn.yq.H2DataBase.H2Manager;
import org.apache.log4j.Logger;

import java.sql.*;

public class mysqlUtil {
    /**
     * 向数据库中做插入操作
     * @param sql
     */
    public static void insert(String sql) {
        Connection conn = BaseConnection.getConnection();
        PreparedStatement ps = null;
        try {
//            ps = conn.prepareStatement(sql);//把写好的sql语句传递到数据库，让数据库知道我们要干什么
//            ps.executeUpdate();//这个方法用于改变数据库数据，a代表改变数据库的条数
            Statement state = conn.createStatement();   //容器
            state.executeUpdate(sql);
//            if (a > 0) {
//                System.out.println("添加成功");
//            } else {
//                System.out.println("添加失败");
//            }
        } catch (SQLException e3) {
            e3.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e2) {

//            e2.printStackTrace();
        }
    }
    public static ResultSet search(String SQL){
        Connection conn = BaseConnection.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(SQL);//把写好的sql语句传递到数据库，让数据库知道我们要干什么
            rs = ps.executeQuery();
        } catch (SQLException e3) {
            e3.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static void main(String []args)throws SQLException{
//        String SQL = "insert into messagefortest(message_type,content,receiver_no,gmt_modified) values('YZM','亲，您的验证码是123456，5分钟内有效。#众安保骉车险#','亲，您的验证码是123456，5分钟内有效。#众安保骉车险#','3123')";
//        insert(SQL);
        String searchSql = "SELECT * FROM env_prd where env ='prd' ";
        ResultSet rs = search(searchSql);
        int i = 0;
        while(rs.next()) {
            System.out.println(rs.getString(3));
            H2Manager.H2insert(rs.getString(3), rs.getString(4), rs.getString(2),rs.getString(5));
        }}
}
