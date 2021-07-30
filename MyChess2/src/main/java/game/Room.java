package game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.Session;
import java.io.IOException;
import java.util.UUID;

//通过这个类来表示一个房间,一个房间就对应到一句对战
public class Room {
    private Gson gson=new GsonBuilder().create();
    private static final int MAX_ROW=15;
    private static final int MAX_COL=15;
    //此处使用UUID来保证每个房间的编号不重复
    private String roomId;
    //两个对局的玩家
    private int userId1;
    private int userId2;

    //棋盘的情况
    int[][] chessBoard=new int[MAX_ROW][MAX_COL];

    public int getUserId1() {
        return userId1;
    }

    public void setUserId1(int userId1) {
        this.userId1 = userId1;
    }

    public String getRoomId() {
        return roomId;
    }

    public int getUserId2() {
        return userId2;
    }

    public void setUserId2(int userId2) {
        this.userId2 = userId2;
    }

    public Room(){
        this.roomId= UUID.randomUUID().toString();
    }

    //预期通过这个方法,来完成具体的落子
    public void putChess(Request request) throws IOException {
        //1.把这个子放到棋盘上,1表示玩家1的棋子,2表示玩家2的棋子
        int chess=request.getUserId()==userId1?userId1:userId2;
        int row=request.getRow();
        int col=request.getCol();
        if(chessBoard[row][col]!=0){
            System.out.println("落子位置有误!"+request);
            return;
        }
        chessBoard[row][col]=chess;
        printChessBoard();
        //2.检查游戏是否结束了
        //返回值为0,表示胜负未分,返回值非零,返回值就表示获胜玩家的id
        int winner=checkWinner(chess,row,col);

        //3.把响应返回给玩家,告诉所有的玩家,当前是谁,把那个颜色的子,落在了哪个位置上
        //a)先构造一个响应对象
        PutChessResponse response=new PutChessResponse();
        response.setUserId(request.getUserId());
        response.setRow(row);
        response.setCol(col);
        response.setWinner(winner);
        //b)给玩家进行响应,根据玩家的id,获取到玩家的session对象
        Session sesison1=OnlineUserManager.getInstance().getSession(userId1);
        Session sesison2=OnlineUserManager.getInstance().getSession(userId2);
        if(sesison1==null&&sesison2==null){
            RoomManager.getInstance().removeRoom(roomId);
            System.out.println("两个玩家都掉线了,RoomId: "+roomId);
            return;
        }
        if(sesison1==null){
            response.setWinner(userId2);
            return;
        }
        if(sesison2==null){
            response.setWinner(userId1);
            return;
        }
        //c)把当前构造好的响应,转化为gson字符串,写回给客户端
        String stringResponse=gson.toJson(response);
        if(sesison1!=null){
            sesison1.getBasicRemote().sendText(stringResponse);
        }
        if(sesison2!=null){
            sesison2.getBasicRemote().sendText(stringResponse);
        }
        //4.如果当前胜负已分,销毁当前的房间
        if(winner!=0){
            //删除房间
            RoomManager.getInstance().removeRoom(roomId);
            System.out.println("游戏结束,房间被销毁! RoomId: "+roomId);
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
        System.out.println("====================");
        for(int i=0;i<MAX_ROW;i++){
            for(int j=0;j<MAX_COL;j++){
                System.out.print(chessBoard[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println("====================");
    }
}
