package game;

import javax.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;

//通过这个类管理用户的在线状态
public class OnlineUserManager {
    private ConcurrentHashMap<Integer, Session> users=new ConcurrentHashMap<>();

    //用户上线
    public void online(int userId,Session session){
        users.put(userId,session);
    }

    //用户下线
    public void offline(int userId){
        users.remove(userId);
    }

    //根据用户id,获取到用户的Session对象
    public Session getSession(int userId){
        return users.get(userId);
    }

    //一旦构造方法变成私有的,就意味着,无法通过这个类外部再new出实例
    private OnlineUserManager(){}
    private static OnlineUserManager onlineUserManager=new OnlineUserManager();

    public static OnlineUserManager getInstance(){
        return onlineUserManager;
    }
}
