package game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

//通过这个类，借助websocket和前端进行交互
@ServerEndpoint("/game/{userId}")
public class GameAPI {
    private int userId;
    private Gson gson=new GsonBuilder().create();
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) throws IOException {
        this.userId=Integer.parseInt(userId);
        System.out.println("连接建立! userId: "+this.userId);
        if(OnlineUserManager.getInstance().getSession(this.userId)==null){
            //当前用户未登录,将用户插入到用户管理器中
            OnlineUserManager.getInstance().onLine(this.userId,session);
        }else{
            //用户重复登录,提示用户重复登录
            session.getBasicRemote().sendText("duplicationLogin!");
        }
    }
    @OnClose
    public void onClose(){
        System.out.println("连接关闭! userId: "+this.userId);
        OnlineUserManager.getInstance().offLine(this.userId);
    }
    @OnError
    public void onError(Session session,Throwable error){
        System.out.println("连接异常！ userId: "+this.userId);
        error.printStackTrace();
        OnlineUserManager.getInstance().offLine(this.userId);
    }
    @OnMessage
    public void onMessage(String message,Session session) throws InterruptedException, IOException {
        System.out.println("收到消息！ message: "+message);
        //把message按照JSON字符串格式转化为对象
        Request request=gson.fromJson(message,Request.class);
        if(request.getType().equals("startMatch")){
            //当前就要执行匹配的逻辑
            Matcher.getInstance().addMatchQueue(request);
        }else if(request.getType().equals("putChess")){
            //当前就要执行落子的逻辑
            //1. 现根据请求对象,获取到当前的房间对象.
            Room room=RoomManager.getInstance().getRoom(request.getRoomId());
            //2. 落子
            room.putChess(request);

        }else{
            System.out.println("当前的请求类型错区 type: " +request.getType());
        }
    }
}
