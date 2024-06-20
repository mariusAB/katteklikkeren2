package util;
public class Obstacle extends Thing{
    private int r;

    public Obstacle(int x, int y, int r) {
        super(x, y, 0);
        this.r = r;
    }

    public int getR() {
        return r;
    }

    public void Tick(int w, int h) {
        return;
    }
}