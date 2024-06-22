package util;
import java.util.ArrayList;
import java.util.List;

import util.things.Enemy;
import util.things.Item;
import util.things.Main;
import util.things.Obstacle;
import util.things.Projectile;
import util.things.Thing;

public class Room {
    private List<Obstacle> obstacles = new ArrayList<>();
    private List<Projectile> projectiles = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private List<Item> items = new ArrayList<>();
    private double margin = 0.03;
    private Logic l;
    private Main main;
    private int i = 0;

    public Room(Logic l) {
        Obstacle obstacle = new Obstacle(100, 100, 50, "img/icon.png", this);
        addObstacle(obstacle);
        this.l = l;

        Enemy enemy = new Enemy(500, 20, 20, 2, 25, 50, "img/icon.png", this);
        addEnemy(enemy);

        Enemy enemy2 = new Enemy(600, 20, 20, 3, 25, 50, "img/icon.png", this);
        addEnemy(enemy2);

        Enemy enemy3 = new Enemy(800, 20, 20, 3, 25, 50, "img/icon.png", this);
        addEnemy(enemy3);

        Enemy enemy4 = new Enemy(1000, 20, 20, 3, 25, 50, "img/icon.png", this);
        addEnemy(enemy4);

        Item item = new Item(200, 600, 70, "HealthPotion", "img/healthPotion.png", this);
        addItem(item);
    }

    public void addProjectile(Projectile p) {
        projectiles.add(p);
    }

    public void addMain(Main m) {
        this.main = m;
    }

    public Main getMain() {
        return main;
    }
    public void addItem(Item t) {
        items.add(t);
    }

    public void removeProjectile(Projectile p) {
        projectiles.remove(p);
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

    public void killButDamageMain(Enemy e) {
        damageMain(e.getDamage());
        enemies.remove(e);
    }

    public void addObstacle(Obstacle o) {
        obstacles.add(o);
    }

    public void addEnemy(Enemy e) {
        enemies.add(e);
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
        newList.addAll(projectiles);
        newList.add(main);
        return newList;
    }



    public void tick() {
        i++;
    }

    public int getRoomTick() {
        return i;
    }

    public boolean canMove(int x, int y, int width, int height) {
        if (x < 0/*width*margin*/ || x > width/*width*(1-margin*2)*/ || y < 0/*height*margin*/ || y > height)/*height*(1-margin*4))*/ {
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

    public void projectileTick(Projectile p) {
        for (Enemy e : enemies) {
            double d = Math.sqrt(Math.pow(p.getX()-e.getX(), 2) + Math.pow(p.getY()-e.getY(), 2));
            if (d <= p.getHitBox() + e.getHitBox()) {
                e.damage(p.getDamage());
                queueRemove(p);
                return;
            }
        }
    }
}
