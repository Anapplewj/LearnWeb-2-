package game;

import java.util.concurrent.ConcurrentHashMap;

//房间管理器,通过这个类来管理多个房间对象
public class RoomManager {
    //key: roomId
    private ConcurrentHashMap<String,Room> rooms=new ConcurrentHashMap<>();

    //1.把房间放到房间管理器中
    public void addRoom(Room room){
        rooms.put(room.getRoomId(),room);
    }

    //2.把房间从房间管理器中移除
    public void removeRoom(String roomId){
        rooms.remove(roomId);
    }

    //3.根据房间id找到对应的房间对象
    public Room getRoom(String roomId){
        return rooms.get(roomId);
    }

    private RoomManager(){};
    private static RoomManager roomManager=new RoomManager();
    public static RoomManager getInstance(){
        return roomManager;
    }
}
