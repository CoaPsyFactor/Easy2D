package actors.weapons;

import actors.Weapon;
import controllers.GameController;
import javafx.scene.media.AudioClip;

/**
 * Created by azivanovic on 7/20/16.
 */
public class Shotgun extends Weapon {

    private final static String imagePath = "weapons/Shotgun.png";

    private final static String shootPath = "weapons/Muzzleflash.png";

    private final static String shootSoundPath = "weapon/shotgun_low.mp3";

    private final AudioClip shootSound = GameController.soundController.getAudioClip(shootSoundPath);

    public Shotgun(String tag) {
        super(tag, imagePath, shootPath, FireType.AUTO);

        getShootSprite().setAnimationSpeed(85);

        setFireRate(2);

        setMaxAmmo(20);

        setClipSize(2);

        setAmmo(20);

        setDamage(50);

        reload(true);

        setReloadTime(2000);
    }

    @Override
    public AudioClip getShootSound() {
        return shootSound;
    }
}