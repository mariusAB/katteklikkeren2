package util.weapons;
import util.*;
import util.things.Projectile;

public class FireBallStaff extends Weapon{
    private int speed = 70;
    private int hitBox = 80;
    private int dmg = 25;
    private int delay = 50;
    private int lastFired = 0;
    private double spread = 10;
    public FireBallStaff(int delay, int dmg, int speed, Room r) {
        super(r);
        this.delay = delay;
        this.dmg = dmg;
        this.speed = speed;
        super.path = "src/resources/img/sword.png";
        super.path1 = "src/resources/img/swordCatL.png";
        super.path2 = "src/resources/img/swordCatR.png";
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
        Projectile p = new Projectile(xfrom, yfrom, newXto, newYto, speed, hitBox, dmg, true, "src/resources/img/magicProjectile.png", r);
        r.addThing(p);
        lastFired = r.getRoomTick();
        }
    }

    public void reset() {
        lastFired = 0;
    }
}