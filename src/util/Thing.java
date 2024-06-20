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
    private boolean isEnemy = false;
    private String path1;
    private String path2;
    private Image img;
    private boolean initiated = false;
    private boolean isDirectional = false;
    private Image leftImg;
    private Image rightImg;
    private int health = 100;
    private int dmg;
    

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
            currRoom.queueRemove(this);
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
        if (!initiated) {
            updateImage(w, h);
            if (isDirectional) {
                initiateDirectional(w, h);
            }
            initiated = true;
        }
        return img;
    }

    public void updateImage(int w, int h) {
        img = scaleImage(w, h);
    }

    private void initiateDirectional(int w, int h) {
        setPath(path1);
        leftImg = scaleImage(w, h);
        setPath(path2);
        rightImg = scaleImage(w, h);
    }

    public void updateImages(int w, int h) {
        leftImg = scaleImage(w, h);
        rightImg = scaleImage(w, h);
    }
  
    private Image scaleImage(int w, int h) {
        try {
            Image tempImg = ImageIO.read(new File(getPath()));
            int originalWidth = tempImg.getWidth(null);
            int originalHeight = tempImg.getHeight(null);
            double widthScale = (double) w / originalWidth;
            double heightScale = (double) h / originalHeight;
            double scale = Math.min(widthScale, heightScale);
            int scaledWidth = (int) (originalWidth * scale);
            int scaledHeight = (int) (originalHeight * scale);
            tempImg = tempImg.getScaledInstance(scaledWidth/15, scaledHeight/15, Image.SCALE_SMOOTH);
            return tempImg;
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
        if (isDirectional) {
            updateImages(width, height);
        }
        else {
            updateImage(width, height);
        }
    }

    public void tick(int w, int h) {
        if (isEnemy) {
            enemyTick(currRoom.getMainX(), currRoom.getMainY());
        } else {
            if (currRoom.canMove(x + vx, y + vy, w, h)) {
                x += vx;
                y += vy;
            }
            else {
                currRoom.queueRemove(this);
            }
        }
    }

    private void enemyTick(int xMain, int yMain) {
        int dx = xMain - x;
        int dy = yMain - y;
        double s = Math.sqrt(dx*dx + dy*dy);
        if (s <= hitBox) {
            currRoom.queueRemove(this);
        }
        dx = (int)(dx*speed/s);
        dy = (int)(dy*speed/s);
        this.x += dx;
        this.y += dy;
        rotation = Math.toDegrees(Math.atan2(dy, dx));
    }
}
