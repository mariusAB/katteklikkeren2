package things;

import util.Room;

public class Projectile extends Thing{
    private int damage;
    private Boolean isFriendly;
    private double vx;
    private double vy;
    private double rotation;
    private boolean canPassThroughObstacles;

    public Projectile(int xfrom, int yfrom, int xto, int yto, int speed, int hitBox, int damage, Boolean isFriendly, boolean canPassThroughObstacles, String path, Room r) {
        super(xfrom, yfrom, hitBox, path, r);
        this.damage = damage;
        this.isFriendly = isFriendly;
        this.canPassThroughObstacles = canPassThroughObstacles;
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

    public void tick() {
        if (currRoom != null) {
            if (isFriendly) {
                for (Enemy e : currRoom.getEnemies()) {
                    double d = Math.sqrt(Math.pow(getX()-e.getX(), 2) + Math.pow(getY()-e.getY(), 2));
                    if (d <= getHitBox() + e.getHitBox()) {
                        e.damage(getDamage());
                        currRoom.queueRemove(this);
                        return;
                    }
                }
            } else {
                Main main = currRoom.getMain();
                double d = Math.sqrt(Math.pow(getX()-main.getX(), 2) + Math.pow(getY()-main.getY(), 2));
                if (d <= getHitBox() + main.getHitBox()) {
                    main.damage(getDamage());
                    currRoom.queueRemove(this);
                    return;
                }
            }
            if (vx != 0 || vy != 0) {
            }
            if (canPassThroughObstacles) {
                x += vx;
                y += vy;
                if (currRoom.isOutOfBounds(x, y)) {
                    currRoom.queueRemove(this);
                }
            }
            else if (currRoom.canMove(x + ((int)vx), y + ((int)vy), this)) {
                x += vx;
                y += vy;
            }
            else {
                currRoom.queueRemove(this);
            }
        }
    }
}