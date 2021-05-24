package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class Student{
    String name;
    int id;
    public Student(String name, int id) {
        this.name = name;
        this.id = id;
    }
}
public class TestGson {
    public static void main(String[] args) {
        Student student=new Student("ttlxbb",520);
        //"工厂模式",创建一个Gson对象
        //new Gson需要手动设置一些参数,但是使用GsonBuilder
        // 这些参数就已经被设置好了默认值
        //可以直接拿过来用.
        Gson gson=new GsonBuilder().create();
        //将一个对象转化为Gson的字符串
        String stringStudent=gson.toJson(student);
        System.out.println(stringStudent);
        //将一个Gson的字符串转化为一个对象
        Student student2=gson.fromJson(stringStudent,Student.class);
    }
}
