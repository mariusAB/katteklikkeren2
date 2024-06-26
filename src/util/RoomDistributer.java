package util;

public class RoomDistributer {
    private RoomCreator roomCreator;
    public RoomDistributer(int width, int height, Logic logic) {
        roomCreator = new RoomCreator(logic.getWidth(), logic.getHeight(), logic);
    }

    public Room getStartRoom() {
        return roomCreator.getRoom("start", null);
    }

    public Room getButtonRoom() {
        return roomCreator.getRoom("button", null);
    }

    public Room getNormalRoom() {
        return roomCreator.getRoom("normal", null);
    }
}
