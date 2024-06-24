package util.things;

import java.lang.ref.WeakReference;

import util.Room;

public class Main extends Thing{
    private int speed;
    private int hp;
    private int maxHealth;
    private String path1;
    private String path2;
    private boolean dirLeft = false;

    public Main(int hitBox, int speed, int hp, String path1, String path2, Room r) {
        super(0, 0, hitBox, path1, r);
        this.speed = speed;
        this.hp = hp;
        maxHealth = hp;
        this.hp = hp;
        this.path1 = path1;
        this.path2 = path2;
    }

    public void moveX(int x) {
        if (x < 0) {
            super.path = path1;
            dirLeft = true;
        } else {
            super.path = path2;
            dirLeft = false;
        }
        for (int i = 0; i < speed; i++) {
            if (currRoom.get().canMove(this.x + x - i, y)) {
                this.x += x - i;
                break;
            }
        }
    }
    
    public void moveY(int y) {
        for (int i = 0; i < speed; i++) {
            if (currRoom.get().canMove(x, this.y + y - i)) {
                this.y += y - i;
                break;
            }
        }
    }

    public void setRoom(Room r) {
        super.currRoom = new WeakReference<>(r);
    }

    public int getSpeed() {
        return speed;
    }

    public void setPaths(String path1, String path2) {
        this.path1 = path1;
        this.path2 = path2;
        if (dirLeft) {
            super.path = path1;
        } else {
            super.path = path2;
        }
    }

    public void damage(int dmg) {
        hp -= dmg;
        if (hp <= 0) {
            currRoom.get().gameOver();
        }
    }

    public void heal(int h) {
        hp += h;
        if (hp > maxHealth) {
            hp = maxHealth;
        }
    }

    public int getHealth() {
        return hp;
    }

    public int getMaxHealth() {
        return maxHealth;
    }
}
