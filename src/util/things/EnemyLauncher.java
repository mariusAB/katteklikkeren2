package util.things;

import util.Room;

public class EnemyLauncher extends Enemy{
    private int fireDelay = 20;
    private int projectileSpeed = 50;
    private int projectileHitBox = 200;
    public EnemyLauncher(int x, int y, int hitBox, int damage, int fireDelay, int hp, String path, Room r) {
        super(x, y, hitBox, 0, damage, hp, path, r);
        this.fireDelay = fireDelay;
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
        if (currRoom.get().getRoomTick()%fireDelay == 0) {
            Projectile p = new Projectile(x, y, xMain, yMain, projectileSpeed, projectileHitBox, damage, false, "img/magicProjectile.png", currRoom.get());
            currRoom.get().addThing(p);
        }
    }
}
