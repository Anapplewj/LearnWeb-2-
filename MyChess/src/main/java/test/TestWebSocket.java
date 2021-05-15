package test;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value="/webSocketTest/{userId}")
public class TestWebSocket {
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session){
        System.out.println("建立连接! userId= "+userId);
        //只要建立连接,每隔一秒就给客户端发送一个hello字符串
        Thread t=new Thread(){
            @Override
            public void run() {
                try {
                    session.getBasicRemote().sendText("hello");
                    Thread.sleep(1000);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    @OnClose
    public void onClose(){
        System.out.println("断开连接!");
    }

    @OnError
    public void onError(Session session,Throwable error){
        System.out.println("连接出现异常!");
        error.printStackTrace();
    }
    @OnMessage
    public void onMessage(String message,Session session) throws IOException{
        System.out.println("收到消息!message="+message);
        session.getBasicRemote().sendText(message);
    }
}
