package game;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

//通过这个类来管理用户的在线状态
//为了管理服务器上的所有用户,只需要一份hashmap就行了
//希望OnlineUserManager只存在唯一的一个实例,这就是单例模式
public class OnlineUserManager {
    //此处为了解决线程安全的问题,不能直接使用hashmap,要使用ConcurrentHashMap
    private ConcurrentHashMap<Integer, Session> users=new ConcurrentHashMap<>();

    //用户上线
    public void onLine(int userId,Session session){
        users.put(userId,session);
    }
    //用户下线
    public void offLine(int userId){
        users.remove(userId);
    }

    //根据用户的id获取到用户的session对象
    public Session getSession(int userId){
        return users.get(userId);
    }
    //一旦构造方法变成私有的,就意味着不能在类外部new
    private OnlineUserManager(){

    }
    //如果类外部想要使用这个实例,那么就在类内部创建实例然后返回.
    //此时这个实例,使用static 指向这个引用
    //static 保证了这个new 只会执行一次
    private static OnlineUserManager onlineUserManager=new OnlineUserManager();

    //外面的代码想要使用唯一的onlineUserManager,就使用下面的方法实现
    public static OnlineUserManager getInstance(){
        return onlineUserManager;
    }
}
