package Pra0425;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

//返回一个页面,每秒钟自动刷新一次
@WebServlet("/Demol4")
public class ServletDemol4 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setIntHeader("Refresh",1);
        //返回的页面中填写上当前时间
        //获取当前时间
//        System.currentTimeMillis();
        Date date=new Date();
        Writer writer=resp.getWriter();
        writer.write("<html>");
        writer.write(date.toString());
        writer.write("</html>");
    }

}
