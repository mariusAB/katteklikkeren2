package weapons;
import util.*;
import util.things.Projectile;

public class FireBallStaff extends Weapon{
    private Room r;
    private int speed = 70;
    private int hitBox = 60;
    private int dmg = 25;
    private int delay = 50;
    private int lastFired = 0;
    private double spread = 10;
    public FireBallStaff(Room r) {
        super(r);
        this.r = r;
        super.path = "img/sword.png";
        super.path1 = "img/swordCatL.png";
        super.path2 = "img/swordCatR.png";
    }
	public void use(int xfrom, int yfrom, int xto, int yto) {
        if (r.getRoomTick() - lastFired > delay){
        double angle = Math.atan2(yto - yfrom, xto - xfrom);
        double spreadRadians = Math.toRadians(spread);
        double randomSpread = (Math.random() * spreadRadians) - (spreadRadians / 2);
        double adjustedAngle = angle + randomSpread;
        int fixedDistance = 100;
        int newXto = xfrom + (int) (Math.cos(adjustedAngle) * fixedDistance);
        int newYto = yfrom + (int) (Math.sin(adjustedAngle) * fixedDistance);
        Projectile p = new Projectile(xfrom, yfrom, newXto, newYto, speed, hitBox, dmg, true, "img/magicProjectile.png", r);
        r.addThing(p);
        lastFired = r.getRoomTick();
        }
    }

    public void reset() {
        lastFired = 0;
    }
}