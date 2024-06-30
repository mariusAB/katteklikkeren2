package things;

import util.*;

public class Door extends Thing{
    private boolean open = false;
    private int x0;
    private int y0;
    private int x1;
    private int y1;
    private int dir;
    private Room r;
    public Door(int x0, int y0, int x1, int y1, int dir, Room r) {
        super(0, 0, 0, "", r);
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.dir = dir;
        this.r = r;
    }

    public void open() {
        open = true;
    }

    public int getx0() {
        return x0;
    }

    public int gety0() {
        return y0;
    }

    public int getx1() {
        return x1;
    }

    public int gety1() {
        return y1;
    }

    public void tick() {
        if (open && currRoom.getMainX() > x0 && currRoom.getMainX() < x1 && currRoom.getMainY() > y0 && currRoom.getMainY() < y1) {
            if (dir == 0) {
                currRoom.getMain().setY(r.getHeight()-currRoom.getMain().getY()-500);
            }
            if (dir == 1) {
                currRoom.getMain().setX(r.getWidth()-currRoom.getMain().getX()+200);
            }
            if (dir == 2) {
                currRoom.getMain().setY(r.getHeight()-currRoom.getMain().getY()-100);
            }
            if (dir == 3) {
                currRoom.getMain().setX(r.getWidth()-currRoom.getMain().getX()-200);
            }
            currRoom.getLogic().getMap().move(dir);
        }
    }
}
