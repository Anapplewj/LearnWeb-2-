package org.example.dao;

import org.example.model.User;
import org.example.util.DBUtil;

import java.sql.*;

public class UserDAO {
    public static User query(User input) throws SQLException {
        //1. 创建数据库连接Connection
        Connection c= DBUtil.getConnection();

        //2. 创建操作命令对象Statement
        String sql="select* from user where username=? and password=?";
        PreparedStatement ps=c.prepareStatement(sql);
        //3. 执行sql语句
        ps.setString(1,input.getUserName());
        ps.setString(2,input.getPassWord());
        ResultSet rs=ps.executeQuery();

        User query=null;
        //4. 如果是查询操作,处理结果集
        while(rs.next()){
            query=new User();
            query.setId(rs.getInt("id"));
            query.setUserName(input.getUserName());
            query.setPassWord(input.getPassWord());
            query.setNickName(input.getNickName());
            query.setSex(rs.getBoolean("sex"));
            Timestamp t=rs.getTimestamp("birthday");
            if(t!=null){
                query.setBirthday(new java.util.Date(t.getTime()));
            }
            query.setHead(input.getHead());
        }
        //5. 释放资源
        ps.close();
        rs.close();
        c.close();
        return null;
    }
}
