package util;

public class RoomContainer {
    public Room r;
    RoomContainer() {
    }

    public void setRoom(Room r) {
        this.r = r;
    }

    public Room getRoom() {
        return r;
    }

    public boolean hasRoom() {
        return r != null;
    }
}
