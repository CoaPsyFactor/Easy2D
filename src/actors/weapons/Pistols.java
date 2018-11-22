package actors.weapons;

import actors.Weapon;
import javafx.scene.media.AudioClip;

/**
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public class Pistols extends Weapon {

    private final static String imagePath = "weapons/Pistols.png";

    private final static String shootPath = "weapons/MuzzleflashPistol.png";

    public Pistols(String tag) {
        super(tag, imagePath, shootPath, FireType.SINGLE);

        getShootSprite().setAnimationSpeed(85);

        setFireRate(5);

        setMaxAmmo(90);

        setClipSize(7);

        setAmmo(90);

        setDamage(1);

        reload(true);

        setReloadTime(500);
    }

    @Override
    public AudioClip getShootSound() {
        return null;
    }
}
