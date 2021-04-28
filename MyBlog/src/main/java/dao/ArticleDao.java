package dao;

import model.Article;
import model.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticleDao {
    // 1. 新增文章(发布博客)
    public void add(Article article) {
        // 1. 获取数据库连接
        Connection connection = DBUtil.getConnection();
        // 2. 构造 SQL
        String sql = "insert into article values (null, ?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, article.getTitle());
            statement.setString(2, article.getContent());
            statement.setInt(3, article.getUserId());
            // 3. 执行 SQL
            int ret = statement.executeUpdate();
            if (ret != 1) {
                System.out.println("执行插入文章操作失败");
                return;
            }
            System.out.println("执行插入文章操作成功");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 4. 释放连接.
            DBUtil.close(connection, statement, null);
        }
    }
    //2.查看文章列表(把所有文章信息都查出来)
    public List<Article> selectAll(){
        List<Article> articles=new ArrayList<>();
        //1.建立连接
        Connection connection=DBUtil.getConnection();
        //2.拼装 sql
        String sql="select articleId,title,userId from article";
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        try {
            statement=connection.prepareStatement(sql);
            //3.执行 sql
            resultSet=statement.executeQuery();
            //4.遍历结果集
            while(resultSet.next()){
                //针对每个结果集中,都构造一个对应的Article对象
                //此时由于没有从数据库中读content字段,这个字段暂时先不设置
                Article article=new Article();
                article.setArticleId(resultSet.getInt("articleId"));
                article.setTitle(resultSet.getString("title"));
                article.setUserId(resultSet.getInt("userId"));
                articles.add(article);
            }
            return articles;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally{//5.释放连接
            DBUtil.close(connection,statement,resultSet);
        }
        return null;
    }
    //3.查看指定文章详情
    public Article selectById(int articleId){
        //1.建立数据库连接
        Connection connection=DBUtil.getConnection();
        //2.构造sql
        String sql="select* from article where articleId=?";
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        try {
            statement=connection.prepareStatement(sql);
            statement.setInt(1,articleId);
            //3.执行sql
            resultSet=statement.executeQuery();
            //4.遍历结果集
            if(resultSet.next()){
                Article article=new Article();
                article.setArticleId(resultSet.getInt("articleId"));
                article.setTitle(resultSet.getString("title"));
                article.setContent(resultSet.getString("content"));
                article.setUserId(resultSet.getInt("userId"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally{//5.释放连接
            DBUtil.close(connection,statement,resultSet);
        }
        return null;
    }
    //4.删除指定文章(给定文章id来删除)
    public void delete(int articleId){
        //1.获取连接
        Connection connection=DBUtil.getConnection();
        //2.拼装sql
        String sql="delete from article where articleId=?";
        PreparedStatement statement=null;
        try {
            statement=connection.prepareStatement(sql);
            statement.setInt(1,articleId);
            //3.执行sql
            int ret=statement.executeUpdate();
            if(ret!=1){
                System.out.println("删除文章失败!");
                return;
            }
            System.out.println("删除文章成功!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally{//4.释放连接
            DBUtil.close(connection,statement,null);
        }
    }

    public static void main(String[] args) {
        ArticleDao articleDao=new ArticleDao();
        Article article=new Article();
        article.setTitle("i am biaoti");
        article.setContent("absbcdhfeudhdsidefbjsjaiosfhb............");
        article.setUserId(1);
        articleDao.add(article);
    }
}

