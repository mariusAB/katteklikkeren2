package weapons;

import util.Room;
import util.things.SwordSwipe;

public class Sword extends Weapon{
    private int dmg = 15;
    private int delay = 30;
    private int lastFired = 0;
    private int upTime = 5;
    private int size = 58;
    private int length = 1430;
    public Sword(int dmg, int delay, Room r) {
        super(r);
        this.dmg = dmg;
        this.delay = delay;
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