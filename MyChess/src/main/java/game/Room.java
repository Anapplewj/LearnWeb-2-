package game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.Session;
import java.io.IOException;
import java.util.UUID;

//通过这个类来表示一个房间,一个房间就对应到一局对战
public class Room {
    private Gson gson=new GsonBuilder().create();
    private static final int MAX_ROW=15;
    private static final int MAX_COL=15;
    //此处我们要保证房间的唯一性
    //每次创建一个房间实例,该房间实例都要和其他房间不重复
    //此处我们使用UUID来作为房间的id,这样就能保证房间的id不重复
    private String roomId;
    //对局的两个玩家
    private int userId1;
    private int userId2;
    //游戏状态,也就是棋盘上的位置
    //约定玩家1的棋子用1表示
    //玩家2的棋子用2表示
    //初始状态下,棋盘里全是0
    private int[][] chessBoard=new int[MAX_ROW][MAX_COL];
    public Room(){
        this.roomId= UUID.randomUUID().toString();
    }

    public int getUserId1() {
        return userId1;
    }

    public void setUserId1(int userId1) {
        this.userId1 = userId1;
    }

    public int getUserId2() {
        return userId2;
    }

    public void setUserId2(int userId2) {
        this.userId2 = userId2;
    }

    public String getRoomId() {
        return roomId;
    }

    //通过这个方法,完成落子的过程
    public void putChess(Request request) throws IOException {
        //1.将子放到棋盘上
        //位置就是request中的row,col
        //放黑子白子,按照request中的用户的userId
        int chess=request.getUserId()==userId1?userId1:userId2;
        int row=request.getRow();
        int col=request.getCol();
        if(chessBoard[row][col]!=0){
            //此处已经落子了
            System.out.println("落子位置有误!"+request);
            return;
        }
        chessBoard[row][col]=chess;
        //打印一下棋盘,方便调试
        printChessBoard();
        //2.检查游戏是否结束,需要检查所有行,所有列,所有的对角线
        //此处的返回值为0:胜负未分;返回非0:就表示获胜玩家的userId
        int winner=checkWinner(chess,row,col);
        //3.把响应返回给玩家.告诉所有的玩家,当前是谁,把某个颜色的子放到棋盘上
        // 以及当前游戏是否分出胜负
        //  a)先构造一个相应对象
        PutChessResponse response=new PutChessResponse();
        response.setUserId(request.getUserId());
        response.setRow(row);
        response.setCol(col);
        response.setWinner(winner);
        //  b)给玩家进行响应,响应的时候,就要根据玩家 id,获取到玩家对象
        Session session1=OnlineUserManager.getInstance().getSession(userId1);
        Session session2=OnlineUserManager.getInstance().getSession(userId2);
        //  c)如果某个玩家掉线了,就给对方相应您获胜了
        if(session1==null&&session2==null){
            //两个玩家都掉线,则销毁房间对象,该方法结束
            //把房间从房间管理器中删除
            RoomManager.getInstance().removeRoom(roomId);
            System.out.println("两个玩家都掉线了! roomId: "+roomId);
            return;
        }
        if(session1==null){
            //玩家1掉线,玩家2获胜
            response.setWinner(userId2);
        }
        if(session2==null){
            //玩家2掉下,玩家1获胜
            response.setWinner(userId1);
        }
        //将响应返回给对应的玩家
        String responseString=gson.toJson(response);
        if (session1 != null) {
            session1.getBasicRemote().sendText(responseString);
        }
        if(session2!=null){
            session2.getBasicRemote().sendText(responseString);
        }
        //4.如果当前胜负已分,就销毁当前的房间
        if(winner!=0){
            //把房间从房间管理器中删除
            RoomManager.getInstance().removeRoom(roomId);
            System.out.println("游戏结束,房间被销毁!"+roomId);
        }

    }

    private int checkWinner(int chess, int row, int col) {
        //done表示当前是否已经分出胜负,如果为true表示已经找到五子连珠的情况
        boolean done=false;
        //1. 检查一行的五种情况,以c的五种情况
        for(int c=col-4;c<=col;c++){
            if(c<0||c+4>=MAX_COL){
                continue;
            }
            if(chessBoard[row][c]==chess
                    &&chessBoard[row][c+1]==chess
                    &&chessBoard[row][c+2]==chess
                    &&chessBoard[row][c+3]==chess
                    &&chessBoard[row][c+4]==chess){
                done=true;
            }
        }
        //2. 检查一列的五种情况
        for(int r=row-4;r<=row;r++){
            if(r<0||r+4>=MAX_ROW){
                continue;
            }
            if(chessBoard[r][col]==chess
                    &&chessBoard[r+1][col]==chess
                    &&chessBoard[r+2][col]==chess
                    &&chessBoard[r+3][col]==chess
                    &&chessBoard[r+4][col]==chess){
                done=true;
            }
        }
        //3. 检查左对角线的五种情况
        for(int r=row-4,c=col-4;r<=row&&c<=col;r++,c++){
            if(r<0||r+4>=MAX_ROW||c<0||c+4>=MAX_COL){
                continue;
            }
            if (chessBoard[r][c]==chess
                    &&chessBoard[r+1][c+1]==chess
                    &&chessBoard[r+2][c+2]==chess
                    &&chessBoard[r+3][c+3]==chess
                    &&chessBoard[r+4][c+4]==chess){
                done=true;
            }
        }
        //4. 检查右对角线的五种情况
        for(int r=row-4,c=col+4;r<=row&&c>=col;r++,c--){
            if(r<0||r+4>=MAX_ROW||c-4<0||c>=MAX_COL){
                continue;
            }
            if(chessBoard[r][c]==chess
                    &&chessBoard[r+1][c-1]==chess
                    &&chessBoard[r+2][c-2]==chess
                    &&chessBoard[r+3][c-3]==chess
                    &&chessBoard[r+4][c-4]==chess){
                done=true;
            }
        }

        if(!done){
            return 0;
        }
        return chess==userId1?userId1:userId2;
    }

    private void printChessBoard() {
        //实现打印棋盘,就可以在服务器这边看到当前棋面上的内容
        System.out.println("==============================");
        for(int r=0;r<MAX_ROW;r++){
            for(int c=0;c<MAX_COL;c++){
                System.out.print(chessBoard[r][c]+" ");
            }
            System.out.println();
        }
        System.out.println("==============================");
    }
}
