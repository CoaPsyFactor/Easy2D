package actors.weapons;

import actors.Weapon;
import controllers.GameController;
import javafx.scene.media.AudioClip;

/**
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public class AssaultRifle extends Weapon {

    private final static String imagePath = "weapons/AssaultRifle.png";

    private final static String shootPath = "weapons/Muzzleflash.png";

    private final static String shootSoundPath = "weapon/assault_low.mp3";

    private final AudioClip shoot = GameController.soundController.getAudioClip(shootSoundPath);

    public AssaultRifle(String tag) {
        super(tag, imagePath, shootPath, FireType.AUTO);

        getShootSprite().setAnimationSpeed(85);

        setFireRate(10);

        setMaxAmmo(120);

        setClipSize(30);

        setAmmo(120);

        setDamage(5);

        reload(true);

        setReloadTime(1000);
    }

    @Override
    public AudioClip getShootSound() {
        return shoot;
    }
}
