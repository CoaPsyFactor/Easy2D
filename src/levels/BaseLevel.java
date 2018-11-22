package levels;

import actors.StaticMap;
import actors.Weapon;
import actors.gui.palyer.HealthBar;
import actors.gui.palyer.LowAmmo;
import actors.gui.palyer.NoAmmo;
import actors.gui.palyer.Reloading;
import actors.navigation.NavigationPath;
import actors.player.Player;
import actors.weapons.AssaultRifle;
import actors.weapons.Pistols;
import actors.weapons.Shotgun;
import controllers.CoreGameController;
import controllers.GameLoopController;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import lib.GameLevel;
import lib.sprite.Sprite;
import lib.sprite.SpriteSheet;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public abstract class BaseLevel extends GameLevel {

    protected final SpriteSheet mapSpriteSheet = SpriteSheet.getSpriteSheet("base_sheet_64.png", 64, 64);

    private final int DEFAULT_DIMENSION = 64;
    private final double[] staticMapSize = new double[]{
            CoreGameController.WIDTH + DEFAULT_DIMENSION,
            CoreGameController.HEIGHT + DEFAULT_DIMENSION
    };
    protected Player player = new Player();

    @Override
    public boolean onMouseMoved(MouseEvent event) {
        return false;
    }

    @Override
    public boolean onMousePressed(MouseEvent event) {
        return false;
    }

    @Override
    public boolean onMouseReleased(MouseEvent event) {

        if (event.getButton() == MouseButton.PRIMARY) {
            player.isShooting(false);
        }

        return false;
    }

    @Override
    public boolean onMouseClicked(MouseEvent event) {
        return false;
    }

    @Override
    public boolean onKeyPressed(KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyReleased(KeyEvent event) {
        return false;
    }

    @Override
    public void onLevelLoad() {
        HealthBar healthBar = new HealthBar(player);

        healthBar.setIsActive(true);

        Reloading reloading = new Reloading(player);

        reloading.setLayer(GameLoopController.Layer.GUI);

        addProperty(reloading.getTag(), reloading);

        NoAmmo noAmmo = new NoAmmo(player);

        noAmmo.setLayer(GameLoopController.Layer.GUI);

        addProperty(noAmmo.getTag(), noAmmo);

        LowAmmo lowAmmo = new LowAmmo(player);

        lowAmmo.setLayer(GameLoopController.Layer.GUI);

        addProperty(lowAmmo.getTag(), lowAmmo);

        Weapon assaultRifle = new AssaultRifle("player_assault_rifle");

        player.addWeapon(assaultRifle);

        Weapon pistols = new Pistols("player_pistols");

        player.addWeapon(pistols);

        Weapon shotgun = new Shotgun("player_shotgun");

        player.addWeapon(shotgun);

        addProperty(assaultRifle.getTag(), assaultRifle);

        addProperty(pistols.getTag(), pistols);

        addProperty(shotgun.getTag(), shotgun);

        addProperty("player", player);

        player.setIsActive(true);

        healthBar.updateHealthBar(100, 100);

        NavigationPath path = new NavigationPath();

        addProperty("navigation_path", path);

        assaultRifle.addGUIItem("low_ammo", lowAmmo);

        assaultRifle.addGUIItem("reloading", reloading);

        assaultRifle.addGUIItem("no_ammo", noAmmo);

        pistols.addGUIItem("low_ammo", lowAmmo);

        pistols.addGUIItem("reloading", reloading);

        pistols.addGUIItem("no_ammo", noAmmo);

        shotgun.addGUIItem("low_ammo", lowAmmo);

        shotgun.addGUIItem("reloading", reloading);

        shotgun.addGUIItem("no_ammo", noAmmo);

    }

    @Override
    public void onLevelUnload() {
        clearProperties();
    }

    protected void drawStaticMap(ArrayList<Sprite> sprites) {
        HashMap<double[], ArrayList<Sprite>> body = new HashMap<>(1);

        body.put(staticMapSize, sprites);

        StaticMap map = new StaticMap();

        map.setBody(body);

        map.setIsActive(true);

        addProperty("map", map);

        sprites.clear();

        body.clear();
    }
}
