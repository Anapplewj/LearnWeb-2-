package Pra0426;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
@WebServlet("/uploadServlet")
@MultipartConfig//上传图片必须得加这个注解
public class ServletDemol10 extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       //收到图片,直接把图片保存到
       //一个指定的路径中
       //先把路径指定好
       String basePath="d:/biter/Web/images/";
       //类似于前面的getParameter
        //参数里传入一个key值,就得到一个想要获取的内容
        //此处得到的是Part对象,对应到上传的文件内容
       Part image=req.getPart("image");
       //这个方法就可以得到上传的文件名
       String path=basePath+image.getSubmittedFileName();
       image.write(path);

       resp.setContentType("text/html;charset=utf-8");
       resp.getWriter().write("图片上传成功");
    }
}
