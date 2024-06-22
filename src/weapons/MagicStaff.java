package weapons;
import util.*;
import util.things.Projectile;

public class MagicStaff extends Weapon{
    private int speed = 10;
    private int hitBox = 10;
    private int dmg = 10;
    private int delay = 20;
    private int lastFired = 0;
    private double spread = 20;
    public MagicStaff(Room r) {
        super(r);
        super.path = "img/magicWand.png";
        super.path1 = "img/magicStaffCatL.png";
        super.path2 = "img/magicStaffCatR.png";
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
}