package things;

import util.Room;

public class Button extends Thing{
    private String pathDown;
    private Boolean clicked = false;
    public Button(int x, int y, int hitBox, String pathUp, String pathDown, Room room) {
        super(x, y, hitBox, pathUp, room);
        this.pathDown = pathDown;
    }

    private void click() {
        super.path = pathDown;
        currRoom.buttonClicked();
    }

    public void tick() {
        if (!clicked) {
            Main main = currRoom.getMain();
            double d = Math.sqrt(Math.pow(main.getX() - x, 2) + Math.pow(main.getY() - y, 2));
            if (d < hitBox) {
                click();
                clicked = true;
            }
        }
    }
}
