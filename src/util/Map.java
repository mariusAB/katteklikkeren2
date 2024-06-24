package util;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private Logic logic;
    private RoomContainer currRoom;
    private List<RoomContainer> rooms;
    private int width = 5;
    private int height = 5;

    public Map(Logic logic, int width, int height) {
        this.logic = logic;
        this.width = width;
        this.height = height;
        initRooms();
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

    public Room getCurrRoom() {
        return currRoom.getRoom();
    }

    public List<Boolean> getDoors() {
        List<Boolean> doors = new ArrayList<Boolean>();

        return doors;
    }

    public void move(int dir) {
        
    }
}
