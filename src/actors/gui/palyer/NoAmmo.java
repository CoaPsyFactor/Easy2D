package actors.gui.palyer;

import lib.gameactor.GameActor;
import lib.sprite.Sprite;
import lib.sprite.SpriteSheet;

/**
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public class NoAmmo extends AttachGUI {
    private final SpriteSheet spriteSheet = SpriteSheet.getSpriteSheet("gui/noammo.png", 64, 16);

    public NoAmmo(GameActor parent) {
        super(0, -5, parent);
    }

    @Override
    public Sprite getSprite() {
        return spriteSheet.getSprite(1);
    }

    @Override
    public String getTag() {
        return "gui_noammo";
    }
}
