package view;

import model.Article;
import model.User;

import java.util.List;

public class HtmlGenerator {
    //通过字符串拼接的形式,构造出一个html格式的内容来
    //下面的代码实现起来,非常麻烦,(Html嵌入到 Java代码中)
    //如果页面简单还好,如果页面复杂,那就非常不好整了.
    //课堂上使用这种写法,代码简单粗暴(不需要引入新的知识点)
    //实际开发中还有更科学的写法:
    //1.服务器渲染页面,把 业务代码嵌入到HTML(JSP,PHP);(很少被使用了)
    //2.服务器渲染页面,把HTML嵌入到业务代码中,不使用字符串拼接的方式
    //而是使用模板的方式,(FreeMarker)
    //3. 前端渲染页面,通过前后端分离的方式,服务器只是返回简单的数据,由前端
    //代码通过JS构造页面内容[主流]
    public static String getMessagePage(String message, String nextUrl) {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("<html>");
        stringBuilder.append("<head>");
        stringBuilder.append("<meta charset=\"utf-8\">");
        stringBuilder.append("<title>页面提示</title>");
        stringBuilder.append("</head>");
        stringBuilder.append("<body>");

        stringBuilder.append("<h3>");
        stringBuilder.append(message);
        stringBuilder.append("</h3>");

        stringBuilder.append(String.format("<a href=\"%s\"> 点击这里进行跳转</a>",
                nextUrl));

        stringBuilder.append("</body>");
        stringBuilder.append("</html>");

        return stringBuilder.toString();
    }
    //按照字符串拼装的方式,生成html
    public static String getArticleListPage(List<Article> articles, User user) {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("<html>");
        stringBuilder.append("<head>");
        stringBuilder.append("<meta charset=\"utf-8\">");
        stringBuilder.append("<title>页面提示</title>");
        stringBuilder.append("</head>");
        stringBuilder.append("<body>");

        stringBuilder.append("<div> 欢迎您!"+user.getName()+"</div>");
        //要有一个文章列表,显示每个文章的标题.
        for(Article article:articles){
            stringBuilder.append(String.format("<div> <a href=\"article?articleId=%d\"> %s </a> </div>",
                    article.getArticleId(),article.getTitle()));
        }
        stringBuilder.append(String.format("<div>当前共有%d篇博客</div>",
                articles.size()));

        stringBuilder.append("</body>");
        stringBuilder.append("</html>");

        return stringBuilder.toString();
    }

    public static String getArticleDetailPage(Article article, User user) {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("<html>");
        stringBuilder.append("<head>");
        stringBuilder.append("<meta charset=\"utf-8\">");
        stringBuilder.append("<title>页面提示</title>");
        stringBuilder.append("</head>");
        stringBuilder.append("<body>");

        stringBuilder.append("<h3>欢迎您!"+user.getName()+"</h3>");
        stringBuilder.append("<hr>");

        stringBuilder.append(String.format("<h1>%s</h1>",article.getTitle()));
        stringBuilder.append(String.format("<h4>%s</h4>",article.getUserId()));
        stringBuilder.append(String.format("<div>%s</div>",article.getContent()));


        stringBuilder.append("</body>");
        stringBuilder.append("</html>");
        return stringBuilder.toString();

    }
}
