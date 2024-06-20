package util;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private List<Obstacle> obstacles = new ArrayList<>();
    private List<Thing> things = new ArrayList<>();
    private double margin = 0.03;
    private Logic l;

    public Room(Logic l) {
        Obstacle o = new Obstacle(100, 100, 50);
        this.l = l;
        addObstacle(o);
        o.setPath("img/icon.png");
        l.addThing(o);
    }

    public void addObstacle(Obstacle o) {
        obstacles.add(o);
    }

    public boolean canMove(int x, int y, int width, int height) {
        if (x < width*margin || x > width*(1-margin*2) || y < height*margin || y > height*(1-margin*4)) {
            return false;
        }
        for (Obstacle o : obstacles) {
            double d = Math.sqrt(Math.pow(x-o.getX(), 2) + Math.pow(y-o.getY(), 2));
            if (d <= o.getR()) {
                return false;
            }
        }
        return true;
    }
}
