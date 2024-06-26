package util;
import java.awt.Image;
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

import weapons.StarterSword;
import weapons.Weapon;

public class Logic {
    private Katteklikkeren2 k;
    private Main main;
    private Set<Integer> keys;
    private Map map;
    private Room currRoom;
    private List<Thing> toRemove = new ArrayList<Thing>();
    private ActionEvent source;
    private ImageHandler ih;
    private Weapon currWeapon;
    private boolean held = false;
    private int mouseX = 0;
    private int mouseY = 0;
    private double margin = 0.06;
    private int width = 10000;
    private int height = 6000;

    public Logic(Katteklikkeren2 k) {
        this.k = k;
        map = new Map(this, 7, 7);
        currRoom = map.getCurrRoom();
        main = new Main(70, 50, 100, "img/venstrefly.png", "img/icon.png", currRoom);
        k.addImage(main);
        currRoom.addMain(main);
        ih = new ImageHandler();
        k.changeBackground(currRoom.getBackground());
        StarterSword w = new StarterSword(currRoom);
        w.equip();
        setWeapon(w);
    }

    public void displayMiniMap() {
        k.displayMiniMap(map.getMiniMap());
    }
    
    public void hideMiniMap() {
        k.hideMiniMap();
    }
    
    public double getMargin() {
        return margin;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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
                ((Projectile) t).tick();
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
                main.moveX(-main.getSpeed());
            }
            if (keys.contains(KeyEvent.VK_D)) {
                main.moveX(main.getSpeed());
            }
            if (keys.contains(KeyEvent.VK_W)) {
                main.moveY(-main.getSpeed());
            }
            if (keys.contains(KeyEvent.VK_S)) {
                main.moveY(main.getSpeed());
            }
        }
    }

    public ImageHandler getImageHandler() {
        return ih;
    }

    public Map getMap() {
        return map;
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
        main.setX(currRoom.getWidth() / 2);
        main.setY(currRoom.getHeight() / 2);
    }

    public void mouseClicked(int x, int y) {
        useWeapon(x, y);
        mouseX = x;
        mouseY = y;
    }

    public void changeBackground(Image i) {
        k.changeBackground(i);
    }

    public int getMapWidth() {
        return map.getMapWidth();
    }

    public int getMapHeight() {
        return map.getMapHeight();
    }

    public void setRoom(Room r) {
        currRoom.resetTick();
        currRoom = r;
        currRoom.addMain(main);
        main.setRoom(r);
        k.changeBackground(r.getBackground());
        if (!r.getOpenStatus()) {
            hideMiniMap();
        }
        currWeapon.setRoom(currRoom);
        currWeapon.reset();
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

    public void resized() {
        k.changeBackground(currRoom.getBackground());
    }
}
