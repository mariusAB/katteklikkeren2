package util.things;

import util.Room;

public class Item extends Thing{
    private String item;
    public Item(int x, int y, int hitBox, String item, String path, Room r) {
        super(x, y, hitBox, path, r);
        this.item = item;
    }

    public void interact() {
        if (item.equals("HealthPotion")) {
            super.currRoom.getMain().heal(25);
            super.currRoom.removeItem(this);
        }
    }
}
