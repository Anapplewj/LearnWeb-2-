package api;

import dao.UserDao;
import model.User;
import view.HtmlGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        //1.获取用户名和密码,并进行简单校验
        String name=req.getParameter("name");
        String password=req.getParameter("password");
        if(name==null|| "".equals(name)
                ||password==null|| "".equals(password)){
            String html= HtmlGenerator.getMessagePage("用户名或密码为空",
                        "login.html");
            resp.getWriter().write(html);
            return;
        }
        //2.数据库中查找,看用户是否存在
        //3.对比密码是否存在
        UserDao userDao=new UserDao();
        User user=userDao.selectByName(name);
        if(user==null||!password.equals(user.getPassword())){
            String html=HtmlGenerator.getMessagePage("用户名或密码错误",
                    "login.html");
            resp.getWriter().write(html);
            return;
        }
        //4.匹配成功则认为登录成功,创建一个Session
        HttpSession httpSession=req.getSession(true);
        httpSession.setAttribute("user",user);
        //5.返回一个登录成功的提示页面
        String html=HtmlGenerator.getMessagePage("登录成功!",
                "article");
        resp.getWriter().write(html);
    }

}
