package model;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//用于管理数据库连接
//1)建立连接
//2)断开连接
//JDBC中使用 DataSource 来管理连接
//DBUtil 相当于是对DataSource再稍微包装一下
//DataSource每个应用程序只应该有一个实例(单例)
//DBUtil本质上就是实现了一个单例模式,管理了一个唯一的DataSource
//单例模式的实现有两种风格: 饿汉模式,懒汉模式
//下面使用懒汉模式
public class DBUtil {
    private static volatile DataSource dataSource=null;
    private static final String URL="jdbc:mysql://127.0.0.1:3306/myblog?characterEncoding=utf-8&useSSL=true";
    private static final String USERNAME="root";
    private static final String PASSWORD="wangjia";

    public static DataSource getDataSource(){
        if (dataSource==null) {
            synchronized (DBUtil.class) {
                if(dataSource==null){
                    dataSource=new MysqlDataSource();
                    //还需要给dataSource设置一些属性
                   ((MysqlDataSource)dataSource).setURL(URL);
                   ((MysqlDataSource)dataSource).setUser(USERNAME);
                   ((MysqlDataSource)dataSource).setPassword(PASSWORD);
                }
            }
        }
        return dataSource;
    }

    //通过这个方法获取连接
    public static Connection getConnection(){
        try {
            return getDataSource().getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    //通过这个方法断开连接
    public static void close(Connection connection, PreparedStatement statement,
                             ResultSet resultSet){
        try {
            if(resultSet!=null) {
                resultSet.close();
            }
            if(statement!=null){
                statement.close();
            }
            if(connection!=null){
                connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
