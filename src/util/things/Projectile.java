package util.things;

import util.Room;

public class Projectile extends Thing{
    private int damage;
    private Boolean isFriendly;
    private double vx;
    private double vy;
    private double rotation;

    public Projectile(int xfrom, int yfrom, int xto, int yto, int speed, int hitBox, int damage, Boolean isFriendly, String path, Room r) {
        super(xfrom, yfrom, hitBox, path, r);
        this.damage = damage;
        this.isFriendly = isFriendly;
        double dx = xto - xfrom;
        double dy = yto - yfrom;
        double length = Math.sqrt(dx * dx + dy * dy);
        this.vx = (dx / length) * speed;
        this.vy = (dy / length) * speed;
        rotation = Math.toDegrees(Math.atan2(vy, vx));
    }

    public int getDamage() {
        return damage;
    }

    public double getRotation() {
        return rotation;
    }

    public boolean isFriendly() {
        return isFriendly;
    }

    public void tick(int w, int h) {
        currRoom.projectileTick(this);
        if (vx != 0 || vy != 0) {
        }
        if (currRoom.canMove(x + ((int)vx), y + ((int)vy), w, h)) {
            x += vx;
            y += vy;
        }
        else {
            currRoom.queueRemove(this);
        }
    }
}