package weapons;

import things.DamageZone;
import util.Room;

public class Hammer extends Weapon{
    private int dmg = 15;
    private int delay = 30;
    private int lastFired = 0;
    private int upTime = 5;
    private int size = 600;
    private int length = 800;
    public Hammer(int dmg, int delay, Room r) {
        super(r);
        this.dmg = dmg;
        this.delay = delay;
        super.path = "src/resources/img/hammer.png";
        super.path1 = "src/resources/img/hammerCatL.png";
        super.path2 = "src/resources/img/hammerCatR.png";
    }

    public void use(int xfrom, int yfrom, int xto, int yto) {
        if (r.getRoomTick() - lastFired > delay){
            int dx = xto - xfrom;
            int dy = yto - yfrom;
            int vx = (int) (dx / Math.sqrt(dx * dx + dy * dy) * length);
            int vy = (int) (dy / Math.sqrt(dx * dx + dy * dy) * length);
            int x = xfrom + vx;
            int y = yfrom + vy;
            DamageZone s = new DamageZone(x, y, size, dmg, upTime, "src/resources/img/hammerStrike.png", r);
            r.addDamageZone(s);
            lastFired = r.getRoomTick();
        }
    }

    public void reset() {
        lastFired = 0;
    }
}