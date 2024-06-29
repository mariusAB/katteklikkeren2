package util;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import things.*;

public class Room {
    protected List<Obstacle> obstacles = new ArrayList<>();
    protected List<Projectile> projectiles = new ArrayList<>();
    protected List<Enemy> enemies = new ArrayList<>();
    protected List<Item> items = new ArrayList<>();
    protected List<Button> buttons = new ArrayList<>();
    protected List<Portal> portals = new ArrayList<>();
    protected List<SwordSwipe> swordswipes = new ArrayList<>();
    protected double margin = 0.06;
    protected Logic l;
    protected Main main;
    protected int i = 0;
    private String path = "src/resources/img/room1.png";
    protected List<Door> doors = new ArrayList<>();
    private boolean open = false;
    protected int width = 10000;
    protected int height = 6000;

    public Room(Logic l) {
        this.l = l;
        l.hideMiniMap();
        margin = l.getMargin();
        height = l.getHeight();
        width = l.getWidth();
    }

    public void addMain(Main m) {
        this.main = m;
    }

    public Main getMain() {
        return main;
    }

    public void resetTick() {
        i = 0;
        projectiles.clear();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getBackground() {
        if (!open) {
            for (int i = 0; i < 4; i++) {
                if (l.getMap().getDoors().get(i)) {
                    if (i == 0) {
                        Door d = new Door(
                        (int) (width / 2.4), 
                        (int) (height * margin), 
                        (int) (width / 1.7), 
                        (int) (height * margin + 70), 
                        0,
                        this
                    );
                    doors.add(d);
                    }
                    if (i == 1) {
                        Door d = new Door(
                        (int) (width * (1 - margin) - 70), 
                        (int) (height / 2.9), 
                        (int) (width), 
                        (int) (height / 1.7), 
                        1,
                        this
                    );
                    doors.add(d);
                    }
                    if (i == 2) {
                        Door d = new Door(
                        (int) (width / 2.4), 
                        (int) (height * (1 - margin*2) - 70), 
                        (int) (width / 1.7), 
                        (int) (height * (1 - margin)), 
                        2,
                        this
                    );
                    doors.add(d);
                    }
                    if (i == 3) {
                        Door d = new Door(
                        (int) (width * margin), 
                        (int) (height / 2.9), 
                        (int) (width * margin) + 70, 
                        (int) (height / 1.7), 
                        3,
                        this
                    );
                    doors.add(d);
                    }
                }
            }
            return getImageHandler().getBackground(path, l.getMap().getDoors(), false);
        }
        l.displayMiniMap();
        return getImageHandler().getBackground(path, l.getMap().getDoors(), true);
    }

    public double getMargin() {
        return margin;
    }

    public List<Door> getDoors() {
        return doors;
    }

    public void removeThing(Thing t) {
        if (t instanceof Projectile) {
            projectiles.remove(t);
        } else if (t instanceof Item) {
            items.remove(t);
        } else if (t instanceof SwordSwipe) {
            swordswipes.remove(t);
        } else if (t instanceof Obstacle) {
            obstacles.remove(t);
        }
    }

    public void addThing(Thing t) {
        if (t instanceof Projectile) {
            projectiles.add((Projectile) t);
        } else if (t instanceof Item) {
            items.add((Item) t);
        } else if (t instanceof SwordSwipe) {
            swordswipes.add((SwordSwipe) t);
        } else if (t instanceof Enemy) {
            enemies.add((Enemy) t);
        } else if (t instanceof Obstacle) {
            obstacles.add((Obstacle) t);
        } else if (t instanceof WeaponItem) {
            items.add((WeaponItem) t);
        } else if (t instanceof Button) {
            buttons.add((Button) t);
        } else if (t instanceof Portal) {
            portals.add((Portal) t);
        }
    }

    public void queueRemove(Thing t) {
        l.removeThing(t);
    }

    public Logic getLogic() {
        return l;
    }

    public ImageHandler getImageHandler() {
        return l.getImageHandler();
    }

    public void kill(Thing t) {
        l.enemyKilled();
        enemies.remove(t);
    }

    public void killButDamageMain(Enemy e) {
        damageMain(e.getDamage());
        enemies.remove(e);
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Enemy> getEnemiesToAvoid() {
        return enemies;
    }

    public int getMainX() {
        return l.getMain().getX();
    }

    public int getMainY() {
        return l.getMain().getY();
    }

    public void damageMain(int dmg) {
        l.getMain().damage(dmg);
    }

    public void gameOver() {
        l.gameOver();
    }

    public List<Thing> getThings() {
        List<Thing> newList = new ArrayList<>();
        newList.addAll(portals);
        newList.addAll(items);
        newList.addAll(obstacles);
        newList.addAll(buttons);
        newList.addAll(enemies);
        newList.addAll(swordswipes);
        newList.addAll(projectiles);
        newList.add(main);
        return newList;
    }

    public void addSwordSwipe(SwordSwipe s) {
        swordswipes.add(s);
    }

    public void tick() {
        if (enemies.size() == 0) {
            l.displayMiniMap();
            openDoors();
        }
        for (Door d : doors) {
            d.tick();
        }
        i++;
        Iterator<Thing> iterator = getThings().iterator();
        while (iterator.hasNext()) {
            Thing t = iterator.next();
            if (t instanceof Enemy) {
                ((Enemy) t).tick();
            }
            else if (t instanceof Projectile) {
                ((Projectile) t).tick();
            }
            else if (t instanceof SwordSwipe) {
                ((SwordSwipe) t).tick();
            }
            else if (t instanceof Button) {
                ((Button) t).tick();
            }
            else if (t instanceof Portal) {
                ((Portal) t).tick();
            }
        }
    }

    public void buttonClicked() {
        l.buttonClicked();
    }

    public void openPortal() {
        for (Portal p : portals) {
            p.open();
        }
    }

    protected void openDoors() {
        if (!open) {
            l.changeBackground(getImageHandler().getBackground(path, l.getMap().getDoors(), true));
            for (Door d : doors) {
                d.open();
            }
            open = true;
        }
    }

    public int getRoomTick() {
        return i;
    }

    public boolean canMove(int x, int y, Thing t) {
        if (x < width*margin || x > width*(1-margin) || y < height*margin || y > height*(1-margin*2)) {
            return false;
        }
        for (Obstacle o : obstacles) {
            if (o.isRound()) {
                double d = Math.sqrt(Math.pow(x-o.getX(), 2) + Math.pow(y-o.getY(), 2));
                if (d <= o.getHitBox()) {
                    return false;
                }
            } else {
                if (x < o.getX() + o.getHitBox() && x > o.getX() - o.getHitBox() && y < o.getY() + o.getHitBox() && y > o.getY() - o.getHitBox()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isOutOfBounds(int x, int y) {
        if (x < 0 || x > width*1.2 || y < 0 || y > height*1.2) {
            return true;
        }
        return false;
    }

    public boolean getOpenStatus() {
        return open;
    }

    public void teleport() {
        l.teleport();
    }

    public void interact() {
        for (Item i : items) {
            double d = Math.sqrt(Math.pow(main.getX()-i.getX(), 2) + Math.pow(main.getY()-i.getY(), 2));
            if (d <= main.getHitBox() + i.getHitBox()) {
                i.interact();
                return;
            }
        }
    }

    public int getTotalButtons() {
        return 0;
    }
}
