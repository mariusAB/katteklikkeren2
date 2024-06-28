package things;

import java.io.IOException;
import java.lang.ref.WeakReference;

import util.Room;

import java.awt.Image;

public class Thing {
    protected int x = 0;
    protected int y = 0;
    protected String path;
    protected WeakReference<Room> currRoom;
    protected int hitBox;

    public Thing(int x, int y, int hitBox, String path, Room r) {
        this.x = x;
        this.y = y;
        this.hitBox = hitBox;
        this.currRoom = new WeakReference<>(r);
        this.path = path;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRoomWidth() {
        return currRoom.get().getWidth();
    }

    public int getRoomHeight() {
        return currRoom.get().getHeight();
    }

    public int getHitBox() {
        return hitBox;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Image getImg(int w, int h) {
        try {
            if (currRoom.get() != null) {
                return currRoom.get().getImageHandler().getImage(path, w, h);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String getPath() {
        return path;
    }

    public double getRotation() {
        return 0;
    }
}
