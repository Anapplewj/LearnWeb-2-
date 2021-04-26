package Pra0425;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
@WebServlet("/ServletDemol2")
public class ServletDemol2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //处理表单提交的数据
        String firstName=req.getParameter("firstName");
        String secondName=req.getParameter("secondName");
        //构造响应页面
        resp.setContentType("text/html;charset=utf-8");
        //注意!!一旦调用了getWriter方法,此时header的内容就不能修改了
        Writer writer=resp.getWriter();
        writer.write("<html>");
        writer.write("firstName: "+firstName);
        writer.write("<br>");
        writer.write("secondName: "+secondName);
        writer.write("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
