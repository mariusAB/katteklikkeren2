package weapons;

import util.Room;
import util.things.SwordSwipe;

public class StarterSword extends Weapon{
    private int dmg = 10;
    private int delay = 20;
    private int lastFired = 0;
    private int upTime = 5;
    private int size = 50;
    private int length = 1100;
    public StarterSword(Room r) {
        super(r);
        super.path = "img/sword.png";
        super.path1 = "img/swordCatL.png";
        super.path2 = "img/swordCatR.png";
    }

    public void use(int xfrom, int yfrom, int xto, int yto) {
        if (r.getRoomTick() - lastFired > delay){
            SwordSwipe s = new SwordSwipe(xfrom, yfrom, xto, yto, size, length, dmg, upTime, "img/swordSwipe.png", r);
            r.addSwordSwipe(s);
            lastFired = r.getRoomTick();
        }
    }

    public void reset() {
        lastFired = 0;
    }
}
