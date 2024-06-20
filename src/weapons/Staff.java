package weapons;
import util.*;

public class Staff extends Weapon{
    private Room r;
    private int speed = 20;
    public Staff(Room r) {
        super(r);
        this.r = r;
    }
	public void use(int x, int y, int vx, int vy) {
        double s = Math.sqrt(vx*vx + vy*vy);
        vx = (int)(vx*speed/s);
        vy = (int)(vy*speed/s);
        Thing p = new Thing(x,y,vx,vy, true, r);
        p.setPath("img/magicProjectile.png");
        r.addThing(p);
    }
}