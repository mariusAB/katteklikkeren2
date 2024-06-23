package util.things;

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
        if (open && currRoom.get().getMainX() > x0 && currRoom.get().getMainX() < x1 && currRoom.get().getMainY() > y0 && currRoom.get().getMainY() < y1) {
            if (dir == 0) {
                currRoom.get().getMain().setY(currRoom.get().getDoors().get(2).gety0()-1);
            }
            if (dir == 1) {
                currRoom.get().getMain().setX(currRoom.get().getDoors().get(3).getx0()+1);
            }
            if (dir == 2) {
                currRoom.get().getMain().setY(currRoom.get().getDoors().get(0).gety1()+1);
            }
            if (dir == 3) {
                currRoom.get().getMain().setX(currRoom.get().getDoors().get(1).getx0()-1);
            }
            currRoom.get().getLogic().getMap().move(dir);
        }
    }
}
