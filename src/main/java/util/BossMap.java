package util;

import java.util.ArrayList;

public class BossMap extends Map{
    private int totalButtons = 0;
    
    public BossMap(Logic logic) {
        super(logic, 1, 3, 0, true);
        initializeMap();
        setTotalButtons();
        logic.setMaxButtons(totalButtons);
    }

    private void setTotalButtons() {
        for (RoomContainer r : rooms) {
            if (r.getRoom() != null) {
                totalButtons += r.getRoom().getTotalButtons();
            }
        }
    }
    
    private void initializeMap() {
        super.rooms = new ArrayList<>();
        initRooms();
        generateStartRoom();
        generateBossRoom();
        visitedRooms.add(rooms.get(currRoom));
        updateVisibleRooms();
    }

    private void generateStartRoom() {
        super.rooms.get(height-1).setRoom(super.roomDistributer.getStartRoom(true));
        currRoom = height-1;
    }

    private void generateBossRoom() {
        super.rooms.get(height-2).setRoom(super.roomDistributer.getBossRoom(0));
        super.rooms.get(height-3).setRoom(super.roomDistributer.getBossRoom(1));
    }
}
