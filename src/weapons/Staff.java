package weapons;
import util.*;
import util.things.Projectile;

public class Staff extends Weapon{
    private Room r;
    private int speed = 10;
    private int hitBox = 10;
    private int dmg = 10;
    private int delay = 20;
    private int lastFired = 0;
    public Staff(Room r) {
        super(r);
        this.r = r;
    }
	public void use(int xfrom, int yfrom, int xto, int yto) {
        if (r.getRoomTick() - lastFired > delay){
            Projectile p = new Projectile(xfrom, yfrom, xto, yto, speed, hitBox, dmg, true, "img/magicProjectile.png", r);
            r.addProjectile(p);
            lastFired = r.getRoomTick();
        }
    }
}