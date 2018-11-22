package actors.debug;

import controllers.GameLoopController;
import lib.gameactor.GameActor;
import lib.sprite.Sprite;
import lib.sprite.SpriteSheet;

/**
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public class Block extends GameActor {
    private Sprite sprite;

    public Block(int id) {
        sprite = SpriteSheet.getSpriteSheet("debug.png", 16, 16).getNewSprite(id);

        setSale(4, 4);
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    public GameLoopController.Layer getLayer() {
        return GameLoopController.Layer.GUI;
    }

    @Override
    public String getTag() {
        return "debug_block";
    }
}
