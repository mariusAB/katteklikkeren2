package util;

public class RoomDistributer {
    private RoomCreator roomCreator;
    private int itemChance = 50;
    public RoomDistributer(int width, int height, Logic logic) {
        roomCreator = new RoomCreator(logic.getWidth(), logic.getHeight(), logic);
    }

    private int getItemChance() {
        return itemChance;
    }

    public Room getStartRoom() {
        return roomCreator.getRoom("start", null);
    }

    public Room getButtonRoom() {
        return roomCreator.getRoom("button", null);
    }

    public Room getNormalRoom() {
        double rand = Math.random() * 100;
        if (rand < itemChance) {
            System.out.println(rand);
            if (rand <= itemChance/2) {
                return roomCreator.getRoom("normal", "healthPotion");
            } else {
                return roomCreator.getRoom("normal", "weapon");
            }
        }
        return roomCreator.getRoom("normal", "null");
    }
}
