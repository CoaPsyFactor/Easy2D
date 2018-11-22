package actors;

import com.sun.javafx.geom.Vec2d;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import controllers.GameLoopController.Layer;
import lib.gameactor.GameActor;
import lib.gameactor.GameActorCollider;
import lib.gameactor.GameActorColliderInterface;
import lib.sprite.Sprite;
import lib.sprite.SpriteAnimation;
import lib.sprite.SpriteSheet;

/**
 * Created by azivanovic on 7/20/16.
 */
public class Bullet extends GameActor implements GameActorColliderInterface, MediaDisposer.Disposable {

    private final SpriteSheet spriteSheet = SpriteSheet.getSpriteSheet("weapons/bullet.png", 16, 16);

    private final Sprite sprite = spriteSheet.getNewSprite(1);

    private final GameActorCollider collider = new GameActorCollider(16, 16, 0, 0);

    private Weapon parent;

    private String bulletTag;

    private final Vec2d direction = new Vec2d();

    public Bullet(Weapon parentWeapon) {
        parent = parentWeapon;

        Vec2d startPosition = new Vec2d();

        startPosition.set(new Vec2d(parent.getPositionX(), parent.getPositionY()));

        setPosition(startPosition.x + 32, startPosition.y + 32);

        SpriteAnimation spriteAnimation = new SpriteAnimation(spriteSheet, new int[]{1, 2, 3, 4, 5, 6});

        sprite.setSpriteAnimation(spriteAnimation);

        bulletTag = "bullet_" + parentWeapon.getParent().getTag() + Math.random();
    }

    public void setDirection(Vec2d dir) {
        direction.set(dir);
    }

    public double getWeaponDamage() {
        return parent.getDamage();
    }

    @Override
    public void onCollision(GameActor target) {
    }

    @Override
    public GameActorCollider getCollider() {
        return collider;
    }

    @Override
    public void render(double delta) {
        setPosition(getPositionX() + direction.x, getPositionY() + direction.y);

        sprite.animate();
    }

    @Override
    public void onEnd() {
        dispose();
    }

    @Override
    public Layer getLayer() {
        return Layer.TOP;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public String getTag() {
        return "bullet_" + parent.getParent().getTag();
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public void dispose() {
        sprite.dispose();
    }
}
