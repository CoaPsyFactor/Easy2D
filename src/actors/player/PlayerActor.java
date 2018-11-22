package actors.player;

import actors.gui.palyer.HealthBar;
import actors.movable.MovableActor;
import com.sun.javafx.geom.Vec3d;
import controllers.GameController;
import lib.gameactor.*;
import lib.sprite.Sprite;
import lib.sprite.SpriteAnimation;
import lib.sprite.SpriteSheet;

/**
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public abstract class PlayerActor extends MovableActor implements GameActorColliderInterface, GameActorKeyboardListener, GameActorMouseListener {

    protected HealthBar healthBar;

    protected double maxHealth = 100;

    protected double health = maxHealth;

    public PlayerActor() {
        healthBar = new HealthBar(this);

        healthBar.updateHealthBar(health, maxHealth);

        healthBar.setIsActive(true);

        GameController.coreController.addActor(healthBar);
    }

    // Walk Animation
    protected final SpriteSheet walkSpriteSheet = SpriteSheet.getNewSpriteSheet("character/AgentWalk.png", 64, 64);
    protected final SpriteAnimation walkUpAnimation = new SpriteAnimation(walkSpriteSheet, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
    protected final SpriteAnimation walkLeftAnimation = new SpriteAnimation(walkSpriteSheet, new int[]{10, 11, 12, 13, 14, 15, 16, 17, 18});
    protected final SpriteAnimation walkDownAnimation = new SpriteAnimation(walkSpriteSheet, new int[]{19, 20, 21, 22, 23, 24, 25, 26, 27});
    protected final SpriteAnimation walkRightAnimation = new SpriteAnimation(walkSpriteSheet, new int[]{28, 29, 30, 31, 32, 33, 34, 35, 36});
    protected final SpriteSheet shootSpriteSheet = SpriteSheet.getNewSpriteSheet("character/AgentShoot.png", 64, 64);

    // Shoot Animation
    protected final SpriteAnimation shootUpAnimation = new SpriteAnimation(shootSpriteSheet, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
    protected final SpriteAnimation shootLeftAnimation = new SpriteAnimation(shootSpriteSheet, new int[]{10, 11, 12, 13, 14, 15, 16, 17, 18});
    protected final SpriteAnimation shootDownAnimation = new SpriteAnimation(shootSpriteSheet, new int[]{19, 20, 21, 22, 23, 24, 25, 26, 27});
    protected final SpriteAnimation shootRightAnimation = new SpriteAnimation(shootSpriteSheet, new int[]{28, 29, 30, 31, 32, 33, 34, 35, 36});
    protected final Vec3d moveVector = new Vec3d();
    protected GameActorCollider collider = new GameActorCollider(48, 5, 8, 32);
    protected Sprite sprite = walkSpriteSheet.getSprite(1);
    protected boolean isActive = true;
    protected boolean isShooting = false;

    @Override
    public GameActorCollider getCollider() {
        return collider;
    }
}
