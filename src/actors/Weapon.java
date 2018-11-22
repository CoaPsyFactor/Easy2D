package actors;

import actors.gui.palyer.AttachGUI;
import actors.gui.palyer.LowAmmo;
import actors.movable.MovableActor;
import actors.movable.MoveDirection;
import actors.player.Player;
import com.sun.javafx.geom.Vec2d;
import controllers.GameController;
import controllers.GameLoopController.Layer;
import javafx.scene.canvas.Canvas;
import javafx.scene.media.AudioClip;
import lib.gameactor.GameActor;
import lib.sprite.Sprite;
import lib.sprite.SpriteAnimation;
import lib.sprite.SpriteSheet;

import java.util.HashMap;

/**
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public abstract class Weapon extends MovableActor {

    public final int ANIMATION_UP = 0;

    public final int ANIMATION_LEFT = 1;

    public final int ANIMATION_DOWN = 2;

    public final int ANIMATION_RIGHT = 3;

    public SpriteAnimation upShoot;
    public SpriteAnimation leftShoot;
    public SpriteAnimation downShoot;
    public SpriteAnimation rightShoot;
    private Sprite weaponSprite;
    private Sprite shootSprite;
    private MovableActor parent;
    private FireType fireType;
    private boolean isShooting;
    private Layer layer = Layer.TOP;
    private String weaponTag;
    private Double weaponRange = 100.0;
    private int fireRate = 1;
    private double cooldown = 0;
    private int ammo = 0;
    private int maxAmmo = 0;
    private double clipAmmo = 0;
    private double clipSize = 0;
    private AudioClip gunShoot;
    private double damage = 0;
    private double reloadTime = 0;
    private double noAmmoAnimation = 0;
    private double lowAmmoAnimation = 0;
    private double reloading = 0;
    private final HashMap<MoveDirection, SpriteAnimation> shootSprites = new HashMap<>(4);

    protected Weapon(String tag, String imagePath, String shootPath, FireType _fireType) {
        weaponTag = tag;

        fireType = _fireType;

        SpriteSheet weaponSpriteSheet = SpriteSheet.getNewSpriteSheet(imagePath, 64, 64);

        weaponSprite = weaponSpriteSheet.getSprite(1);

        SpriteSheet shootSpriteSheet = SpriteSheet.getNewSpriteSheet(shootPath, 64, 64);

        shootSprite = shootSpriteSheet.getSprite(1);

        HashMap<MoveDirection, SpriteAnimation> animations = new HashMap<>(4);

        int[][] animationFrames = new int[][]{
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {10, 11, 12, 13, 14, 15, 16, 17, 18},
                {19, 20, 21, 22, 23, 24, 25, 26, 27},
                {28, 29, 30, 31, 32, 33, 34, 35, 36}
        };

        animations.put(MoveDirection.LEFT, new SpriteAnimation(weaponSpriteSheet, animationFrames[ANIMATION_LEFT]));

        animations.put(MoveDirection.UP, new SpriteAnimation(weaponSpriteSheet, animationFrames[ANIMATION_UP]));

        animations.put(MoveDirection.RIGHT, new SpriteAnimation(weaponSpriteSheet, animationFrames[ANIMATION_RIGHT]));

        animations.put(MoveDirection.DOWN, new SpriteAnimation(weaponSpriteSheet, animationFrames[ANIMATION_DOWN]));

        setDirectionalAnimations(animations);

        shootSprites.put(MoveDirection.LEFT, new SpriteAnimation(shootSpriteSheet, animationFrames[ANIMATION_LEFT]));

        shootSprites.put(MoveDirection.UP, new SpriteAnimation(shootSpriteSheet, animationFrames[ANIMATION_UP]));

        shootSprites.put(MoveDirection.RIGHT, new SpriteAnimation(shootSpriteSheet, animationFrames[ANIMATION_RIGHT]));

        shootSprites.put(MoveDirection.DOWN, new SpriteAnimation(shootSpriteSheet, animationFrames[ANIMATION_DOWN]));
    }

    private final HashMap<String, AttachGUI> weaponGUIItems = new HashMap<>();

    public void addGUIItem(String identifier, AttachGUI item) {
        if (weaponGUIItems.containsKey(identifier)) {
            weaponGUIItems.replace(identifier, item);
        } else {
            weaponGUIItems.put(identifier, item);
        }
    }

    public AttachGUI getVisibleGUIItem(String identifier) {
        if (weaponGUIItems.containsKey(identifier) && weaponGUIItems.get(identifier).isActive()) {
            return weaponGUIItems.get(identifier);
        }

        return null;
    }

    @Override
    public void render(double delta) {
        if (null == parent || null == weaponSprite) {
            return;
        }

        if (getAnimationDirection() != parent.getAnimationDirection()) {
            setAnimationToDirection(parent.getAnimationDirection());
        }

        if (null != shootSprite && shootSprites.get(getAnimationDirection()) != shootSprite.getSpriteAnimation()) {
            shootSprite.setSpriteAnimation(shootSprites.get(getAnimationDirection()));
        }

        AttachGUI guiItem = getVisibleGUIItem("low_ammo");

        lowAmmoAnimation -= delta;

        if (null != guiItem && lowAmmoAnimation <= 0) {
            lowAmmoAnimation = 0;

            guiItem.setIsActive(false);
        }

        guiItem = getVisibleGUIItem("no_ammo");

        noAmmoAnimation -= delta;

        if (null != guiItem && noAmmoAnimation <= 0) {
            noAmmoAnimation = 0;

            guiItem.setIsActive(false);
        }

        guiItem = getVisibleGUIItem("reloading");

        reloading -= delta;

        if (reloading > 0) {
            isShooting = false;
        } else if (null != guiItem) {
            guiItem.setIsActive(false);
        }

        if (parent.isMoving()) {

            getSprite().animate();

            setPosition(parent.getPositionX(), parent.getPositionY());
        } else if (null != parent.getSprite() && null != parent.getSprite().getSpriteAnimation()) {
            weaponSprite.clearAnimation();
        }

        if (isShooting && null != shootSprite.getSpriteAnimation() && shootSprite.getSpriteAnimation().isLastFrame() && fireType == FireType.SINGLE) {
            isShooting(false);

            shootSprite.getSpriteAnimation().resetAnimation();
        }

        shoot(delta);
    }

    private boolean shoot(double delta) {
        if (false == isShooting) {
            return false;
        }

        cooldown -= delta;

        if (cooldown > 0) {
            return false;
        }

        if (clipAmmo <= 0) {

            if (ammo == 0) {
                noAmmoAnimation = 500;

                GameController.coreController.getCurrentLevel().getProperty("gui_noammo").setIsActive(true);
            }

            clipAmmo = 0;

            isShooting = false;

            return false;
        }

        clipAmmo--;

        if ((clipAmmo / clipSize) * 100 <= 25) {
            lowAmmoAnimation = 500;

            GameController.coreController.getCurrentLevel().getProperty("gui_lowammo").setIsActive(true);
        }

        cooldown = 1000 / fireRate;

        makeShootSound();

        spawnBullet(getAnimationDirection());

        return true;
    }

    private double rotationDelta = 0;

    private void spawnBullet(MoveDirection direction) {

        switch (direction) {
            case UP_LEFT:
            case UP_RIGHT:
            case DOWN_LEFT:
            case DOWN_RIGHT:
                if (parent instanceof Player) {
                    ((Player) parent).isShooting(false);
                }


                return;
        }

        Bullet bullet = new Bullet(this);

        switch (direction) {
            case LEFT:

                bullet.setDirection(new Vec2d(-29, 0));

                bullet.setPosition(bullet.getPositionX() - 32, bullet.getPositionY());

                break;
            case RIGHT:

                bullet.setDirection(new Vec2d(29, 0));

                bullet.setPosition(bullet.getPositionX() + 32, bullet.getPositionY());

                break;
            case UP:

                bullet.setDirection(new Vec2d(0, -29));

                bullet.setPosition(bullet.getPositionX() + 8, bullet.getPositionY() - 20);

                break;
            case DOWN:
                bullet.setDirection(new Vec2d(0, 29));

                bullet.setPosition(bullet.getPositionX() - 20, bullet.getPositionY());

                break;
        }

        GameController.coreController.addActor(bullet);
    }

    public void setDamage(double amount) {
        damage = amount;
    }

    public double getDamage() {
        return damage;
    }

    public boolean reload(boolean silent) {
        if (reloading > 0 || clipAmmo == clipSize) {
            return false;
        }

        if (ammo <= 0) {
            ammo = 0;

            noAmmoAnimation = 500;

            GameController.coreController.getCurrentLevel().getProperty("gui_noammo").setIsActive(true);

            return false;
        }

        double amount = clipSize - clipAmmo;

        if (amount > ammo) {
            amount = ammo;
        }

        clipAmmo += amount;

        ammo -= amount;

        reloading = reloadTime;

        if (false == silent) {
            GameController.coreController.getCurrentLevel().getProperty("gui_reloading").setIsActive(true);
        }

        return true;
    }

    public boolean reload() {
        return reload(false);
    }

    public void setReloadTime(double time) {
        reloadTime = time;
    }

    public double getReloadTime() {
        return reloadTime;
    }

    public void setFireRate(int rate) {
        fireRate = rate;
    }

    public int getFireRate() {
        return fireRate;
    }

    public void setAmmo(int amount) {
        ammo = amount;
    }

    public void addAmmo(int amount) {
        ammo += amount;

        if (getTotalAmmo() > maxAmmo) {
            ammo -= getTotalAmmo() - maxAmmo;
        }
    }

    public int getAmmo() {
        return ammo;
    }

    public void setMaxAmmo(int amount) {
        maxAmmo = amount;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public void setClipSize(int amount) {
        clipSize = amount;
    }

    public int getClipSize() {
        return (int) clipSize;
    }

    public int getTotalAmmo() {
        return (int) (ammo + clipAmmo);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (isShooting && null != shootSprite) {
            shootSprite.animate();

            shootSprite.setPosition(parent.getPositionX(), parent.getPositionY());

            shootSprite.draw(canvas);
        }
    }

    public GameActor getParent() {
        return parent;
    }

    @Override
    public void onEnd() {

        if (null != shootSprite) {
            shootSprite.dispose();

            shootSprite = null;
        }

        if (null != weaponSprite) {
            weaponSprite.dispose();

            weaponSprite = null;
        }

        if (null != upShoot) {
            upShoot.dispose();

            upShoot = null;
        }

        if (null != leftShoot) {
            leftShoot.dispose();

            leftShoot = null;
        }

        if (null != downShoot) {
            downShoot.dispose();

            downShoot = null;
        }

        if (null != rightShoot) {
            rightShoot.dispose();

            rightShoot = null;
        }
    }

    @Override
    public Layer getLayer() {
        return layer;
    }

    @Override
    public void setLayer(Layer _layer) {
        layer = _layer;
    }

    @Override
    public String getTag() {
        return weaponTag;
    }

    public Sprite getSprite() {
        return weaponSprite;
    }

    public void setSprite(Sprite weaponSprite) {
        return;
    }

    public Sprite getShootSprite() {
        return shootSprite;
    }

    public void isShooting(boolean value) {
        isShooting = value;
    }

    private void makeShootSound() {
        if (null == gunShoot) {
            gunShoot = getShootSound();
        }

        if (null != gunShoot) {
            gunShoot.play();
        }
    }

    public void setParent(MovableActor player) {
        parent = player;
    }

    public void setWeaponRange(double range) {
        weaponRange = range;
    }

    public double getWeaponRange() {
        return weaponRange;
    }

    public abstract AudioClip getShootSound();

    public enum FireType {
        NONE,
        SINGLE,
        AUTO
    }
}
