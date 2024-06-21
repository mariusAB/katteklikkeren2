package util;
import java.util.ArrayList;
import java.util.List;

//TODO: prosjektiler overlever mellom wipes noen ganger

public class Room {
    private List<Thing> obstacles = new ArrayList<>();
    private List<Thing> things = new ArrayList<>();
    private List<Thing> enemies = new ArrayList<>();
    private List<Item> items = new ArrayList<>();
    private double margin = 0.03;
    private Logic l;
    private Thing main;
    private int i = 0;

    public Room(Logic l) {
        Thing obstacle = new Thing(100, 100, 50, this);
        obstacle.setPath("img/icon.png");
        addObstacle(obstacle);
        this.l = l;

        Thing enemy = new Thing(500, 20, 20, 2, 25, 50, this);
        enemy.setPath("img/icon.png");
        addEnemy(enemy);

        Thing enemy2 = new Thing(600, 20, 20, 3, 25, 50, this);
        enemy2.setPath("img/icon.png");
        addEnemy(enemy2);

        Thing enemy3 = new Thing(800, 20, 20, 3, 25, 50, this);
        enemy3.setPath("img/icon.png");
        addEnemy(enemy3);

        Thing enemy4 = new Thing(1000, 20, 20, 3, 25, 50, this);
        enemy4.setPath("img/icon.png");
        addEnemy(enemy4);

        Item item = new Item(200, 600, 70, "HealthPotion", this);
        item.setPath("img/healthPotion.png");
        addItem(item);
    }

    public void addThing(Thing t) {
        things.add(t);
    }

    public void addMain(Thing t) {
        this.main = t;
    }

    public Thing getMain() {
        return main;
    }
    public void addItem(Item t) {
        items.add(t);
    }

    public void removeThing(Thing t) {
        things.remove(t);
    }

    public void removeItem(Item i) {
        items.remove(i);
    }

    public void queueRemove(Thing t) {
        l.removeThing(t);
    }

    public ImageHandler getImageHandler() {
        return l.getImageHandler();
    }

    public void kill(Thing t) {
        enemies.remove(t);
    }

    public void killButDamageMain(Thing t) {
        damageMain(t.getDamage());
        enemies.remove(t);
    }

    public void addObstacle(Thing t) {
        obstacles.add(t);
    }

    public void addEnemy(Thing t) {
        enemies.add(t);
    }

    public List<Thing> getEnemies() {
        return enemies;
    }

    public List<Item> getItems() {
        return items;
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
        newList.addAll(enemies);
        newList.addAll(things);
        newList.remove(main);
        newList.add(main);
        return newList;
    }

    public List<Thing> getObstacles() {
        return obstacles;
    }

    public void roomTick() {

    }

    public boolean canMove(int x, int y, int width, int height) {
        if (x < 0/*width*margin*/ || x > width/*width*(1-margin*2)*/ || y < 0/*height*margin*/ || y > height)/*height*(1-margin*4))*/ {
            return false;
        }
        for (Thing o : obstacles) {
            double d = Math.sqrt(Math.pow(x-o.getX(), 2) + Math.pow(y-o.getY(), 2));
            if (d <= o.getRad()) {
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

    public void projectileTick(Thing t) {
        for (Thing e : enemies) {
            double d = Math.sqrt(Math.pow(t.getX()-e.getX(), 2) + Math.pow(t.getY()-e.getY(), 2));
            if (d <= t.getHitBox() + e.getHitBox()) {
                e.damage(t.getDamage());
                queueRemove(t);
                return;
            }
        }
    }
}
