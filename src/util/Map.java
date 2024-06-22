package util;

public class Map {
    private Logic l;
    public Map(Logic l) {
        this.l = l;
    }

    public Room getCurrRoom() {
        return new Room(l);
    }
}
