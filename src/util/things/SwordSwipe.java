package util.things;

import java.util.ArrayList;
import java.util.List;

import util.Room;

public class SwordSwipe extends Thing{
    private int damage;
    private List<Enemy> hitEnemies = new ArrayList<Enemy>();
    private int xTo;
    private int yTo;
    private int upTime;
    private int time = 0;
    private int length;
    private int spread;

    public SwordSwipe(int x, int y, int xTo, int yTo, int spread, int length, int damage, int upTime, String path, Room r) {
        super(x, y, 0, path, r);
        this.damage = damage;
        this.xTo = xTo;
        this.yTo = yTo;
        this.upTime = upTime;
        this.length = length;
        this.spread = spread;
    }

    public double getRotation() {
        return Math.toDegrees(Math.atan2(yTo - y, xTo - x));
    }
    
    public void tick() {
        if (time > upTime) {
            currRoom.get().queueRemove(this);
            return;
        }
        double swipeAngle = Math.atan2(yTo - y, xTo - x);
        double spreadRadians = Math.toRadians(spread);
        for (Enemy e : currRoom.get().getEnemies()) {
            double d = Math.sqrt(Math.pow(e.getX() - x, 2) + Math.pow(e.getY() - y, 2));
            if (d <= length) {
                double enemyAngle = Math.atan2(e.getY() - y, e.getX() - x);
                double angleDifference = Math.abs(enemyAngle - swipeAngle);
                if (angleDifference > Math.PI) {
                    angleDifference = 2 * Math.PI - angleDifference;
                }
                if (angleDifference < spreadRadians / 2) {
                    if (!hitEnemies.contains(e)) {
                        e.damage(damage);
                        hitEnemies.add(e);
                    }
                }
            }
        }
        time++;
    }
}
