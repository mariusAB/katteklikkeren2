package things;

import util.BossRoom;
import util.Room;

public class ButtonEnemy extends Enemy{
    protected int fireDelay = 20;
    protected int projectileSpeed = 25;
    protected int projectileHitBox = 70;
    protected String pathUp;
    private String pathDown;
    private String pathDead;
    protected int currentAttack = 0;
    protected Boolean dead = false;
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
        if (currentAttack != 0 && !dead) {
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
        if (currRoom instanceof BossRoom) {
            ((BossRoom) currRoom).activateNext();
        }
        currRoom.buttonClicked();
    }

    private void attack() {
        if (currentAttack == 0) {
        }
        else  if (currentAttack == 1) {
            int xMain = currRoom.getMainX();
            int yMain = currRoom.getMainY();
            if (currRoom.getRoomTick()%fireDelay == 0) {
                Projectile p = new Projectile(x, y, xMain, yMain, projectileSpeed, projectileHitBox, damage, false, false, "src/resources/img/enemyProjectile.png", currRoom);
                currRoom.addThing(p);
            }
        }
        else if (currentAttack == 2) {
            if (currRoom.getRoomTick()%(fireDelay*10) == 0) {
                Enemy minion = new Enemy(x, y, 40, 25, 20, 20, "src/resources/img/enemy.png", 0.5, currRoom);
                currRoom.addThing(minion);
            }
        }
    }

    public void tick() {
        if (!dead) {
            attack();
        }
    }
}
