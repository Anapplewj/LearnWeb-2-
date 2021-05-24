package game;

import java.util.concurrent.ConcurrentHashMap;

//通过这个类来管理多个房间对象
public class RoomManager {
    //仍需要一个hashMap来保存房间的信息
    //key: roomId, value: 房间本身
    private ConcurrentHashMap<String,Room> rooms=new ConcurrentHashMap<>();

    //把房间放到房间管理器中
    public void addRoom(Room room){
        rooms.put(room.getRoomId(),room);
    }
    //把房间从房间管理器中移除
    public void removeRoom(String roomId){
        rooms.remove(roomId);
    }
    //根据房间id查找对应的房间
    public Room getRoom(String roomId){
        return rooms.get(roomId);
    }

    private RoomManager(){}
    private static RoomManager roomManager=new RoomManager();
    public static RoomManager getInstance(){
        return roomManager;
    }
}
