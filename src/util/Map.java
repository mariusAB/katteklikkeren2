package util;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private Logic l;
    private Room currRoom;
    private Room firstRoom;
    private Room secondRoom;
    public Map(Logic l) {
        this.l = l;
        firstRoom = new Room(l);
        currRoom = firstRoom;
        secondRoom = new Room(l);
    }

    public Room getCurrRoom() {
        return currRoom;
    }

    public List<Boolean> getDoors() {
        List<Boolean> doors = new ArrayList<Boolean>();
        doors.add(true);
        doors.add(true);
        doors.add(true);
        doors.add(true);
        return doors;
    }

    public void move(int dir) {
        if (currRoom.equals(firstRoom)) {
            currRoom = secondRoom;
            l.setRoom(currRoom);
        } else {
            currRoom = firstRoom;
            l.setRoom(currRoom);
        }
    }
}
