package game;

import java.util.LinkedList;
import java.util.Queue;

public class Matcher {
    //这个队列中要存储待匹配的请求
    private Queue<Request> matcherQueue=new LinkedList<>();

    public void addMatchQueue(Request request){
        matcherQueue.offer(request);
    }

    private Matcher(){};
    private static Matcher matcher=new Matcher();
    public static Matcher getInstance(){
        return matcher;
    }
}
