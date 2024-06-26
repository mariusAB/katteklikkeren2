package weapons;

import util.Room;
import util.things.SwordSwipe;

public class StarterSword extends Weapon{
    private int dmg = 10;
    private int delay = 30;
    private int lastFired = 0;
    private int upTime = 5;
    private int size = 50;
    private int length = 1100;
    public StarterSword(Room r) {
        super(r);
        super.path = "img/starterSword.png";
        super.path1 = "img/starterSwordCatL.png";
        super.path2 = "img/starterSwordCatR.png";
    }

    public void use(int xfrom, int yfrom, int xto, int yto) {
        if (r.getRoomTick() - lastFired > delay){
            SwordSwipe s = new SwordSwipe(xfrom, yfrom, xto, yto, size, length, dmg, upTime, "img/starterSwordSwipe.png", r);
            r.addSwordSwipe(s);
            lastFired = r.getRoomTick();
        }
    }

    public void reset() {
        lastFired = 0;
    }
}
