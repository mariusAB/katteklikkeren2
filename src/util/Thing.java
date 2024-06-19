package util;
public class Thing {
    private int x = 0;
    private int y = 0;
    private int vx = 0;
    private int vy = 0;
    private String path = "img/icon.png";
    private int speed;
    private Room currRoom = new Room();
    private int hitBox;
    private boolean isFriendly;
    private Logic l;

    public Thing(int x, int y, int hitBox, boolean isFriendly, Logic l) {
        this.x = x;
        this.y = y;
        this.hitBox = hitBox;
        this.isFriendly = isFriendly;
        this.l = l;
    }

    public Thing(int x, int y, int vx, int vy, boolean isFriendly, Logic l) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.l = l;
    }

    public void setCurrRoom(Room c) {
        this.currRoom = c;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHitBox() {
        return hitBox;
    }

    public boolean isFriendly() {
        return isFriendly;
    }

    public void moveX(int x, int w, int h) {
        for (int i = 0; i < speed; i++) {
            if (currRoom.canMove(this.x + x - i, y, w, h)) {
                this.x += x - i;
                break;
            }
        }
    }
    
    public void moveY(int y, int w, int h) {
        for (int i = 0; i < speed; i++) {
            if (currRoom.canMove(x, this.y + y - i, w, h)) {
                this.y += y - i;
                break;
            }
        }
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public void resize(int prevWidth, int prevHeight, int width, int height) {
        x = x * width / prevWidth;
        y = y * height / prevHeight;
        speed = speed * width / prevWidth;
    }

    public void tick(int w, int h) {
        if (currRoom.canMove(x + vx, y + vy, w, h)) {
            x += vx;
            y += vy;
        }
        else {
            l.removeThing(this);
        }
    }
}
