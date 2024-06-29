package things;

import util.Room;

public class EnemyLauncher extends Enemy{
    private int fireDelay = 40;
    private int projectileSpeed = 25;
    private int projectileHitBox = 70;
    public EnemyLauncher(int x, int y, int hitBox, int damage, int fireDelay, int hp, String path, double imageScalar, Room r) {
        super(x, y, hitBox, 0, damage, hp, path, imageScalar, r);
        this.fireDelay = fireDelay;
    }

    public void tick() {
        if (currRoom.get().getRoomTick() > 40) {
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
            if (currRoom.get().getRoomTick()%fireDelay == 0) {
                Projectile p = new Projectile(x, y, xMain, yMain, projectileSpeed, projectileHitBox, damage, false, false, "src/resources/img/enemyProjectile.png", currRoom.get());
                currRoom.get().addThing(p);
            }
        }
    }
}
