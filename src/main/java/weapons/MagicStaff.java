package weapons;

import things.Projectile;
import util.Room;

public class MagicStaff extends Weapon{
    private int speed = 70;
    private int hitBox = 80;
    private int dmg = 10;
    private int delay = 20;
    private int lastFired = 0;
    private double spread = 20;
    public MagicStaff(int delay, int dmg, int speed, Room r) {
        super(r);
        this.delay = delay;
        this.dmg = dmg;
        this.speed = speed;
        super.path = "src/resources/img/magicWand.png";
        super.path1 = "src/resources/img/magicStaffCatL.png";
        super.path2 = "src/resources/img/magicStaffCatR.png";
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
        Projectile p = new Projectile(xfrom, yfrom, newXto, newYto, speed, hitBox, dmg, true, false, "src/resources/img/magicProjectile.png", r);
        r.addThing(p);
        lastFired = r.getRoomTick();
        }
    }

    public void reset() {
        lastFired = 0;
    }
}