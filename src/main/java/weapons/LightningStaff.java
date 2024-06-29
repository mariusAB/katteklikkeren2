package weapons;

import things.LightningStrike;
import things.SwordSwipe;
import util.Room;

public class LightningStaff extends Weapon{
    private int dmg = 15;
    private int delay = 30;
    private int lastFired = 0;
    private int upTime = 5;
    private int size = 100;
    public LightningStaff(int dmg, int delay, Room r) {
        super(r);
        this.dmg = dmg;
        this.delay = delay;
        super.path = "src/resources/img/lightningStaff.png";
        super.path1 = "src/resources/img/lightningStaffCatL.png";
        super.path2 = "src/resources/img/lightningStaffCatR.png";
    }

    public void use(int xfrom, int yfrom, int xto, int yto) {
        if (r.getRoomTick() - lastFired > delay){
            LightningStrike s = new LightningStrike(xfrom, yfrom, xto, yto, size, dmg, upTime, "src/resources/img/lightning.png", r);
            r.addLightningStrike(s);
            lastFired = r.getRoomTick();
        }
    }

    public void reset() {
        lastFired = 0;
    }
}