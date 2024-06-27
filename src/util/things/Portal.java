package util.things;

import util.Room;

public class Portal extends Thing{
    private String pathOpen;
    private Boolean open = false;
    public Portal(int x, int y, int hitBox, String pathClosed, String pathOpen, Room room) {
        super(x, y, hitBox, pathClosed, room);
        this.pathOpen = pathOpen;
    }

    public void open() {
        super.path = pathOpen;
        open = true;
    }

    public void tick() {
        if (open) {
            Main main = currRoom.get().getMain();
            if (main.getX() < x + hitBox && main.getX() > x - hitBox && main.getY() < y + hitBox && main.getY() > y - hitBox) {
                currRoom.get().teleport();
            }
        }
    }
}
