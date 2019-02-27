package cn.yq.util;

import java.sql.*;

public class ConnectMysql {
    public static void main(String[] args) {
        Connection con;
        String driver = "com.mysql.jdbc.Driver";
        //这里我的数据库是qcl
        String url = "jdbc:mysql://localhost:3306/scrapy";
        String user = "root";
        String password = "";
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            if (!con.isClosed()) {
                System.out.println("数据库连接成功");
            }
            Statement statement = con.createStatement();
            String sql = "select * from scrapy.phone;";//我的表格叫home
            ResultSet resultSet = statement.executeQuery(sql);
            String name;
            String phone;
            while (resultSet.next()) {
                name = resultSet.getString("id");
                phone = resultSet.getString("phone");
                System.out.println("姓名：" + name);
                System.out.println("手机号：" + phone);
            }
            resultSet.close();
            con.close();
        } catch (ClassNotFoundException e) {
            System.out.println("数据库驱动没有安装");

        } catch (SQLException e) {
            System.out.println("数据库连接失败");
        }
    }

}
