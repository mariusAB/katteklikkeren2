package util;

import util.things.Item;

public class RoomDistributer {
    private Logic logic;
    public RoomDistributer(int width, int height, Logic logic) {
        this.logic = logic;
    }

    public Room getStartRoom() {
        return new Room(logic);
    }

    public Room getButtonRoom() {
        Room broom = new Room(logic);
        Item item = new Item(2000, 4000, 300, "HealthPotion", "img/healthPotion.png", broom);
        broom.addThing(item);
        return broom;
    }

    public Room getNormalRoom() {
        return new Room(logic);
    }
}
