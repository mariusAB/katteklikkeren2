package util;

public class RoomContainer {
    private Room r;
    private int index;
    private int width;
    private int height;

    RoomContainer(int index, int width, int height) {
        this.index = index;
        this.width = width;
        this.height = height;
    }

    public void setRoom(Room room) {
        this.r = room;
    }

    public Room getRoom() {
        return r;
    }

    public int getAbove() {
        if (index - width >= 0) {
            return index - width;
        }
        return -1;
    }

    public int getRight() {
        if (index % width != width - 1) {
            return index + 1;
        }
        return -1;
    }

    public int getBelow() {
        if (index + width < width * height) {
            return index + width;
        }
        return -1;
    }

    public int getLeft() {
        if (index % width != 0) {
            return index - 1;
        }
        return -1;
    }

    public boolean hasRoom() {
        return r != null;
    }

    public int getRoomIndex() {
        return index;
    }
}
