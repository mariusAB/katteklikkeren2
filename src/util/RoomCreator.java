package util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import util.things.*;
import weapons.*;

public class RoomCreator {
    private int width;
    private int height;
    private Logic logic;
    private double margin;
    private List<Point> enemyPositionSlots = new ArrayList<>();
    private List<Point> obstaclePositionSlots = new ArrayList<>();
    private Point lootSlot;
    public RoomCreator(int width, int height, Logic logic) {
        margin = logic.getMargin();
        this.width = width - (int) (width*3*margin);
        this.height = height - (int) (height*4*margin);
        this.logic = logic;
        addEnemyPositionSlots();
        addObstaclePositionSlots();
    }

    public Room getRoom(String type, String item) {
        if (type.equals("normal")) {
            return getNormalRoom(item);
        }
        if (type.equals("button")) {
            return getButtonRoom(item);
        }
        if (type.equals("start")) {
            return getStartRoom();
        }
        return null;
    }

    private Room getStartRoom() {
        Room startRoom = new Room(logic);
        Portal p = new Portal((int) (width / 2 + margin*2*width), (int) (height / 2 + margin*2.5*height), 400, "img/portalClosed.png", "img/portalOpen.png", startRoom);
        startRoom.addThing(p);
        return startRoom;
    }

    private Room getButtonRoom(String item) {
        Room buttonRoom = new Room(logic);
        Button b = new Button((int) (width / 2 + margin*2*width), (int) (height / 2 + margin*2.5*height), 400, "img/buttonUp.png", "img/buttonDown.png", buttonRoom);
        buttonRoom.addThing(b);
        lootSlot = new Point((int) (width / 2 + margin*2*width), (int) (height / 2 + margin*6*height));
        if (item.equals("weapon")) {
            generateWeapon((int) lootSlot.getX(), (int) lootSlot.getY(), buttonRoom);
        }
        else if (item.equals("healthPotion")) {
            Item healthPotion = new Item((int) lootSlot.getX(), (int) lootSlot.getY(), 400, "healthPotion", "img/healthPotion.png", buttonRoom);
            buttonRoom.addThing(healthPotion);
        }
        return buttonRoom;
    }

    private Room getNormalRoom(String item) {
        Room normalRoom = new Room(logic);
        generateEnemies(normalRoom);
        generateObstacles(normalRoom);
        if (item.equals("weapon")) {
            lootSlot = new Point((int) (width / 2 + margin*2*width), (int) (height / 2 + margin*2.5*height));
            generateWeapon((int) lootSlot.getX(), (int) lootSlot.getY(), normalRoom);
        }
        if (item.equals("healthPotion")) {
            lootSlot = new Point((int) (width / 2 + margin*2*width), (int) (height / 2 + margin*2.5*height));
            Item healthPotion = new Item((int) lootSlot.getX(), (int) lootSlot.getY(), 400, "healthPotion", "img/healthPotion.png", normalRoom);
            normalRoom.addThing(healthPotion);
        }
        return normalRoom;
    }

    private Enemy getRandomEnemy(int x, int y, Room room) {
        int randomIndex = (int) (Math.random() * 10);
        Enemy enemy = null;
        if (randomIndex < 9) {
            enemy = new Enemy(x, y, 60, 15, 25, 100, "img/icon.png", room);
        }
        else if (randomIndex >= 9) {
            enemy = new EnemyLauncher(x, y, 60, 10, 80, 50, "img/icon.png", room);
        }
        return enemy;
    }

    private Obstacle getRandomObstacle(int x, int y, Room room) {
        int randomIndex = (int) (Math.random() * 10);
        Obstacle obstacle = null;
        if (randomIndex < 5) {
            obstacle = new Obstacle(x, y, 300, true, "img/icon.png", room);
        }
        if (randomIndex >= 5) {
            obstacle = new Obstacle(x, y, 300, false, "img/icon.png", room);
        }
        return obstacle;
    }

    private void addObstaclePositionSlots() {
        int widthIncrement = (int) (width / 9);
        for (int i = 1; i < 8; i++) {
            obstaclePositionSlots.add(new Point(widthIncrement * i + (int) (margin*2*width), (int) (height / 2 + margin*2.5*height)));
        }
        for (int i = 1; i < 8; i++) {
            obstaclePositionSlots.add(new Point(widthIncrement * i + (int) (margin*2*width), (int) (height / 2 - margin*2.5*height)));
        }
        for (int i = 1; i < 8; i++) {
            obstaclePositionSlots.add(new Point(widthIncrement * i + (int) (margin*2*width), (int) (height / 2 + margin*7.5*height)));
        }
    }

    private void addEnemyPositionSlots() {
        int widthIncrement = (int) (width / 6);
        int heightIncrement = (int) (height / 6);
        for (int i = 0; i < 7; i++) {
            if (i != 3) {
                enemyPositionSlots.add(new Point(widthIncrement * i + (int) (margin*2*width), height - heightIncrement * i + (int) (margin*2.5*height)));
            }
        }

        for (int i = 0; i < 7; i++) {
            if (i != 3) {
                enemyPositionSlots.add(new Point(widthIncrement * i + (int) (margin*2*width), heightIncrement * i + (int) (margin*2.5*height)));
            }
        }
    }

    private int randomizeStats(int baseStat) {
        return (int) (Math.random() * 0.8 * baseStat + baseStat * 0.6);
    }

    private void generateWeapon(int x, int y, Room room) {
        int randomIndex = (int) (Math.random() * 3);
        if (randomIndex == 0) {
            int speed = randomizeStats(70);
            int dmg = randomizeStats(50);
            int delay = randomizeStats(50);
            FireBallStaff fireBallStaff = new FireBallStaff(delay, dmg, speed, room);
            WeaponItem weaponItem = new WeaponItem(x, y, 300, fireBallStaff, room);
            room.addThing(weaponItem);
        }
        else if (randomIndex == 1) {
            int dmg = randomizeStats(30);
            int delay = randomizeStats(40);
            Sword sword = new Sword(dmg, delay, room);
            WeaponItem weaponItem = new WeaponItem(x, y, 300, sword, room);
            room.addThing(weaponItem);

        }
        else if (randomIndex == 2) {
            int speed = randomizeStats(70);
            int dmg = randomizeStats(10);
            int delay = randomizeStats(20);
            MagicStaff magicStaff = new MagicStaff(delay, dmg, speed, room);
            WeaponItem weaponItem = new WeaponItem(x, y, 300, magicStaff, room);
            room.addThing(weaponItem);
        }
    }

    private void generateEnemies(Room room) {
        int enemyAmount = (int) (Math.random() * 4) + 5;
        List<Point> enemyPositionsCopy = new ArrayList<>(enemyPositionSlots);
        List<Point> enemyPositions = new ArrayList<>();
    
        for (int i = 0; i < enemyAmount; i++) {
            int randomIndex = (int) (Math.random() * enemyPositionsCopy.size());
            enemyPositions.add(enemyPositionsCopy.get(randomIndex));
            enemyPositionsCopy.remove(randomIndex);
        }
    
        for (int i = 0; i < enemyPositions.size(); i++) {
            Point p = enemyPositions.get(i);
            Enemy enemy = getRandomEnemy((int) p.getX(), (int) p.getY(), room);
            room.addThing(enemy);
        }
    }

    private void generateObstacles(Room room) {
        int obstacleAmount = (int) (Math.random() * 6);
        List<Point> obstaclePositionsCopy = new ArrayList<>(obstaclePositionSlots);
        List<Point> obstaclePositions = new ArrayList<>();
    
        for (int i = 0; i < obstacleAmount; i++) {
            int randomIndex = (int) (Math.random() * obstaclePositionsCopy.size());
            obstaclePositions.add(obstaclePositionsCopy.get(randomIndex));
            obstaclePositionsCopy.remove(randomIndex);
        }
    
        for (int i = 0; i < obstaclePositions.size(); i++) {
            Point p = obstaclePositions.get(i);
            Obstacle obstacle = getRandomObstacle((int) p.getX(), (int) p.getY(), room);
            room.addThing(obstacle);
        }
    }
}
