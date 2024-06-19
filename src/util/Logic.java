package util;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java.util.Iterator;

import weapons.Staff;

public class Logic {
    private List<Thing> activeThings = new ArrayList<Thing>();
    private Katteknappeklikkeren2 k;
    private Thing main;
    private Set<Integer> keys;
    private List<Projectile> projectiles = new ArrayList<Projectile>();
    private Room currRoom = new Room();

    public Logic(Katteknappeklikkeren2 k) {
        this.k = k;
        main = new Thing(0,0,10, true);
        main.setPath("img/icon.png");
        k.addImage(main);
        activeThings.add(main);
        main.setSpeed(15);
    }

    public void tick() {
        for (Thing thing : activeThings) {
            thing.tick();
            k.addImage(thing);
        }
        Iterator<Projectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            Projectile p = iterator.next();
            if (p.isHoming()) {
                // Using the radius of the projectile as the hitbox
                if (Math.sqrt(Math.pow(p.getX() - main.getX(), 2) + Math.pow(p.getY() - main.getY(), 2)) < p.getHitBox()){
                    iterator.remove();
                }
                p.homeTick(main, k.getWidth(), k.getHeight());
            }
            else {
                for (Thing t : activeThings) {
                    if (!t.isFriendly() && Math.sqrt(Math.pow(p.getX() - t.getX(), 2) + Math.pow(p.getY() - t.getY(), 2)) < p.getHitBox()){
                        iterator.remove();
                    }
                }
                p.tick();
            }
            k.addImage(p);
        }
        if (keys != null) {
            if (keys.contains(KeyEvent.VK_A)) {
                main.moveX(-main.getSpeed(), k.getWidth(), k.getHeight());
                main.setPath("img/back.png");
            }
            if (keys.contains(KeyEvent.VK_D)) {
                main.moveX(main.getSpeed(), k.getWidth(), k.getHeight());
                main.setPath("img/icon.png");
            }
            if (keys.contains(KeyEvent.VK_W)) {
                main.moveY(-main.getSpeed(), k.getWidth(), k.getHeight());
            }
            if (keys.contains(KeyEvent.VK_S)) {
                main.moveY(main.getSpeed(), k.getWidth(), k.getHeight());
            }
        }
    }

    public Room getRoom() {
        return currRoom;
    }

    public void toMid() {
        main.setX(k.getSize().width / 2);
        main.setY(k.getSize().height / 2);
    }

    public void mouseClicked(int x, int y) {
        int vx = x - main.getX();
        int vy = y - main.getY();
        Staff s = new Staff(this);
        s.use(main.getX(), main.getY(), vx, vy, false);
    }

    public void addProjectile(Projectile p) {
        projectiles.add(p);
    }

    public void removeProjectile(Projectile p) {
        projectiles.remove(p);
    }

    public void keys(Set<Integer> keys) {
        this.keys = keys;
    }

    public List<Thing> getActiveThings() {
        return new ArrayList<Thing>(activeThings);
    }

    public void addThing(Thing thing) {
        activeThings.add(thing);
    }

    public void removeThing(Thing thing) {
        activeThings.remove(thing);
    }

    public void resize(int prevWidth, int prevHeight, int width, int height) {
        for (Thing thing : activeThings) {
            thing.resize(prevWidth, prevHeight, width, height);
        }
    }
}
