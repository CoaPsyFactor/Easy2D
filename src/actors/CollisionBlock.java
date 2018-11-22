package actors;

import controllers.GameLoopController.Layer;
import lib.gameactor.GameActor;
import lib.gameactor.GameActorCollider;
import lib.gameactor.GameActorColliderInterface;

/**
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public class CollisionBlock extends GameActor implements GameActorColliderInterface {

    private GameActorCollider collider;

    public CollisionBlock(double x, double y, int width, int height) {
        collider = new GameActorCollider(width, height, 0, 0);

        collider.setPosition(x, y);
    }

    @Override
    public void onCollision(GameActor target) {
    }

    @Override
    public GameActorCollider getCollider() {
        return collider;
    }

    @Override
    public void onEnd() {
        collider = null;
    }

    @Override
    public Layer getLayer() {
        return Layer.BASE;
    }

    @Override
    public String getTag() {
        return "collision_block";
    }
}
