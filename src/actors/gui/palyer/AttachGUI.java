package actors.gui.palyer;

import lib.gameactor.GameActor;

/**
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public abstract class AttachGUI extends GameActor {

    private double offsetX;

    private double offsetY;

    private GameActor parent;

    public AttachGUI(double x, double y, GameActor parentActor) {
        offsetX = x;
        offsetY = y;

        parent = parentActor;
    }

    @Override
    public void render(double delta) {
        if (null != parent) {
            setIsActive(parent.isActive());

            setPosition(parent.getPositionX() + offsetX, parent.getPositionY() + offsetY);
        }
    }
}