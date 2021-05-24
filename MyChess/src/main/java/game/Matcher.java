package game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Matcher {

    //这个队列用来放置待匹配的请求
    //这个队列也不需要多份
    //对于该队列,也要考虑线程安全问题
    private BlockingQueue<Request> matchQueue=new LinkedBlockingQueue<>();
    private Gson gson=new GsonBuilder().create();
    public void addMatchQueue(Request request) throws InterruptedException {
        //offer方法对于 BlockingQueue 没有阻塞功能
        //put方法就有阻塞的功能
        matchQueue.put(request);
    }

    private void handlerMatch() throws InterruptedException, IOException {
        //1. 先尝试从队列中取出两个元素
        //只要队列中存在两个元素,就让其配成一对
        //此处使用take而不是poll目的就是能够阻塞等待
        //如果当前队列没有元素,此处的take就阻塞了
        //就可以避免浪费CPU资源
        Request player1=matchQueue.take();
        Request player2=matchQueue.take();
        System.out.println("匹配到两个玩家: "+player1.getUserId()+","+
                player2.getUserId());
        //2.根据玩家的id,获取到玩家的session对象
        //为了后面的返回数据做铺垫,同时还能检测玩家的在线状态
        Session session1=OnlineUserManager.getInstance().
                getSession(player1.getUserId());
        Session session2=OnlineUserManager.getInstance().
                getSession(player2.getUserId());
        if(session1==null){
            //玩家1掉线,将玩家2重新插入到匹配队列中
            matchQueue.put(player2);
            return;
        }
        if(session2==null){
            //玩家2掉线,将玩家1重新插入到匹配队列中
            matchQueue.put(player1);
            return;
        }
        //3. 如果两个玩家都没掉线,就将两个玩家放到同一个房间中进行对战
        //创建一个房间,将玩家放进去
        Room room=new Room();
        room.setUserId1(player1.getUserId());
        room.setUserId2(player2.getUserId());
        //再把房间放到房间管理器中
        RoomManager.getInstance().addRoom(room);
        //4. 给两个玩家反馈匹配成功的消息
        // 1)给玩家1返回
        MatcherResponse response1=new MatcherResponse();
        response1.setRoomId(room.getRoomId());
        response1.setWhite(true);
        response1.setOtherUserId(player2.getUserId());
        String respString1=gson.toJson(response1);
        session1.getBasicRemote().sendText(respString1);
        // 2)给玩家2返回
        MatcherResponse response2=new MatcherResponse();
        response2.setRoomId(room.getRoomId());
        response2.setWhite(false);
        response2.setOtherUserId(player1.getUserId());
        String respString2=gson.toJson(response2);
        session2.getBasicRemote().sendText(respString2);

    }
    //此处也把这个类实现为单例模式
    private Matcher(){
        //在此处专门创建一个扫描的线程,来扫描队列中是否存在合适的玩家
        Thread t=new Thread(){
            @Override
            public void run() {
                //线程的入口方法
                //这个扫描的方法是自始至终的,只要服务器正在运行
                //就可能有客户端发来匹配请求
                //就需要持续不断的进行处理
                while (true) {
                    //通过这个方法来实现"一次匹配"的过程
                    try {
                        handlerMatch();
                    } catch (InterruptedException|IOException e) {
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
