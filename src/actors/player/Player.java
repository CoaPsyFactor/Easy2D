package actors.player;

import actors.Weapon;
import actors.enemy.EnemyRobot;
import actors.movable.MoveDirection;
import com.sun.javafx.geom.Vec3d;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import controllers.GameController;
import controllers.GameLoopController.Layer;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import lib.gameactor.GameActor;
import lib.sprite.Sprite;
import lib.sprite.SpriteAnimation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public class Player extends PlayerActor implements MediaDisposer.Disposable {

    private final ArrayList<Weapon> weapons = new ArrayList<>(3);
    private final HashMap<MoveDirection, SpriteAnimation> currentAnimationSet = new HashMap<>(4);
    private double safePosX = 0;
    private double safePosY = 0;
    private boolean colliding = false;
    private Weapon currentWeapon;

    private double playerSpeed = 1;

    private AudioClip footsteps = GameController.soundController.getAudioClip("character/footsteps_low.mp3");

    public Player() {
        currentAnimationSet.put(MoveDirection.UP, walkUpAnimation);

        currentAnimationSet.put(MoveDirection.LEFT, walkLeftAnimation);

        currentAnimationSet.put(MoveDirection.DOWN, walkDownAnimation);

        currentAnimationSet.put(MoveDirection.RIGHT, walkRightAnimation);
    }

    @Override
    public void onCollision(GameActor target) {
        setFreeze(target.getTag().equals("collision_block") || target.getTag().equals("enemy_robot"));

        colliding = isFreezed();
    }

    @Override
    public void render(double delta) {

        setFreeze(colliding);

        if (colliding) {
            moveTo(safePosX, safePosY);

            colliding = false;

            return;
        }

        safePosX = this.getPositionX();

        safePosY = this.getPositionY();

        if (isMoving()) {
            sprite.animate();

            if (false == footsteps.isPlaying()) {
                footsteps.play();
            }
        }

        super.render(delta);
    }

    @Override
    public void onBegin() {
        setDirectionalAnimations(currentAnimationSet);

        sprite.setAnimationSpeed(85);
    }

    @Override
    public void onEnd() {
        weapons.clear();

        currentAnimationSet.clear();

        currentWeapon = null;

        if (footsteps.isPlaying()) {
            footsteps.stop();
        }
    }

    @Override
    public Layer getLayer() {
        return Layer.SECOND;
    }

    @Override
    public String getTag() {
        return "player";
    }

    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public boolean onKeyPressed(GameActor currentActor, KeyEvent event) {
        switch (event.getCode()) {
            case L:
                EnemyRobot r = (EnemyRobot) GameController.coreController.getCurrentLevel().getProperty("enemy_robot_two");

                r.changeDirection(new Vec3d(1, 0, 1));

                break;
            case R:
                if (null != currentWeapon) {
                    currentWeapon.reload();
                }

                break;
            case SPACE:
                isShooting(true);

                break;
            case DIGIT1:
                if (false == isShooting) {
                    setActiveWeapon(0);
                }

                break;
            case DIGIT2:
                if (false == isShooting) {
                    setActiveWeapon(1);
                }

                break;
            case DIGIT3:
                if (false == isShooting) {
                    setActiveWeapon(2);
                }

                break;
            case DIGIT0:
                if (false == isShooting) {
                    setActiveWeapon(-1);
                }

                break;
            case W:

                moveVector.set(moveVector.x, -1, playerSpeed);

                changeDirection(moveVector);

                break;
            case S:

                moveVector.set(moveVector.x, 1, playerSpeed);

                changeDirection(moveVector);

                break;
            case A:

                moveVector.set(-1, moveVector.y, playerSpeed);

                changeDirection(moveVector);//-1, moveVector.y, 1, currentAnimationSet.get(ANIMATION_LEFT));

                break;
            case D:

                moveVector.set(1, moveVector.y, playerSpeed);

                changeDirection(moveVector);//1, moveVector.y, 1, currentAnimationSet.get(ANIMATION_RIGHT));

                break;
        }

        return false;
    }

    @Override
    public boolean onKeyReleased(GameActor currentActor, KeyEvent event) {

        switch (event.getCode()) {
            case SPACE:
                isShooting(false);

                break;
            case W:
            case S:

                moveVector.y = 0;

                if (moveVector.x == 0) {
                    moveVector.z = 0;
                }

                changeDirection(moveVector);

                break;
            case A:
            case D:

                moveVector.x = 0;

                if (moveVector.y == 0) {
                    moveVector.z = 0;
                }

                changeDirection(moveVector);

                break;
        }

        if (null != currentWeapon) {
            currentWeapon.setAnimationToDirection(getMoveDirection());
        }

        if (false == isMoving()) {
            footsteps.stop();

            if (null != currentWeapon) {
                currentWeapon.getSprite().clearAnimation();
            }
        }

        return false;
    }

    @Override
    public boolean onMousePressed(MouseEvent event) {
        return false;
    }

    @Override
    public boolean onMouseReleased(MouseEvent event) {
        return false;
    }

    @Override
    public boolean onMouseClicked(MouseEvent event) {
        return false;
    }

    @Override
    public boolean onMouseMoved(MouseEvent event) {

        return false;
    }

    public void isShooting(boolean value) {
        isShooting = value;

        if (null != currentWeapon) {
            currentWeapon.isShooting(value);
        }
    }

    public boolean isMoving() {
        return 0 != moveVector.z;
    }

    public void addWeapon(Weapon weapon) {
        if (weapons.contains(weapon)) {
            return;
        }

        weapon.setParent(this);

        weapons.add(weapon);
    }

    public Weapon getActiveWeapon() {
        return currentWeapon;
    }

    public void setActiveWeapon(int index) {

        if (null != currentWeapon) {
            currentWeapon.isShooting(false);
        }

        currentWeapon = null;

        weapons.forEach(weapon -> weapon.setIsActive(false));

        if (index != -1) {
            currentAnimationSet.replace(MoveDirection.UP, shootUpAnimation);

            currentAnimationSet.replace(MoveDirection.LEFT, shootLeftAnimation);

            currentAnimationSet.replace(MoveDirection.DOWN, shootDownAnimation);

            currentAnimationSet.replace(MoveDirection.RIGHT, shootRightAnimation);

            currentWeapon = weapons.get(index);

            currentWeapon.getShootSprite().clearAnimation();
        } else {
            currentAnimationSet.replace(MoveDirection.UP, walkUpAnimation);

            currentAnimationSet.replace(MoveDirection.LEFT, walkLeftAnimation);

            currentAnimationSet.replace(MoveDirection.DOWN, walkDownAnimation);

            currentAnimationSet.replace(MoveDirection.RIGHT, walkRightAnimation);
        }

        sprite.setSpriteAnimation(currentAnimationSet.get(getMoveDirection()));

        if (null != currentWeapon) {

            currentWeapon.setPosition(getPositionX(), getPositionY());

            currentWeapon.setIsActive(true);
        }

        sprite.clearAnimation();
    }

    @Override
    public void dispose() {
        currentAnimationSet.clear();
    }
}
