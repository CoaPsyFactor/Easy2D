package actors.movable;

import com.sun.javafx.geom.Vec3d;
import lib.gameactor.GameActor;
import lib.sprite.SpriteAnimation;

import java.util.HashMap;

/**
 * Created by azivanovic on 9/6/16.
 */
public abstract class MovableActor extends GameActor {

    private boolean freeze = false;

    private final Vec3d moveVector = new Vec3d();

    private MoveDirection currentDirection = MoveDirection.UP;

    private MoveDirection animationDirection = MoveDirection.UP;

    protected HashMap<MoveDirection, SpriteAnimation> directionalAnimations;

    protected boolean isMoving = false;

    public void setDirectionalAnimations(HashMap<MoveDirection, SpriteAnimation> animations) {
        directionalAnimations = animations;
    }

    public boolean isFreezed() {
        return freeze;
    }

    public void setFreeze(boolean freezed) {
        freeze = freezed;
    }

    public void changeDirection(Vec3d moveData) {

        moveVector.set(moveData.x, moveData.y, (moveData.z * 64) / 1000);

        double x = moveVector.x;

        double y = moveVector.y;

        isMoving = moveVector.z != 0 && false == freeze;

        if (x < 0 && y < 0) {
            currentDirection = MoveDirection.UP_LEFT;
        } else if (x < 0 && y == 0) {
            currentDirection = MoveDirection.LEFT;
        } else if (x == 0 && y < 0) {
            currentDirection = MoveDirection.UP;
        } else if (x > 0 && y < 0) {
            currentDirection = MoveDirection.UP_RIGHT;
        } else if (x > 0 && y == 0) {
            currentDirection = MoveDirection.RIGHT;
        } else if (x > 0 && y > 0) {
            currentDirection = MoveDirection.DOWN_RIGHT;
        } else if (x == 0 && y > 0) {
            currentDirection = MoveDirection.DOWN;
        } else if (x < 0 && y > 0) {
            currentDirection = MoveDirection.DOWN_LEFT;
        }

        setAnimationToDirection(currentDirection);
    }

    public void setAnimationToDirection(MoveDirection direction) {
        if (null == getSprite()) {
            return;
        }

        animationDirection = direction;

        getSprite().clearAnimation();

        if (getSprite().getSpriteAnimation() == directionalAnimations.get(animationDirection)) {
            return;
        }

        getSprite().setSpriteAnimation(directionalAnimations.get(animationDirection));
    }

    public MoveDirection getAnimationDirection() {
        return animationDirection;
    }

    public MoveDirection getMoveDirection() {
        return currentDirection;
    }

    public boolean isMoving() {
        return isMoving;
    }

    double move = 0;

    @Override
    public void render(double delta) {

        if (false == isMoving || freeze) {
            return;
        }

        if (null != getSprite() && null != getSprite().getSpriteAnimation()) {
            getSprite().animate();
        }

        moveFor(moveVector.x * moveVector.z * delta, moveVector.y * moveVector.z * delta);
    }
}
