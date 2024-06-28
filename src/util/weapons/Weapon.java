package util.weapons;

import util.Room;

public class Weapon {
    protected Room r;
    protected String path;
    protected String path1;
    protected String path2;
    public Weapon(Room r) {
        this.r = r;
    }
    public void use(int xfrom, int yfrom, int xto, int yto) {
    }

    public String getPath() {
        return path;
    }

    public void setRoom(Room r) {
        this.r = r;
    }

    public void equip() {
        r.getMain().setPaths(path1, path2);
    }

    public void reset() {
    }
}
