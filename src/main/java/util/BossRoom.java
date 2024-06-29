package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import things.Button;
import things.ButtonEnemy;
import things.Door;
import things.Enemy;
import things.Item;
import things.Obstacle;
import things.Portal;
import things.Projectile;
import things.SwordSwipe;
import things.DamageZone;
import things.LightningStrike;
import things.Thing;
import things.WeaponItem;

public class BossRoom extends Room{

    private List<ButtonEnemy> buttonEnemies = new ArrayList<>();
    private List<ButtonEnemy> canBeUsedButtonEnemies = new ArrayList<>();
    private int stageTime = 1000;

    public BossRoom(Logic logic, int stageTime) {
        super(logic);
        this.stageTime = stageTime;
    }

    public void tick() {
        updateCanBeUsedButtonEnemies();
        if (enemies.size() == 0 && canBeUsedButtonEnemies.size() == 0) {
            l.displayMiniMap();
            openDoors();
        }

        for (Door d : doors) {
            d.tick();
        }
        i++;
        Iterator<Thing> iterator = getThings().iterator();
        while (iterator.hasNext()) {
            Thing t = iterator.next();
            if (t instanceof ButtonEnemy) {
                ((ButtonEnemy) t).tick();
            } else if (t instanceof Enemy) {
                ((Enemy) t).tick();
            } else if (t instanceof Projectile) {
                ((Projectile) t).tick();
            }
            else if (t instanceof SwordSwipe) {
                ((SwordSwipe) t).tick();
            }
            else if (t instanceof DamageZone) {
                ((DamageZone) t).tick();
            }
            else if (t instanceof LightningStrike) {
                ((LightningStrike) t).tick();
            }
            else if (t instanceof Button) {
                ((Button) t).tick();
            }
            else if (t instanceof Portal) {
                ((Portal) t).tick();
            }
        }
        if (i%stageTime == 0) {
            activateButtons();
        }
    }

    private void updateCanBeUsedButtonEnemies() {
        canBeUsedButtonEnemies = new ArrayList<>();
        for (ButtonEnemy b : buttonEnemies) {
            if (b.canBeActivated()) {
                canBeUsedButtonEnemies.add(b);
            }
        }
    }

    public void addThing(Thing t) {
        if (t instanceof Projectile) {
            projectiles.add((Projectile) t);
        } else if (t instanceof Item) {
            items.add((Item) t);
        } else if (t instanceof SwordSwipe) {
            swordswipes.add((SwordSwipe) t);
        } else if (t instanceof DamageZone) {
            damagezones.add((DamageZone) t);
        } else if (t instanceof LightningStrike) {
            lightningstrikes.add((LightningStrike) t);
        } else if (t instanceof ButtonEnemy) {
            buttonEnemies.add((ButtonEnemy) t);
        } else if (t instanceof Enemy) {
            enemies.add((Enemy) t);
        } else if (t instanceof Obstacle) {
            obstacles.add((Obstacle) t);
        } else if (t instanceof WeaponItem) {
            items.add((WeaponItem) t);
        } else if (t instanceof Button) {
            buttons.add((Button) t);
        } else if (t instanceof Portal) {
            portals.add((Portal) t);
        }
    }

    public List<Thing> getThings() {
        List<Thing> newList = new ArrayList<>();
        newList.addAll(portals);
        newList.addAll(items);
        newList.addAll(obstacles);
        newList.addAll(buttons);
        newList.addAll(buttonEnemies);
        newList.addAll(enemies);
        newList.addAll(swordswipes);
        newList.addAll(damagezones);
        newList.addAll(lightningstrikes);
        newList.addAll(projectiles);
        newList.add(main);
        return newList;
    }

    public List<Enemy> getEnemies() {
        List<Enemy> combEnemies = new ArrayList<>(enemies);
        combEnemies.addAll(buttonEnemies);
        return combEnemies;
    }

    public List<Enemy> getEnemiesToAvoid() {
        return enemies; 
    }

    public void activateNext() {
        updateCanBeUsedButtonEnemies();
        stageTime = i + stageTime;
        activateButtons();
    }

    private void activateButtons() {
        if (canBeUsedButtonEnemies.size() > 0) {
            int randomInt = (int) (Math.random() * canBeUsedButtonEnemies.size());
            for (ButtonEnemy b : buttonEnemies) {
                b.deActivate();
            }
            canBeUsedButtonEnemies.get(randomInt).activate();
        }
    }

    public boolean canMove(int x, int y, Thing t) {
        if (x < width*margin || x > width*(1-margin) || y < height*margin || y > height*(1-margin*2)) {
            return false;
        }
        for (Obstacle o : obstacles) {
            if (o.isRound()) {
                double d = Math.sqrt(Math.pow(x-o.getX(), 2) + Math.pow(y-o.getY(), 2));
                if (d <= o.getHitBox()) {
                    return false;
                }
            } else {
                if (x < o.getX() + o.getHitBox() && x > o.getX() - o.getHitBox() && y < o.getY() + o.getHitBox() && y > o.getY() - o.getHitBox()) {
                    return false;
                }
            }
        }
        if (!(t instanceof Projectile)) {
            for (ButtonEnemy b : buttonEnemies) {
                double d = Math.sqrt(Math.pow(x-b.getX(), 2) + Math.pow(y-b.getY(), 2));
                if (d <= b.getRadius()) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getTotalButtons() {
        return buttonEnemies.size();
    }
}
