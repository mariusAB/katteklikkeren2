package weapons;

import util.Room;

public class Weapon {
    protected Room r;
    protected String path1;
    protected String path2;
    public Weapon(Room r) {
        this.r = r;
    }
    public void use(int xfrom, int yfrom, int xto, int yto) {
    }

    public void equip() {
        r.getMain().setPaths(path1, path2);
    }
}
