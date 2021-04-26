package org.example.util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBUtil {

    private static final DataSource DS=new MysqlDataSource();

    static{
        ((MysqlDataSource)DS).setURL("jdbc:mysql://localhost:3306/servlet_blog");
        ((MysqlDataSource)DS).setUser("Anapple");
        ((MysqlDataSource)DS).setPassword("wangjia");
        ((MysqlDataSource)DS).setUseSSL(false);
    }

    public static Connection getConnection() throws SQLException {
        return DS.getConnection();
    }

    public static void main(String[] args) throws SQLException {
        System.out.println(getConnection());
    }
}
