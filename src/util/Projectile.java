package util;
public class Projectile extends Thing{
    private int x;
    private int y;
    private int vx;
    private int vy;
    private double speed;
    private boolean homing = false;
    private Logic l;

    public Projectile(int x, int y, int vx, int vy, boolean homing, double speed, Logic l) {
        super(x, y, 0);
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.l = l;
        this.homing = homing;
    }

    public void homeTick(Thing t, int w, int h) {
        int dirX = t.getX() - x;
        int dirY = t.getY() - y;
        x += (int) (dirX / speed);
        y += (int) (dirY / speed);
    }

    public void tick(int w, int h) {
        if (l.getRoom().canMove(x, y, w, h)) {
            x += vx;
            y += vy;
        }
        else {
            l.removeProjectile(this);
        }
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
