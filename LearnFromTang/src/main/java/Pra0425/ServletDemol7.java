package Pra0425;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//演示服务器给浏览器写回cookie
@WebServlet("/Demol7")
public class ServletDemol7 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1. 先构造 Cookie对象,每个Cookie对象就是一个键值对
        Cookie userName=new Cookie("userName","wj");
        Cookie age=new Cookie("age",18+"");
        //2. 把Cookie放到响应中
        resp.addCookie(userName);
        resp.addCookie(age);
        //3. 创建一个响应报文
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().write("返回 Cookie 成功");
    }
}
