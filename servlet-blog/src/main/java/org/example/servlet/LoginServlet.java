package org.example.servlet;

import org.example.dao.UserDAO;
import org.example.model.JSONResponse;
import org.example.model.User;
import org.example.util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        //1.解析请求数据(请求数据类型不同,调用API不同)
        User input= JSONUtil.deserialize(
                req.getInputStream(),User.class);

        //2.处理业务(CRUD)
        User query=null;
        try{
            query = UserDAO.query(input);
        }catch(SQLException e){
            throw new RuntimeException("数据库查询用户失败");
        }

        //3.返回响应的数据(json)
        JSONResponse json=new JSONResponse();
        if(query==null){ //根据输入的账号密码查不到
            json.setCode("LOG001");
            json.setMessage("用户名或密码错误");
        }else{
            json.setSuccess(true);
            //获取session获取不到就创建一个
            HttpSession session=req.getSession();
            //保存用户信息
            session.setAttribute("user",query);
        }
        PrintWriter pw=resp.getWriter();
        pw.println(JSONUtil.serialize(json));
    }
}
