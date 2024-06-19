package util;
public class Projectile extends Thing{
    private int x;
    private int y;
    private int vx;
    private int vy;
    private double speed;
    private boolean homing = false;

    public Projectile(int x, int y, int vx, int vy, boolean homing) {
        super(x, y);
        this.vx = vx;
        this.vy = vy;
        this.homing = homing;
        speed = Math.sqrt(vy * vy + vx * vx);
    }

    public void homeTick(Thing t) {
        int dirX = t.getX() - x;
        int dirY = t.getY() - y;
        x += (int) (dirX / speed);
        y += (int) (dirY / speed);
    }

    public void tick() {
        x += vx;
        y += vy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isHoming() {
        return homing;
    }
}
