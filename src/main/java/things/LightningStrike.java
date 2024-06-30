package things;

import java.util.ArrayList;
import java.util.List;

import util.Room;

public class LightningStrike extends Thing {
    private int damage;
    private List<Enemy> hitEnemies = new ArrayList<>();
    private int xTo;
    private int yTo;
    private int upTime;
    private int time = 0;
    private int width;
    private int maxLength = 5000;

    public LightningStrike(int x, int y, int xTo, int yTo, int width, int damage, int upTime, String path, Room r) {
        super(x, y, 0, path, r);
        this.damage = damage;
        this.upTime = upTime;
        this.width = width;
    
        double distance = Math.hypot(xTo - x, yTo - y);
        if (distance > maxLength) {
            double dx = xTo - x;
            double dy = yTo - y;
            double factor = maxLength / distance;
            this.xTo = x + (int)(dx * factor);
            this.yTo = y + (int)(dy * factor);
        } else {
            this.xTo = xTo;
            this.yTo = yTo;
        }
    }

    public double getRotation() {
        return Math.toDegrees(Math.atan2(yTo - y, xTo - x));
    }

    private double distanceFromLineToPoint(int px, int py) {
        double normalLength = Math.hypot(xTo - x, yTo - y);
        return Math.abs((px - x) * (yTo - y) - (py - y) * (xTo - x)) / normalLength;
    }

    public void tick() {
        if (time > upTime) {
            currRoom.queueRemove(this);
            return;
        }
        double lx = xTo - x;
        double ly = yTo - y;
    
        for (Enemy e : currRoom.getEnemies()) {
            if (!hitEnemies.contains(e)) {
                double ex = e.getX() - x;
                double ey = e.getY() - y;
                double dotProduct = ex * lx + ey * ly;
                double s = Math.sqrt(ex*ex + ey*ey);
    
                if (dotProduct > 0 && s < maxLength) {
                    double distanceToLine = distanceFromLineToPoint(e.getX(), e.getY());
                    if (distanceToLine <= e.getHitBox() + this.width) {
                        e.damage(damage);
                        hitEnemies.add(e);
                    }
                }
            }
        }
        time++;
    }
}