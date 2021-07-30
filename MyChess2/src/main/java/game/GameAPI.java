package game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

//借助这个类,使用websocket和前端进行交互
@ServerEndpoint("/game/{userId}")
public class GameAPI {
    private int  userId;
    private Gson gson=new GsonBuilder().create();

    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session){
        this.userId=Integer.parseInt(userId);
        System.out.println("建立连接! userId: "+this.userId);
        OnlineUserManager.getInstance().online(this.userId,session);
    }

    @OnClose
    public void onClose(){
        System.out.println("连接关闭! userId: "+this.userId);
        OnlineUserManager.getInstance().offline(this.userId);
    }

    @OnError
    public void onError(Session session,Throwable error){
        System.out.println("连接异常! userId: "+this.userId);
        error.printStackTrace();
        OnlineUserManager.getInstance().offline(this.userId);
    }

    @OnMessage
    public void onMessage(String message,Session session) throws InterruptedException, IOException {
        System.out.println("收到消息! message: "+message);
        //把message按照json格式,转换成对象
        Request request=gson.fromJson(message,Request.class);
        if(request.getType().equals("startMatch")){
            //当前的请求为匹配
            Matcher.getInstance().addMatchQueue(request);

        }else if(request.getType().equals("putChess")){
            //当前的请求为落子
            //1.获取到当前的房间id,进一步找到房间对象
            Room room=RoomManager.getInstance().getRoom(request.getRoomId());
            //2.具体的落子操作
            room.putChess(request);

        }else{
            System.out.println("当前的请求类型错误! type: "+request.getType());
        }
    }
}
