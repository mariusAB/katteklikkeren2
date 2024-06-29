package util;


public class RoomDistributer {
    private RoomCreator roomCreator;
    private int itemChance = 50;
    public RoomDistributer(int width, int height, Logic logic) {
        roomCreator = new RoomCreator(logic.getWidth(), logic.getHeight(), logic);
    }

    public Room getBossRoom(int i) {
        if (i == 0) {
            return roomCreator.getRoom("boss", "preBoss");
        }
        else if (i == 1) {
            return roomCreator.getRoom("boss", "buttonBoss");
        }
        return null;
    }

    public Room getStartRoom() {
        return roomCreator.getRoom("start", null);
    }

    public Room getButtonRoom() {
        double rand = Math.random() * 100;
        if (rand < itemChance*2) { // TODO: fiks
            if (rand <= itemChance/2) {
                return roomCreator.getRoom("button", "healthPotion");
            } else {
                return roomCreator.getRoom("button", "weapon");
            }
        }
        return roomCreator.getRoom("button", null);
    }

    public Room getNormalRoom() {
        double rand = Math.random() * 100;
        if (rand < itemChance) {
            if (rand <= itemChance/2) {
                return roomCreator.getRoom("normal", "healthPotion");
            } else {
                return roomCreator.getRoom("normal", "weapon");
            }
        }
        return roomCreator.getRoom("normal", "null");
    }
}
