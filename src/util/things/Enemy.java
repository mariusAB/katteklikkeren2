package util.things;

import java.util.List;
import java.util.ArrayList;

import util.Room;

public class Enemy extends Thing{
    private int speed;
    protected int damage;
    private int hp;
    private int maxHealth;
    protected double rotation = 0.0;
    protected boolean damagedMain = false;
    public Enemy(int x, int y, int hitBox, int speed, int damage, int hp, String path, Room r) {
        super(x, y, hitBox, path, r);
        this.speed = speed;
        this.damage = damage;
        this.hp = hp;
        maxHealth = hp;
        this.hp = hp;
    }

    public void damage(int dmg) {
        hp -= dmg;
        if (hp <= 0) {
            currRoom.get().queueRemove(this);
        }
    }

    public int getDamage() {
        return damage;
    }

    public int getHealth() {
        return hp;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public double getRotation() {
        return rotation;
    }

    public boolean damagedMain() {
        return damagedMain;
    }

    private List<Enemy> getNearbyEnemies(int x, int y) {
        List<Enemy> nearbyEnemies = new ArrayList<>();
        for (Enemy e : currRoom.get().getEnemies()) {
            if (!e.equals(this)) {
                nearbyEnemies.add(e);
            }
        }
        return nearbyEnemies;
    }

    private boolean containsEnemy(int x, int y) {
        for (Enemy e : getNearbyEnemies(x, y)) {
            double d = Math.sqrt(Math.pow(x-e.getX(), 2) + Math.pow(y-e.getY(), 2));
            if (d < hitBox + e.getHitBox()) {
                return true;
            }
        }
        return false;
    }

    private int getXBasedOnNearbyEnemies(int x, int y) {
        List<Enemy> nearbyEnemies = getNearbyEnemies(x, y);
        if (nearbyEnemies.isEmpty()) {
            return x;
        }
    
        double centerX = 0, centerY = 0;
        for (Enemy e : nearbyEnemies) {
            centerX += e.getX();
            centerY += e.getY();
        }
        centerX /= nearbyEnemies.size();
        centerY /= nearbyEnemies.size();
    
        double avoidanceX = x - centerX;
        double avoidanceY = y - centerY;
        double magnitude = Math.sqrt(avoidanceX * avoidanceX + avoidanceY * avoidanceY);
        if (magnitude == 0) {
            return x;
        }
    
        avoidanceX = (avoidanceX / magnitude) * speed;
        return (int) (x + avoidanceX);
    }
    
    private int getYBasedOnNearbyEnemies(int x, int y) {
        List<Enemy> nearbyEnemies = getNearbyEnemies(x, y);
        if (nearbyEnemies.isEmpty()) {
            return y;
        }
    
        double centerX = 0, centerY = 0;
        for (Enemy e : nearbyEnemies) {
            centerX += e.getX();
            centerY += e.getY();
        }
        centerX /= nearbyEnemies.size();
        centerY /= nearbyEnemies.size();
    
        double avoidanceY = y - centerY;
        double avoidanceX = x - centerX;
        double magnitude = Math.sqrt(avoidanceY * avoidanceY + avoidanceX * avoidanceX);
        if (magnitude == 0) {
            return y;
        }
    
        avoidanceY = (avoidanceY / magnitude) * speed;
        return (int) (y + avoidanceY);
    }

    public void tick() {
        int xMain = currRoom.get().getMainX();
        int yMain = currRoom.get().getMainY();
        double dx = xMain - x;
        double dy = yMain - y;
        double s = Math.sqrt(dx*dx + dy*dy);
        if (s <= hitBox) {
            damagedMain = true;
            currRoom.get().queueRemove(this);
        }
        rotation = Math.toDegrees(Math.atan2(dy, dx));
        double lerpFactor = speed / s;
        dx = lerp(x, xMain, lerpFactor);
        dy = lerp(y, yMain, lerpFactor);
        if (containsEnemy((int) dx, (int) dy)) {
            this.x = getXBasedOnNearbyEnemies((int) dx, (int) dy);
            this.y = getYBasedOnNearbyEnemies((int) dx, (int) dy);
        } else {
            this.x = (int) dx;
            this.y = (int) dy;
        }
    }

    private double lerp(double start, double end, double t) {
        return start + t * (end - start);
    }
}
