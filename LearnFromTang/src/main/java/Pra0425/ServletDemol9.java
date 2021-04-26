package Pra0425;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;
@WebServlet("/Demol9")
public class ServletDemol9 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1. 先获取Session,如果用户曾经没有访问过,此时就创建新的Session
        //如果用户已经访问过了,就获取到曾经的Session
        // 用户初次访问:
        //这个操作就会自动生成一个SessionId,同时创建一个httpSeesion对象
        //把这个键值对放到内置的hash表中,同时把SessionId写回到浏览器的Cookie中
        // 老用户访问:
        //根据请求中的 Cookie里的SessionId,在hash表中查,找到对应的Session对象
        HttpSession httpSession=req.getSession(true);
        //2. 判断他是不是新用户
        Integer count=1;
        if(httpSession.isNew()){
            //新用户
            //把count值写入到session对象中
            //httpSession也可以当成一个hash表
            httpSession.setAttribute("count",count);
        }else{
            //老用户
            //从httpSession中读取到count值
            count=(Integer)httpSession.getAttribute("count");
            count+=1;
            //count 自增完成之后,要重新写入到 session中
            httpSession.setAttribute("count",count);
        }
        //3. 返回响应页面
        resp.setContentType("text/html;charset=utf-8");
        Writer writer=resp.getWriter();
        writer.write("<html>");
        writer.write("count: "+count);
        writer.write("</html>");
    }
}
