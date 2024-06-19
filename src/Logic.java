import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Logic {
    private List<Thing> activeThings = new ArrayList<Thing>();
    private Katteknappeklikkeren2 k;
    private int i = 0;
    private Thing main;
    private Set<Integer> keys;

    public Logic(Katteknappeklikkeren2 k) {
        this.k = k;
        main = new Thing(k.getSize().width / 2, k.getSize().height / 2);
        k.addImage(main);
        activeThings.add(main);
    }

    public void tick() {
        for (Thing thing : activeThings) {
            thing.tick();
            k.addImage(thing);
        }
        i++;
        if (keys != null) {
            if (keys.contains(KeyEvent.VK_A)) {
                main.moveX(-10);
            }
            if (keys.contains(KeyEvent.VK_D)) {
                main.moveX(10);
            }
            if (keys.contains(KeyEvent.VK_W)) {
                main.moveY(-10);
            }
            if (keys.contains(KeyEvent.VK_S)) {
                main.moveY(10);
            }
        }
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
}
