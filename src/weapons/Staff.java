package weapons;
import util.*;

public class Staff extends Weapon{
    private Room r;
    private int speed = 20;
    public Staff(Room r) {
        super(r);
        this.r = r;
    }
	public void use(int xfrom, int yfrom, int xto, int yto) {
        Thing p = new Thing(xfrom, yfrom, xto, yto, speed, true, r);
        p.setPath("img/magicProjectile.png");
        r.addThing(p);
    }
}