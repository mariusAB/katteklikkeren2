package util;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private Logic l;
    public Map(Logic l) {
        this.l = l;
    }

    public Room getCurrRoom() {
        return new Room(l);
    }

    public List<Boolean> getDoors() {
        List<Boolean> doors = new ArrayList<Boolean>();
        doors.add(true);
        doors.add(false);
        doors.add(false);
        doors.add(true);
        return doors;
    }

    public void move(int dir) {
        l.setRoom(new Room(l));
    }
}
