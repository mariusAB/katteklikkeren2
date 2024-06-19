package weapons;
import util.*;

public class Staff extends Weapon{
    Logic l;
    public Staff(Logic l) {
        super(l);
        this.l = l;
    }
	public void use(int x, int y, int vx, int vy) {
        double s = Math.sqrt(vx*vx + vy*vy);
        vx = (int)(vx*10/s);
        vy = (int)(vy*10/s);
        Thing p = new Thing(x,y,vx,vy, true, l);
        p.setPath("img/magicProjectile.png");
        l.addProjectile(p);
    }
}