package util;

public class RoomDistributer {
    private Logic logic;
    public RoomDistributer(int width, int height, Logic logic) {
        this.logic = logic;
    }

    public Room getStartRoom() {
        return new Room(logic);
    }
}
