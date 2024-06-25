package util.things;

import util.Room;

public class Obstacle extends Thing{
    private boolean isRound = true;
    public Obstacle(int x, int y, int rad, Boolean isRound, String path, Room r) {
        super(x, y, rad, path, r);
        this.isRound = isRound;
    }

    public boolean isRound() {
        return isRound;
    }
}
