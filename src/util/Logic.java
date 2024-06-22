package util;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import util.things.Main;
import util.things.Thing;
import util.things.Enemy;
import util.things.Projectile;
import util.things.SwordSwipe;

import java.util.Iterator;

import weapons.FireBallStaff;
import weapons.MagicStaff;
import weapons.StarterSword;
import weapons.Weapon;

public class Logic {
    private Katteknappeklikkeren2 k;
    private Main main;
    private Set<Integer> keys;
    private Map map = new Map(this);
    private Room currRoom = map.getCurrRoom();
    private List<Thing> toRemove = new ArrayList<Thing>();
    private ActionEvent source;
    private ImageHandler ih;
    private Weapon currWeapon;
    private boolean held = false;
    private int mouseX = 0;
    private int mouseY = 0;

    public Logic(Katteknappeklikkeren2 k) {
        this.k = k;
        main = new Main(10, 7, 100, "img/venstrefly.png", "img/icon.png", currRoom);
        k.addImage(main);
        currRoom.addMain(main);
        ih = new ImageHandler();
        StarterSword w = new StarterSword(currRoom);
        w.equip();
        setWeapon(w);
    }

    public void tick() {
        currRoom.tick();
        Iterator<Thing> iterator = currRoom.getThings().iterator();
        while (iterator.hasNext()) {
            Thing t = iterator.next();
            if (t instanceof Enemy) {
                ((Enemy) t).tick();
            }
            if (t instanceof Projectile) {
                ((Projectile) t).tick(k.getWidth(), k.getHeight());
            }
            if (t instanceof SwordSwipe) {
                ((SwordSwipe) t).tick();
            }
            k.addImage(t);
        }
        for (Thing t : toRemove) {
            if (t instanceof Enemy) {
                if (((Enemy) t).damagedMain()) {
                    currRoom.killButDamageMain((Enemy) t);
                }
                else {
                    currRoom.kill(t);
                }
            }
            else {
                currRoom.removeThing(t);
            }
        }
        if (held) {
            mouseClicked(mouseX, mouseY);
        }
        toRemove.clear();
        if (keys != null) {
            if (keys.contains(KeyEvent.VK_A)) {
                main.moveX(-main.getSpeed(), k.getWidth(), k.getHeight());
            }
            if (keys.contains(KeyEvent.VK_D)) {
                main.moveX(main.getSpeed(), k.getWidth(), k.getHeight());
            }
            if (keys.contains(KeyEvent.VK_W)) {
                main.moveY(-main.getSpeed(), k.getWidth(), k.getHeight());
            }
            if (keys.contains(KeyEvent.VK_S)) {
                main.moveY(main.getSpeed(), k.getWidth(), k.getHeight());
            }
        }
    }

    public ImageHandler getImageHandler() {
        return ih;
    }

    public Room getRoom() {
        return currRoom;
    }

    public Main getMain() {
        return main;
    }

    public void giveSource(ActionEvent e) {
        source = e;
    }

    public void interact() {
        currRoom.interact();
    }

    public void gameOver() {
        k.gameOver(source);
    }

    public void toMid() {
        main.setX(k.getSize().width / 2);
        main.setY(k.getSize().height / 2);
    }

    public void mouseClicked(int x, int y) {
        useWeapon(x, y);
        mouseX = x;
        mouseY = y;
    }

    public void mouseHeld(Boolean held) {
        this.held = held;
    }

    private void useWeapon(int x, int y) {
        currWeapon.use(main.getX(), main.getY(), x, y);
    }

    public Weapon getWeapon() {
        return currWeapon;
    }

    public void setWeapon(Weapon w) {
        currWeapon = w;
    }

    public void keys(Set<Integer> keys) {
        this.keys = keys;
    }

    public void removeThing(Thing thing) {
        toRemove.add(thing);
    }

    public void resize(int prevWidth, int prevHeight, int width, int height) {
        for (Thing thing : currRoom.getThings()) {
            thing.resize(prevWidth, prevHeight, width, height);
        }
    }
}
