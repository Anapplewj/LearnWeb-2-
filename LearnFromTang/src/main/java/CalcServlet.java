import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet("/calc")
public class CalcServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //读取url中的参数内容(a和b的内容)
        //针对a和b进行相加
        String astr=req.getParameter("a");
        String bstr=req.getParameter("b");
        int a=Integer.parseInt(astr);
        int b=Integer.parseInt(bstr);
        int result=a+b;
        resp.getWriter().write(String.format("<h1>result=%d</h1>",result));
    }
}
