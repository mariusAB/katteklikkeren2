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
    private Room currRoom = new Room();
    private List<Thing> toRemove = new ArrayList<Thing>();

    public Logic(Katteknappeklikkeren2 k) {
        this.k = k;
        main = new Thing(0,0,10, true, this);
        main.setPath("img/icon.png");
        k.addImage(main);
        activeThings.add(main);
        main.setSpeed(15);
    }

    public void tick() {
        Iterator<Thing> iterator = activeThings.iterator();
        while (iterator.hasNext()) {
            Thing t = iterator.next();
            t.tick(k.getWidth(), k.getHeight());
            k.addImage(t);
        }
        for (Thing t : toRemove) {
            activeThings.remove(t);
        }
        toRemove.clear();
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
        if (vx == 0 && vy == 0) {
            vx = 1;
        }
        s.use(main.getX(), main.getY(), vx, vy);
    }

    public void addProjectile(Thing p) {
        activeThings.add(p);
    }

    public void removeProjectile(Thing p) {
        activeThings.remove(p);
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
        toRemove.add(thing);
    }

    public void resize(int prevWidth, int prevHeight, int width, int height) {
        for (Thing thing : activeThings) {
            thing.resize(prevWidth, prevHeight, width, height);
        }
    }
}
