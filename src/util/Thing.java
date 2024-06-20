package util;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.Image;

public class Thing {
    private int x = 0;
    private int y = 0;
    private int vx = 0;
    private int vy = 0;
    private String path = "img/icon.png";
    private int speed;
    private Room currRoom;
    private int hitBox;
    private boolean isFriendly;
    private int rad;
    private double rotation = 0.0;
    private int originalSpeed;
    

    public Thing(int x, int y, int hitBox, boolean isFriendly, Room r) {
        this.x = x;
        this.y = y;
        this.hitBox = hitBox;
        this.isFriendly = isFriendly;
        currRoom = r;
    }

    public Thing(int x, int y, int vx, int vy, boolean isFriendly, Room r) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        rotation = Math.toDegrees(Math.atan2(vy, vx));
        currRoom = r;
    }

    public Thing(int x, int y, int rad, Room r) {
        this.x = x;
        this.y = y;
        this.rad = rad;
        currRoom = r;
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

    public Image getImg(int w, int h) {
        try {
            Image img = ImageIO.read(new File(getPath()));
            int originalWidth = img.getWidth(null);
            int originalHeight = img.getHeight(null);
            double widthScale = (double) w / originalWidth;
            double heightScale = (double) h / originalHeight;
            double scale = Math.min(widthScale, heightScale);
            int scaledWidth = (int) (originalWidth * scale);
            int scaledHeight = (int) (originalHeight * scale);
            Image sImg = img.getScaledInstance(scaledWidth/15, scaledHeight/15, Image.SCALE_SMOOTH);
            return sImg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
        this.originalSpeed = speed;
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public void resize(int prevWidth, int prevHeight, int width, int height) {
        x = x * width / prevWidth;
        y = y * height / prevHeight;
        speed = originalSpeed * width / 1700;
    }

    public void tick(int w, int h) {
        if (currRoom.canMove(x + vx, y + vy, w, h)) {
            x += vx;
            y += vy;
        }
        else {
            currRoom.queueRemove(this);
        }
    }
}
