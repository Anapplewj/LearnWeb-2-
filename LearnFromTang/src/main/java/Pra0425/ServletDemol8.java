package Pra0425;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

//通过读取请求,获取到浏览器发来的Cookie内容
@WebServlet("/Demol8")
public class ServletDemol8 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies= req.getCookies();
        resp.setContentType("text/html;charset=utf-8");
        Writer writer=resp.getWriter();
        writer.write("<html>");
        for(Cookie cookie:cookies){
            writer.write(cookie.getName()+":"+cookie.getValue());
            writer.write("<br>");
        }
        writer.write("</html>");
    }
}
