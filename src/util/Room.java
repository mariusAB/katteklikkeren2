package util;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private List<Thing> obstacles = new ArrayList<>();
    private List<Thing> things = new ArrayList<>();
    private double margin = 0.03;
    private Logic l;

    public Room(Logic l) {
        Thing obstacle = new Thing(100, 100, 50, this);
        obstacle.setPath("img/icon.png");
        addObstacle(obstacle);
        this.l = l;
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

    public void addObstacle(Thing t) {
        obstacles.add(t);
    }

    public List<Thing> getThings() {
        return things;
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
