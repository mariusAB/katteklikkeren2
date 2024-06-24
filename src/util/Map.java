package util;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private Logic logic;
    private RoomContainer currRoom ;
    private List<RoomContainer> rooms;
    private int width = 5;
    private int height = 5;

    public Map(Logic logic, int width, int height) {
        this.logic = logic;
        this.width = width;
        this.height = height;
        initRooms();
        currRoom = rooms.get(0); // TODO: Change to random room
        currRoom.setRoom(new Room(logic));
    }

    private void initRooms() {
        rooms = new ArrayList<>(width*height);
        for (int i = 0; i < width*height; i++) {
            rooms.add(new RoomContainer());
        }
    }

    private int getRoomIndex(int x, int y) {
        return x + y*width;
    }

    public int getMapWidth() {
        return width;
    }

    public int getMapHeight() {
        return height;
    }

    public Room getCurrRoom() {
        return currRoom.getRoom();
    }

    public List<Integer> getMiniMap() {
        List<Integer> miniMap = new ArrayList<Integer>();
        for (int i = 0; i < width*height; i++) {
            if (rooms.get(i).hasRoom()) {
                miniMap.add(1);
            } else {
                miniMap.add(0);
            }
        }
        return miniMap;
    
    }

    public List<Boolean> getDoors() {
        List<Boolean> doors = new ArrayList<Boolean>();
        for (int i = 0; i < 4; i++) {
            doors.add(true);
        }
        return doors;
    }

    public void move(int dir) {
        
    }
}
