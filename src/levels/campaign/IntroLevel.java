package levels.campaign;

import actors.enemy.EnemyRobot;
import actors.map.MapLoader;
import controllers.GameController;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import levels.BaseLevel;

/**
 * Created by azivanovic on 7/21/16.
 */
public class IntroLevel extends BaseLevel {

    @Override
    public boolean onKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.Q) {
            GameController.coreController.loadLevel(new IntroLevel());

            return false;
        }

        return super.onKeyReleased(event);
    }

    @Override
    public String getBackgroundSoundPath() {
        return "background/nature_low.mp3";
    }

    @Override
    public void onLevelLoad() {
        EnemyRobot enemy = null;

        enemy = new EnemyRobot();

        enemy.setIsActive(true);

        enemy.setPosition(256, 200);

        addProperty("enemy_robot_two", enemy);

        enemy = new EnemyRobot();

        enemy.setIsActive(true);

        enemy.setPosition(32, 420);

        addProperty("enemy_robot_three", enemy);

        enemy = new EnemyRobot();

        enemy.setIsActive(true);

        enemy.setPosition(128, 512);

        addProperty("enemy_robot_four", enemy);

        enemy = new EnemyRobot();

        enemy.setPosition(64, 512);

        enemy.setIsActive(true);

        addProperty("enemy_robot_five", enemy);

        MapLoader map = new MapLoader();

        addProperty("level_map", map);

        super.onLevelLoad();
    }

    @Override
    public String getLevelIdentifier() {
        return "intro_level";
    }

    @Override
    public boolean onMousePressed(MouseEvent event) {
        return false;
    }

    @Override
    public void onLevelUnload() {
    }
}
