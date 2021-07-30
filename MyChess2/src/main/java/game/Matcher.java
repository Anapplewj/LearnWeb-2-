package game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Matcher {
    //这个队列中要存储待匹配的请求
    private BlockingDeque<Request> matcherQueue=new LinkedBlockingDeque<>();
    private Gson gson=new GsonBuilder().create();
    public void addMatchQueue(Request request) throws InterruptedException {
        matcherQueue.put(request);
    }

    private void handlerMatch() throws InterruptedException, IOException {
        //1.先尝试从队列中取出两个匹配请求
        Request player1=matcherQueue.take();
        Request player2=matcherQueue.take();
        System.out.println("当前匹配到两个玩家: "+player1.getUserId()+","+player2.getUserId());
        //2.
        Session session1=OnlineUserManager.getInstance().getSession(player1.getUserId());
        Session session2=OnlineUserManager.getInstance().getSession(player2.getUserId());
        if(session1==null){
            matcherQueue.put(player2);
            return;
        }else if(session2==null){
            matcherQueue.put(player1);
            return;
        }
        //3.创建一个房间,把玩家放进去,再把房间放到房间管理器中.
        Room room=new Room();
        room.setUserId1(player1.getUserId());
        room.setUserId2(player2.getUserId());
        RoomManager.getInstance().addRoom(room);

        //4.给两个玩家反馈匹配成功的消息
        MatcherResponse response1=new MatcherResponse();
        //当前的房间ID先写死
        response1.setRoomId(room.getRoomId());
        response1.setWhite(true);
        response1.setOtherUserId(player2.getUserId());
        session1.getBasicRemote().sendText(gson.toJson(response1));
        MatcherResponse response2=new MatcherResponse();
        response2.setRoomId(room.getRoomId());
        response2.setWhite(false);
        response2.setOtherUserId(player1.getUserId());
        session2.getBasicRemote().sendText(gson.toJson(response2));
    }

    private Matcher(){
        Thread t=new Thread(){
            @Override
            public void run() {
                //线程的入口方法
                //这个扫描的线程是自始至终的,只要服务器开着,就不断的进行
                while (true){
                    try {
                        handlerMatch();
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        //真正的启动线程
        t.start();
    }
    private static Matcher matcher=new Matcher();
    public static Matcher getInstance(){
        return matcher;
    }
}
