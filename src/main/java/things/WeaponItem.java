package things;

import util.Room;
import weapons.Weapon;

public class WeaponItem extends Item{
    private Weapon w;
    public WeaponItem(int x, int y, int hitBox, Weapon w, Room r) {
        super(x, y, hitBox, "Weapon", w.getPath(), r);
        this.w = w;
    }

    public void interact() {
        super.currRoom.get().addThing(new WeaponItem(super.x, super.y, super.hitBox, super.currRoom.get().getLogic().getWeapon(), super.currRoom.get()));
        super.currRoom.get().getLogic().setWeapon(w);
        w.equip();
        super.currRoom.get().removeThing(this);
        super.hitBox = 300;
    }
}
