package util;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private Logic logic;
    private int currRoom ;
    private List<RoomContainer> rooms;
    private int width = 5;
    private int height = 5;

    public Map(Logic logic, int width, int height) {
        this.logic = logic;
        this.width = width;
        this.height = height;
        initRooms();
        currRoom = width*height/2; // TODO: fiks
        rooms.get(currRoom).setRoom(new Room(logic));
    }

    private void initRooms() {
        rooms = new ArrayList<>(width*height);
        for (int i = 0; i < width*height; i++) {
            rooms.add(new RoomContainer(i, width, height));
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
        return rooms.get(currRoom).getRoom();
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
        if (rooms.get(currRoom).getAbove() != -1) {
            doors.add(true);
        } else {
            doors.add(false);
        }
        if (rooms.get(currRoom).getRight() != -1) {
            doors.add(true);
        } else {
            doors.add(false);
        }
        if (rooms.get(currRoom).getBelow() != -1) {
            doors.add(true);
        } else {
            doors.add(false);
        }
        if (rooms.get(currRoom).getLeft() != -1) {
            doors.add(true);
        } else {
            doors.add(false);
        }
        return doors;
    }

    public void move(int dir) {
        if (dir == 0 && rooms.get(currRoom).getAbove() != -1) {
            currRoom = rooms.get(currRoom).getAbove();
        } else if (dir == 1 && rooms.get(currRoom).getRight() != -1) {
            currRoom = rooms.get(currRoom).getRight();
        } else if (dir == 2 && rooms.get(currRoom).getBelow() != -1) {
            currRoom = rooms.get(currRoom).getBelow();
        } else if (dir == 3 && rooms.get(currRoom).getLeft() != -1) {
            currRoom = rooms.get(currRoom).getLeft();
        }
    }
}
