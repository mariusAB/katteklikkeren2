package weapons;

import things.Projectile;
import util.Room;

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
        super.path = "src/resources/img/fireStaff.png";
        super.path1 = "src/resources/img/fireStaffCatL.png";
        super.path2 = "src/resources/img/fireStaffCatR.png";
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
        Projectile p = new Projectile(xfrom, yfrom, newXto, newYto, speed, hitBox, dmg, true, "src/resources/img/fireBall.png", r);
        r.addThing(p);
        lastFired = r.getRoomTick();
        }
    }

    public void reset() {
        lastFired = 0;
    }
}