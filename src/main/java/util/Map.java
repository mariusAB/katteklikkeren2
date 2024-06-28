package util;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class Map {
    private Logic logic;
    protected int currRoom ;
    protected List<RoomContainer> rooms;
    private List<RoomContainer> edgeRooms = new ArrayList<>();
    private List<RoomGroup> buttonGroups = new ArrayList<>();
    protected Set<RoomContainer> visitedRooms = new HashSet<>();
    protected Set<RoomContainer> visibleRooms = new HashSet<>();
    protected RoomDistributer roomDistributer;
    protected int width;
    protected int height;
    private int buttonRooms = 3;

    public Map(Logic logic, int width, int height, int buttonRooms, boolean isBossMap) {
        this.logic = logic;
        this.width = width;
        this.height = height;
        roomDistributer = new RoomDistributer(width, height, logic);
        if (!isBossMap) {
            this.buttonRooms = buttonRooms;
            if (width < 3 || height < 3) {
                throw new IllegalArgumentException("Width and height must be at least 3");
            }
            initializeMap();
            visitedRooms.add(rooms.get(currRoom));
            updateVisibleRooms();
        }
    }

    private void initializeMap() {
        rooms = new ArrayList<>();
        edgeRooms.clear();
        buttonGroups.clear();
        visitedRooms.clear();
        visibleRooms.clear();
        initRooms();
        setUpEdgeRooms();
        generateStartRoom();
        generateButtonRoomsOnEdges();
        generateMap();
        if (getFilledRooms() < width * height / 2 || getFilledRooms() > width * height / 1.5 || getSqueezedRooms() > width*height/(width*height*0.6)){
            rooms.clear();
            edgeRooms.clear();
            buttonGroups.clear();
            initializeMap();
        }
    }

    private int getFilledRooms() {
        int amount = 0;
        for (RoomContainer rc : rooms) {
            if (rc.hasRoom()) {
                amount++;
            }
        }
        return amount;
    }

    private int getSqueezedRooms() {
        int amount = 0;
        for (RoomContainer rc : rooms) {
            if (rc.hasRoom()) {
                if (rc.getAbove() != -1 && rooms.get(rc.getAbove()).hasRoom()
                && rc.getRight() != -1 && rooms.get(rc.getRight()).hasRoom()
                && rc.getBelow() != -1 && rooms.get(rc.getBelow()).hasRoom()
                && rc.getLeft() != -1 && rooms.get(rc.getLeft()).hasRoom()) {
                    amount ++;
                }
            }
        }
        return amount;
    }

    private void setUpEdgeRooms() {
        for (int i = 0; i < width*height; i++) {
            if (rooms.get(i).getAbove() == -1 || rooms.get(i).getRight() == -1 || rooms.get(i).getBelow() == -1 || rooms.get(i).getLeft() == -1) {
                edgeRooms.add(rooms.get(i));
            }
        }
    }

    private void generateStartRoom() {
        List<RoomContainer> middleRooms = new ArrayList<>(rooms);
        middleRooms.removeAll(edgeRooms);
        currRoom = middleRooms.get((int) (Math.random() * middleRooms.size())).getRoomIndex();
        rooms.get(currRoom).setRoom(roomDistributer.getStartRoom());
        RoomContainer up = rooms.get(rooms.get(currRoom).getAbove());
        up.cantHaveSpecialRoom();
        up.setConnected();
        RoomContainer right = rooms.get(rooms.get(currRoom).getRight());
        right.cantHaveSpecialRoom();
        right.setConnected();
        RoomContainer down = rooms.get(rooms.get(currRoom).getBelow());
        down.cantHaveSpecialRoom();
        down.setConnected();
        RoomContainer left = rooms.get(rooms.get(currRoom).getLeft());
        left.cantHaveSpecialRoom();
        left.setConnected();
    }

    protected void initRooms() {
        rooms = new ArrayList<>();
        for (int i = 0; i < width*height; i++) {
            rooms.add(new RoomContainer(i, width, height));
        }
    }

    private void generateMap() {
        int groupsNotConnected = buttonGroups.size();
        while (groupsNotConnected > 0) {
            groupsNotConnected = buttonGroups.size();
            for (RoomGroup rg : buttonGroups) {
                rg.expand();
                if (rg.isConnected()) {
                    groupsNotConnected--;
                }
            }
        }
    }

    private void generateButtonRoomsOnEdges() {
        for (int i = 0; i < buttonRooms; i++) {
            Room buttonRoom = roomDistributer.getButtonRoom();
            RoomContainer buttonRoomContainer = null;
            int edgeRoomSize = edgeRooms.size();
            int edgeRoomIndex = (int) (Math.random() * edgeRoomSize);
            if (edgeRooms.get(edgeRoomIndex).canHaveSpecialRoom()) {
                edgeRooms.get(edgeRoomIndex).setRoom(buttonRoom);
                buttonRoomContainer = edgeRooms.get(edgeRoomIndex);
                makeButtonRoomsEndRooms(buttonRoomContainer);
                buttonGroups.add(new RoomGroup(buttonRoomContainer, rooms, roomDistributer));
            } else {
                i--;
            }
        }
        for (RoomGroup g : buttonGroups) {
            List<RoomGroup> buttonGroupWithoutItself = new ArrayList<>(buttonRooms);
            buttonGroupWithoutItself.remove(g);
            g.addButtonGroups(buttonGroupWithoutItself);
        }
    }

    private void makeButtonRoomsEndRooms(RoomContainer buttonRoomContainer) {
        List<RoomContainer> adjacentRooms = new ArrayList<>();
        if (buttonRoomContainer.getAbove() != -1) {
            adjacentRooms.add(rooms.get(buttonRoomContainer.getAbove()));
        }
        if (buttonRoomContainer.getRight() != -1) {
            adjacentRooms.add(rooms.get(buttonRoomContainer.getRight()));
        }
        if (buttonRoomContainer.getBelow() != -1) {
            adjacentRooms.add(rooms.get(buttonRoomContainer.getBelow()));
        }
        if (buttonRoomContainer.getLeft() != -1) {
            adjacentRooms.add(rooms.get(buttonRoomContainer.getLeft()));
        }
        for (RoomContainer r : adjacentRooms) {
            r.cantHaveSpecialRoom();
        }
    }

    public int getMapWidth() {
        return width;
    }

    public int getMapHeight() {
        return height;
    }

    public Room getCurrRoom() {
        return rooms.get(currRoom).getRoom();
    }

    public List<Integer> getMiniMap() {
        List<Integer> miniMap = new ArrayList<Integer>();
        for (int i = 0; i < width*height; i++) {
            if (rooms.get(i).hasRoom() && i == currRoom) {
                miniMap.add(2);
            } else if (visitedRooms.contains(rooms.get(i))) {
                miniMap.add(1);
            } else if (visibleRooms.contains(rooms.get(i))) {
                miniMap.add(3);
            } else {
                miniMap.add(0);
            }
        }
        return miniMap;
    }

    public List<Boolean> getDoors() {
        List<Boolean> doors = new ArrayList<Boolean>();
        if (rooms.get(currRoom).getAbove() != -1 && rooms.get(rooms.get(currRoom).getAbove()).hasRoom()) {
            doors.add(true);
        } else {
            doors.add(false);
        }
        if (rooms.get(currRoom).getRight() != -1 && rooms.get(rooms.get(currRoom).getRight()).hasRoom()) {
            doors.add(true);
        } else {
            doors.add(false);
        }
        if (rooms.get(currRoom).getBelow() != -1 && rooms.get(rooms.get(currRoom).getBelow()).hasRoom()) {
            doors.add(true);
        } else {
            doors.add(false);
        }
        if (rooms.get(currRoom).getLeft() != -1 && rooms.get(rooms.get(currRoom).getLeft()).hasRoom()) {
            doors.add(true);
        } else {
            doors.add(false);
        }
        return doors;
    }

    public RoomContainer getRoom(int index, int dir) {
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

    protected void updateVisibleRooms() {
        RoomContainer r = rooms.get(currRoom);
        if (r.getAbove() != -1 && rooms.get(r.getAbove()).hasRoom()) {
            visibleRooms.add(rooms.get(r.getAbove()));
        }
        if (r.getRight() != -1 && rooms.get(r.getRight()).hasRoom()) {
            visibleRooms.add(rooms.get(r.getRight()));
        }
        if (r.getBelow() != -1 && rooms.get(r.getBelow()).hasRoom()) {
            visibleRooms.add(rooms.get(r.getBelow()));
        }
        if (r.getLeft() != -1 && rooms.get(r.getLeft()).hasRoom()) {
            visibleRooms.add(rooms.get(r.getLeft()));
        }
        }

    public void move(int dir) {
        currRoom = getRoom(currRoom, dir).getRoomIndex();
        visitedRooms.add(rooms.get(currRoom));
        updateVisibleRooms();
        logic.setRoom(rooms.get(currRoom).getRoom());
    }
}
