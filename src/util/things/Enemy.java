package util.things;

import util.Room;

public class Enemy extends Thing{
    private int speed;
    private int damage;
    private int hp;
    private int maxHealth;
    private double rotation = 0.0;
    private boolean damagedMain = false;
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
        this.x = (int) dx;
        this.y = (int) dy;
    }

    private double lerp(double start, double end, double t) {
        return start + t * (end - start);
    }
}
