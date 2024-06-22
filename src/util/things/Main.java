package util.things;

import util.Room;

public class Main extends Thing{
    private int speed;
    private int originalSpeed;
    private int hp;
    private int maxHealth;
    private String path1;
    private String path2;
    public Main(int hitBox, int speed, int hp, String path1, String path2, Room r) {
        super(0, 0, hitBox, path1, r);
        this.speed = speed;
        this.originalSpeed = speed;
        this.hp = hp;
        maxHealth = hp;
        this.hp = hp;
        this.path1 = path1;
        this.path2 = path2;
    }

    public void moveX(int x, int w, int h) {
        if (x < this.x) {
            super.path = path1;
        } else {
            super.path = path2;
        }
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

    public int getSpeed() {
        return speed;
    }

    public void damage(int dmg) {
        hp -= dmg;
        if (hp <= 0) {
            currRoom.gameOver();
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

    public void resize(int prevWidth, int prevHeight, int width, int height) {
        super.resize(prevWidth, prevHeight, width, height);
        speed = originalSpeed * width / 1700;
    }
}
