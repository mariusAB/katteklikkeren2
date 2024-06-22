package util.things;

import util.Room;
import weapons.Weapon;

public class WeaponItem extends Item{
    private Weapon w;
    public WeaponItem(int x, int y, int hitBox, Weapon w, Room r) {
        super(x, y, hitBox, "Weapon", w.getPath(), r);
        this.w = w;
    }

    public void interact() {
        super.currRoom.addThing(new WeaponItem(super.x, super.y, super.hitBox, super.currRoom.getLogic().getWeapon(), super.currRoom));
        super.currRoom.getLogic().setWeapon(w);
        w.equip();
        super.currRoom.removeThing(this);
    }
}
