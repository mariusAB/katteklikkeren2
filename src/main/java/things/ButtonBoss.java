package things;

import util.Room;

public class ButtonBoss extends ButtonEnemy{
    public ButtonBoss(int x, int y, int hitBox, int radius, int damage, int fireDelay, int hp, String pathUp, String pathDown, String pathDead, double imageScalar, Room r) {
        super(x, y, hitBox, radius, damage, fireDelay, hp, pathUp, pathDown, pathDead, imageScalar, r);
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
            if (currRoom.get().getRoomTick()%(fireDelay*5) == 0) {
                Enemy minion = new Enemy(x, y, 60, 15, 25, 100, "src/resources/img/enemy.png", 1, currRoom.get());
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
