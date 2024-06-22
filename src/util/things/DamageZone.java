package util.things;

import java.util.ArrayList;
import java.util.List;

import util.Room;

public class DamageZone extends Thing{
    private int damage;
    private List<Enemy> hitEnemies = new ArrayList<Enemy>();
    private int upTime;
    private int time = 0;

    public DamageZone(int x, int y, int size, int damage, int upTime, String path, Room r) {
        super(x, y, size, path, r);
        this.damage = damage;
        this.upTime = upTime;
    }
    
    public void tick() {
        if (time > upTime) {
            currRoom.queueRemove(this);
            return;
        }
        for (Enemy e : currRoom.getEnemies()) {
            if (e.getHitBox() + hitBox > Math.sqrt(Math.pow(e.getX() - x, 2) + Math.pow(e.getY() - y, 2))) {
                if (!hitEnemies.contains(e)) {
                    e.damage(damage);
                    hitEnemies.add(e);
                }
            }
        }
        time++;
    }
}
