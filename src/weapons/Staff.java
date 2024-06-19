package weapons;
import util.*;

public class Staff extends Weapon{
    private Logic l;
    private int speed = 20;
    public Staff(Logic l) {
        super(l);
        this.l = l;
    }
	public void use(int x, int y, int vx, int vy) {
        double s = Math.sqrt(vx*vx + vy*vy);
        vx = (int)(vx*speed/s);
        vy = (int)(vy*speed/s);
        Thing p = new Thing(x,y,vx,vy, true, l);
        p.setPath("img/magicProjectile.png");
        l.addProjectile(p);
    }
}