package things;

import util.Room;

public class ButtonBoss extends ButtonEnemy{

    public ButtonBoss(int x, int y, int hitBox, int radius, int damage, int fireDelay, int hp, String pathUp, String pathDown, String pathDead, double imageScalar, Room r) {
        super(x, y, hitBox, radius, damage, fireDelay, hp, pathUp, pathDown, pathDead, imageScalar, r);
    }

    public void activate() {
        path = pathUp;
        int randAttack = (int)(Math.random()*3) + 1;
        currentAttack = randAttack;
    }

    private void attack() {
        if (currentAttack == 0) {
        }
        else  if (currentAttack == 1) {
            int xMain = currRoom.getMainX();
            int yMain = currRoom.getMainY();
            double dx = xMain - x;
            double dy = yMain - y;
            double s = Math.sqrt(dx*dx + dy*dy);
            if (s <= hitBox) {
                damagedMain = true;
                currRoom.queueRemove(this);
            }
            if (currRoom.getRoomTick()%fireDelay == 0) {
                Projectile p = new Projectile(x, y, xMain, yMain, projectileSpeed, projectileHitBox, damage, false, false, "src/resources/img/enemyProjectile.png", currRoom);
                currRoom.addThing(p);
            }
        }
        else if (currentAttack == 2) {
            if (currRoom.getRoomTick()%(fireDelay*5) == 0) {
                Enemy minion = new Enemy(x, y, 60, 15, 25, 100, "src/resources/img/enemy.png", 1, currRoom);
                currRoom.addThing(minion);
            }
        }
        else if (currentAttack == 3) {
            if (currRoom.getRoomTick()%(fireDelay) == 0) {
                int randX = (int) (Math.random() * currRoom.getWidth());
                Projectile p = new Projectile(randX, 0, randX, 1, 10, 60, 50, false, true, "src/resources/img/fireBall.png", currRoom);
                currRoom.addThing(p);
            }
        }
    }
    
    public void tick() {
        if (!dead) {
            attack();
        }
    }
}
