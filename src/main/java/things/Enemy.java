package things;

import java.util.List;

import util.Room;

import java.util.ArrayList;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;

public class Enemy extends Thing{
    private int speed;
    protected int damage;
    protected int hp;
    private int maxHealth;
    private double scalar;
    protected double rotation = 0.0;
    protected boolean damagedMain = false;
    public Enemy(int x, int y, int hitBox, int speed, int damage, int hp, String path, double imageScalar, Room r) {
        super(x, y, hitBox, path, r);
        this.speed = speed;
        this.damage = damage;
        this.scalar = imageScalar;
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
        for (Enemy e : currRoom.get().getEnemiesToAvoid()) {
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

    private Point getAvoidancePointBasedOnNearbyEnemies(int x, int y) {
        List<Enemy> nearbyEnemies = getNearbyEnemies(x, y);
        if (nearbyEnemies.isEmpty()) {
            return new Point(x, y);
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
    
        // Increase the avoidance distance if too close
        final double MIN_DISTANCE = 10; // Minimum distance to maintain from other enemies
        if (magnitude < MIN_DISTANCE) {
            magnitude = MIN_DISTANCE;
        }
    
        if (magnitude == 0) {
            return new Point(x, y);
        }
        avoidanceX = (avoidanceX / magnitude) * speed;
        avoidanceY = (avoidanceY / magnitude) * speed;
        return new Point((int) (x + avoidanceX), (int) (y + avoidanceY));
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
        dx /= s;
        dy /= s;
        dx *= speed;
        dy *= speed;
        if (containsEnemy((int) dx + this.x, (int) dy + this.y)) {
            Point avoidancePoint = getAvoidancePointBasedOnNearbyEnemies((int) dx + this.x, (int) dy + this.y);
            dx = avoidancePoint.x - x;
            dy = avoidancePoint.y - y;
            dx /= s;
            dy /= s;
            dx *= speed;
            dy *= speed;
        }
        this.x += dx;
        this.y += dy;
    }

        public Image getImg(int w, int h) {
        try {
            if (currRoom.get() != null) {
                return currRoom.get().getImageHandler().getImage(path, w, h, scalar);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
