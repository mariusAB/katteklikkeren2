package util;

public class Item extends Thing{
    private String item;
    private int hitBox;
    Item(int x, int y, int hitBox, String item, Room r) {
        super(x, y, hitBox, r);
        this.item = item;
        this.hitBox = hitBox;
    }

    public int getHitBox() {
        return hitBox;
    }

    public void interact() {
        if (item.equals("HealthPotion")) {
            super.currRoom.getMain().heal(25);
            super.currRoom.removeItem(this);
        }
    }
}
