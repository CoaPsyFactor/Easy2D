package actors.enemy;

import actors.Bullet;
import actors.Weapon;
import actors.gui.palyer.HealthBar;
import actors.movable.MoveDirection;
import actors.weapons.AssaultRifle;
import com.sun.javafx.geom.Vec3d;
import controllers.GameController;
import controllers.GameLoopController;
import lib.gameactor.GameActor;
import lib.gameactor.GameActorCollider;
import lib.gameactor.GameActorColliderInterface;
import lib.sprite.Sprite;
import lib.sprite.SpriteAnimation;
import lib.sprite.SpriteSheet;

import java.util.HashMap;

/**
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public class EnemyRobot extends EnemyAI implements GameActorColliderInterface {

    public static final double DEFAULT_HEALTH = 100;

    private HealthBar healthBar;

    private GameActorCollider collider = new GameActorCollider(64, 64, 0, 0);

    private final Sprite sprite = SpriteSheet.getSpriteSheet("enemy/ArmorShoot.png", 64, 64).getNewSprite(1);

    private final SpriteAnimation deathAnimation = new SpriteAnimation(SpriteSheet.getSpriteSheet("enemy/ArmorDeath.png", 64, 64), new int[]{1, 2, 3, 4, 5, 6});

    private double maxHealth = EnemyRobot.DEFAULT_HEALTH;

    private double health = EnemyRobot.DEFAULT_HEALTH;

    private GameLoopController.Layer layer = GameLoopController.Layer.SECOND;

    private final HashMap<MoveDirection, SpriteAnimation> walkAnimations = new HashMap<>(4);

    private Weapon weapon = new AssaultRifle("enemy_rifle");

    public EnemyRobot() {
        weapon.setParent(this);

        SpriteSheet walkSpriteSheet = SpriteSheet.getNewSpriteSheet("enemy/ArmorShoot.png", 64, 64);

        walkAnimations.put(MoveDirection.UP, new SpriteAnimation(walkSpriteSheet, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}));

        walkAnimations.put(MoveDirection.LEFT, new SpriteAnimation(walkSpriteSheet, new int[]{10, 11, 12, 13, 14, 15, 16, 17, 18}));

        walkAnimations.put(MoveDirection.DOWN, new SpriteAnimation(walkSpriteSheet, new int[]{19, 20, 21, 22, 23, 24, 25, 26, 27}));

        walkAnimations.put(MoveDirection.RIGHT, new SpriteAnimation(walkSpriteSheet, new int[]{28, 29, 30, 31, 32, 33, 34, 35, 36}));

        weapon.setIsActive(true);

        setDirectionalAnimations(walkAnimations);

        GameController.coreController.addActor(weapon);
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public String getTag() {
        return "enemy_robot";
    }

    @Override
    public double getSenseRange() {
        return 10;
    }

    @Override
    public void onCollision(GameActor target) {
        if (target.getTag().startsWith("bullet_player")) {

            health -= ((Bullet) target).getWeaponDamage();

            healthBar.updateHealthBar(health, maxHealth);

            GameController.coreController.removeActor(target);

            if (health <= 0) {

                changeDirection(new Vec3d(0, 0, 0));

                getSprite().setSpriteAnimation(deathAnimation);

                getSprite().clearAnimation();

                setLayer(GameLoopController.Layer.FIRST);

                collider = null;

                weapon.delete();

                healthBar.delete();
            }
        }
    }

    @Override
    public void onBegin() {
        healthBar = new HealthBar(this);

        healthBar.updateHealthBar(health, maxHealth);

        healthBar.setIsActive(true);

        GameController.coreController.addActor(healthBar);
    }

    @Override
    public void onEnd() {
        walkAnimations.forEach((direction, spriteAnimation) -> {
            spriteAnimation.dispose();
        });

        walkAnimations.clear();

        sprite.dispose();

        healthBar.dispose();

        weapon.dispose();
    }

    @Override
    public void render(double delta) {
        if (
                null != getSprite() && getSprite().getSpriteAnimation() == deathAnimation &&
                        false == getSprite().getSpriteAnimation().isLastFrame()
                ) {
            getSprite().animate();
        }

        super.render(delta);
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
    public GameActorCollider getCollider() {
        return collider;
    }

    public void setHealth(double amount) {
        health = amount;
    }

    public double getHealth() {
        return health;
    }
}
