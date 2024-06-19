package weapons;
import util.*;

public class Staff extends Weapon{
    Logic l;
    public Staff(Logic l) {
        super(l);
        this.l = l;
    }
	public void use(int x, int y, int vx, int vy, boolean homing) {
        Projectile p = new Projectile(x,y,vx,vy,false,15, l);
        p.setX(x);
        p.setY(y);
        p.setPath("img/magicProjectile.png");
        l.addProjectile(p);
        // TODO: timer
    }
}
