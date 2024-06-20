package util;

import java.io.IOException;
import java.awt.Image;

public class Thing {
    private int x = 0;
    private int y = 0;
    private double vx = 0;
    private double vy = 0;
    private String path = "img/icon.png";
    private int speed;
    private Room currRoom;
    private int hitBox;
    private boolean isFriendly;
    private int rad;
    private double rotation = 0.0;
    private int originalSpeed;
    private boolean isEnemy = false;
    private String path1;
    private String path2;
    private Image img;
    private boolean initiated = false;
    private boolean isDirectional = false;
    private Image leftImg;
    private Image rightImg;
    private int health = 101;
    private int dmg;
    private boolean isMain = false;
    

    public Thing(int x, int y, int hitBox, boolean isFriendly, Room r) {
        this.x = x;
        this.y = y;
        this.hitBox = hitBox;
        this.isFriendly = isFriendly;
        currRoom = r;
    }

    public Thing(int xfrom, int yfrom, int xto, int yto, int speed, boolean isFriendly, Room r) {
        this.x = xfrom;
        this.y = yfrom;
        double dx = xto - xfrom;
        double dy = yto - yfrom;
        double length = Math.sqrt(dx * dx + dy * dy);
        this.vx = (dx / length) * speed;
        this.vy = (dy / length) * speed;
        rotation = Math.toDegrees(Math.atan2(vy, vx));
        currRoom = r;
    }

    public Thing(int x, int y, int rad, Room r) {
        this.x = x;
        this.y = y;
        this.rad = rad;
        currRoom = r;
    }

    public Thing (int x, int y, int hitBox, int speed, int dmg, int hp, Room r) {
        this.x = x;
        this.y = y;
        this.hitBox = hitBox;
        this.speed = speed;
        this.originalSpeed = speed;
        currRoom = r;
        isEnemy = true;
        health = hp;
        this.dmg = dmg;
    }

    public void setDirectional() {
        isDirectional = true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRad() {
        return rad;
    }

    public int getHitBox() {
        return hitBox;
    }

    public double getRotation() {
        return rotation;
    }

    public boolean isFriendly() {
        return isFriendly;
    }

    public void setMain() {
        setDirectional();
        isMain = true;
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

    public void damage(int dmg) {
        health -= dmg;
        if (health <= 0) {
            if (isMain) {
                currRoom.gameOver();
            }
            else {
                currRoom.queueRemove(this);
            }
        }
    }

    public int getDamage() {
        return dmg;
    }

    public int getHealth() {
        return health;
    }

    public void heal(int h) {
        health += h;
    }

    public Image getImg(int w, int h) {
        try {
            return currRoom.getImageHandler().getImage(path, w, h);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void faceLeft(boolean left) {
        if (left) {
            path = path1;
            img = leftImg;
        } else {
            path = path2;
            img = rightImg;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPaths(String path1, String path2) {
        this.path1 = path1;
        this.path2 = path2;
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
        this.originalSpeed = speed;
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public void resize(int prevWidth, int prevHeight, int width, int height) {
        x = x * width / prevWidth;
        y = y * height / prevHeight;
        speed = originalSpeed * width / 1700;
        currRoom.getImageHandler().resize(width, height);
    }

    public void tick(int w, int h) {
        if (isEnemy) {
            enemyTick(currRoom.getMainX(), currRoom.getMainY());
        } else {
            if (currRoom.canMove(x + ((int)vx), y + ((int)vy), w, h)) {
                x += vx;
                y += vy;
            }
            else {
                currRoom.queueRemove(this);
            }
        }
    }

    private void enemyTick(int xMain, int yMain) {
        double dx = xMain - x;
        double dy = yMain - y;
        double s = Math.sqrt(dx*dx + dy*dy);
        if (s <= hitBox) {
            currRoom.queueRemove(this);
        }
        rotation = Math.toDegrees(Math.atan2(dy, dx));
        double lerpFactor = speed / s;
        dx = lerp(x, xMain, lerpFactor);
        dy = lerp(y, yMain, lerpFactor);
        this.x = (int) dx;
        this.y = (int) dy;
    }
    
    private double lerp(double start, double end, double t) {
        return start + t * (end - start);
    }
}
