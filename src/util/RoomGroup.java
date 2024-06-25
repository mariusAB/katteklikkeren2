package util;

import java.util.ArrayList;
import java.util.List;

public class RoomGroup {
    private List<RoomContainer> containedRooms;
    private List<RoomContainer> rooms;
    private boolean connected = false;
    private RoomDistributer roomDistributer;
    private int currRoom;
    private int dir;
    private boolean inactive = false;
    private List<RoomGroup> otherGroups;

    public RoomGroup(RoomContainer buttonRoom, List<RoomContainer> rooms, RoomDistributer distributer) {
        containedRooms = new ArrayList<>();
        containedRooms.add(buttonRoom);
        this.roomDistributer = distributer;
        currRoom = buttonRoom.getRoomIndex();
        this.rooms = rooms;
        findDir();
    }

    private void findDir() {
        if (rooms.get(0).getAbove() != -1) {
            dir = 0;
        } else if (rooms.get(0).getRight() != -1) {
            dir = 1;
        } else if (rooms.get(0).getBelow() != -1) {
            dir = 2;
        } else {
            dir = 3;
        } 
    }

    public void addContainedRooms(List<RoomContainer> containedRooms) {
        this.containedRooms.addAll(containedRooms);
    }

    public List<RoomContainer> getContainedRooms() {
        return containedRooms;
    }

    private void deactivate(RoomGroup otherGroup) {
        inactive = true;
        otherGroup.addContainedRooms(containedRooms);
        containedRooms.clear();
    }

    public boolean isConnected() {
        return connected;
    }

    private void connectAll() {
        for (RoomContainer room : containedRooms) {
            room.setConnected();
        }
    }

    private RoomContainer getRoom(int index, int dir) {
        if (dir == 0 && rooms.get(index).getAbove() != -1) {
            return rooms.get(rooms.get(index).getAbove());
        } else if (dir == 1 && rooms.get(index).getRight() != -1) {
            return rooms.get(rooms.get(index).getRight());
        } else if (dir == 2 && rooms.get(index).getBelow() != -1) {
            return rooms.get(rooms.get(index).getBelow());
        } else if (dir == 3 && rooms.get(index).getLeft() != -1) {
            return rooms.get(rooms.get(index).getLeft());
        }
        return null;
    }

    private void randomizeDir() {
        dir = ((int) (Math.random() * 4)) % 4;
    }

    private RoomGroup isInOtherGroups(RoomContainer rc) {
        for (RoomGroup rg : otherGroups) {
            if (rg.getContainedRooms().contains(rc)) {
                return rg;
            }
        }
        return null;
    }

    public void addButtonGroups(List<RoomGroup> otherGroups) {
        this.otherGroups = otherGroups;
    }

    public void expand() {
        if (!inactive) {
            RoomContainer nextRoom = getRoom(currRoom, dir);
            if (nextRoom != null) {
                RoomGroup otherGroup = isInOtherGroups(nextRoom);
                if (otherGroup != null) {
                    deactivate(otherGroup);
                }
                if (!nextRoom.hasRoom()) {
                    nextRoom.setRoom(roomDistributer.getNormalRoom());
                }
                currRoom = nextRoom.getRoomIndex();
                containedRooms.add(nextRoom);
                if (nextRoom.isConnected()) {
                    connectAll();
                    connected = true;
                }
            }
            randomizeDir();
        }
    }
}
