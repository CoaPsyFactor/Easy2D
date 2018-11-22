package actors.map;

import controllers.GameLoopController;
import lib.gameactor.GameActor;
import lib.sprite.Sprite;

/**
 * Created by azivanovic on 9/11/16.
 */
public class MapActor extends GameActor {

    private double randomId;

    private Sprite sprite;

    private GameLoopController.Layer layer;

    public MapActor(Sprite _sprite) {
        sprite = _sprite;

        randomId = Math.random();
    }

    @Override
    public String getTag() {
        return "map_tile_" + randomId;
    }

    @Override
    public void setLayer(GameLoopController.Layer l) {
        layer = l;
    }

    @Override
    public GameLoopController.Layer getLayer() {
        return layer;
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }
}
