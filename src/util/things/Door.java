package util.things;

import java.util.List;

import util.Room;

public class Door extends Thing{
    private boolean open = false;
    private int x0;
    private int y0;
    private int x1;
    private int y1;
    private int dir;
    public Door(int x0, int y0, int x1, int y1, int dir, Room r) {
        super(0, 0, 0, "Door", r);
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.dir = dir;
    }

    public void open() {
        open = true;
    }

    public void tick() {
        System.out.println(open && currRoom.getMainX() > x0 && currRoom.getMainX() < x1 && currRoom.getMainY() > y0 && currRoom.getMainY() < y1);
        if (open && currRoom.getMainX() < x0 && currRoom.getMainX() > x1 && currRoom.getMainY() > y0 && currRoom.getMainY() < y1) {
            currRoom.getLogic().getMap().move(dir);
        }
    }
}
