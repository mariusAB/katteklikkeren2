package util;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private List<Obstacle> obstacles = new ArrayList<>();
    private double margin = 0.03;

    public Room() {
    }

    public void addObstacle(Obstacle o) {
        obstacles.add(o);
    }

    public boolean canMove(int x, int y, int width, int height) {
        if (x < width*margin || x > width*(1-margin*2) || y < height*margin || y > height*(1-margin*4)) {
            return false;
        }
        for (Obstacle o : obstacles) {
            double d = Math.sqrt(Math.pow(x-o.x(), 2) + Math.pow(y-o.y(), 2));
            if (d <= o.r()) {
                return false;
            }
        }
        return true;
    }
}
