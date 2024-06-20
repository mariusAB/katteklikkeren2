package util;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private List<Thing> obstacles = new ArrayList<>();
    private List<Thing> things = new ArrayList<>();
    private List<Thing> enemies = new ArrayList<>();
    private double margin = 0.03;
    private Logic l;

    public Room(Logic l) {
        Thing obstacle = new Thing(100, 100, 50, this);
        obstacle.setPath("img/icon.png");
        addObstacle(obstacle);
        this.l = l;

        Thing enemy = new Thing(500, 20, 20, 2, 25, 50, this);
        enemy.setPath("img/icon.png");
        addEnemy(enemy);

        Thing enemy2 = new Thing(600, 20, 20, 5, 25, 50, this);
        enemy2.setPath("img/icon.png");
        addEnemy(enemy2);

        Thing enemy3 = new Thing(800, 20, 20, 5, 25, 50, this);
        enemy3.setPath("img/icon.png");
        addEnemy(enemy3);

        Thing enemy4 = new Thing(1000, 20, 20, 5, 25, 50, this);
        enemy4.setPath("img/icon.png");
        addEnemy(enemy4);
    }

    public void addThing(Thing t) {
        things.add(t);
    }

    public void removeThing(Thing t) {
        things.remove(t);
    }

    public void queueRemove(Thing t) {
        l.removeThing(t);
    }

    public void kill(Thing t) {
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
        List<Thing> newList = new ArrayList<>(things);
        newList.addAll(enemies);
        return newList;
    }

    public List<Thing> getObstacles() {
        return obstacles;
    }

    public boolean canMove(int x, int y, int width, int height) {
        if (x < width*margin || x > width*(1-margin*2) || y < height*margin || y > height*(1-margin*4)) {
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
}
