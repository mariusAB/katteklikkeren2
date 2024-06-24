package util;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import util.things.Door;
import util.things.Enemy;
import util.things.Item;
import util.things.Main;
import util.things.Obstacle;
import util.things.Projectile;
import util.things.SwordSwipe;
import util.things.Thing;
import util.things.WeaponItem;
import weapons.FireBallStaff;
import weapons.MagicStaff;

public class Room {
    private List<Obstacle> obstacles = new ArrayList<>();
    private List<Projectile> projectiles = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private List<Item> items = new ArrayList<>();
    private List<SwordSwipe> swordswipes = new ArrayList<>();
    private double margin = 0.06;
    private Logic l;
    private Main main;
    private int i = 0;
    private String path = "img/room1.png";
    private List<Door> doors = new ArrayList<>();
    private boolean open = false;
    private int width = 10000;
    private int height = 6000;

    public Room(Logic l) {
        Obstacle obstacle = new Obstacle(1000, 1000, 300, "img/icon.png", this);
        addThing(obstacle);
        this.l = l;

        Enemy enemy = new Enemy(5000, 200, 60, 5, 25, 100, "img/icon.png", this);
        addThing(enemy);

        Enemy enemy2 = new Enemy(6000, 200, 60, 17, 25, 50, "img/icon.png", this);
        addThing(enemy2);

        Enemy enemy3 = new Enemy(7000, 200, 60, 17, 25, 50, "img/icon.png", this);
        addThing(enemy3);

        Enemy enemy4 = new Enemy(8000, 200, 60, 17, 25, 50, "img/icon.png", this);
        addThing(enemy4);

        Item item = new Item(2000, 5000, 300, "HealthPotion", "img/healthPotion.png", this);
        addThing(item);

        MagicStaff weapon = new MagicStaff(this);

        WeaponItem weaponItem = new WeaponItem(3000, 5000, 300, weapon, this);
        addThing(weaponItem);

        l.hideMiniMap();
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
        enemies.remove(t);
    }

    public void killButDamageMain(Enemy e) {
        damageMain(e.getDamage());
        enemies.remove(e);
    }

    public List<Enemy> getEnemies() {
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
        newList.addAll(items);
        newList.addAll(obstacles);
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
            openDoors();
        }
        for (Door d : doors) {
            d.tick();
        }
        i++;
    }

    private void openDoors() {
        if (!open) {
            l.changeBackground(getImageHandler().getBackground(path, l.getMap().getDoors(), true));
            for (Door d : doors) {
                d.open();
            }
            l.displayMiniMap();
            open = true;
        }
    }

    public int getRoomTick() {
        return i;
    }

    public boolean canMove(int x, int y) {
        if (x < width*margin || x > width*(1-margin) || y < height*margin || y > height*(1-margin*2)) {
            return false;
        }
        for (Thing o : obstacles) {
            double d = Math.sqrt(Math.pow(x-o.getX(), 2) + Math.pow(y-o.getY(), 2));
            if (d <= o.getHitBox()) {
                return false;
            }
        }
        return true;
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
}
