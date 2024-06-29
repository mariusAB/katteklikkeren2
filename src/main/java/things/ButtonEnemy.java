package things;

import util.BossRoom;
import util.Room;

public class ButtonEnemy extends Enemy{
    private int fireDelay = 20;
    private int projectileSpeed = 25;
    private int projectileHitBox = 70;
    private String pathUp;
    private String pathDown;
    private String pathDead;
    private int currentAttack = 0;
    private Boolean dead = false;
    private int radius;

    public ButtonEnemy(int x, int y, int hitBox, int radius, int damage, int fireDelay, int hp, String pathUp, String pathDown, String pathDead, double imageScalar, Room r) {
        super(x, y, hitBox, 0, damage, hp, pathDown, imageScalar, r);
        this.fireDelay = fireDelay;
        this.pathUp = pathUp;
        this.pathDown = pathDown;
        this.pathDead = pathDead;
        path = pathDown;
        this.radius = radius;
    }

    public void activate() {
        path = pathUp;
        int randAttack = (int)(Math.random()*2) + 1;
        currentAttack = randAttack;
    }

    public int getRadius() {
        return radius;
    }

    public boolean canBeActivated() {
        return !dead;
    }

    public void deActivate() {
        if (!dead) {
            path = pathDown;
            currentAttack = 0;
        }
    }

    public void damage(int dmg) {
        if (currentAttack != 0) {
            hp -= dmg;
            if (hp <= 0) {
                die();
            }
        }
    }

    public void die() {
        path = pathDead;
        dead = true;
        radius = 0;
        if (currRoom.get() instanceof BossRoom) {
            ((BossRoom) currRoom.get()).activateNext();
        }
    }

    private void attack() {
        if (currentAttack == 0) {
        }
        else  if (currentAttack == 1) {
            int xMain = currRoom.get().getMainX();
            int yMain = currRoom.get().getMainY();
            double dx = xMain - x;
            double dy = yMain - y;
            double s = Math.sqrt(dx*dx + dy*dy);
            if (s <= hitBox) {
                damagedMain = true;
                currRoom.get().queueRemove(this);
            }
            if (currRoom.get().getRoomTick()%fireDelay == 0) {
                Projectile p = new Projectile(x, y, xMain, yMain, projectileSpeed, projectileHitBox, damage, false, "src/resources/img/magicProjectile.png", currRoom.get());
                currRoom.get().addThing(p);
            }
        }
        else if (currentAttack == 2) {
            if (currRoom.get().getRoomTick()%(fireDelay*10) == 0) {
                Enemy minion = new Enemy(x, y, 40, 25, 20, 20, "src/resources/img/enemy.png", 0.5, currRoom.get());
                currRoom.get().addThing(minion);
            }
        }
    }

    public void tick() {
        if (!dead) {
            attack();
        }
    }
}
