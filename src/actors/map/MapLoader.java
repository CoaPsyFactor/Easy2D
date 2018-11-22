package actors.map;

import actors.CollisionBlock;
import actors.StaticMap;
import actors.navigation.NavigationPath;
import controllers.CoreGameController;
import controllers.DatabaseController;
import controllers.GameController;
import controllers.GameLoopController;
import lib.gameactor.GameActor;
import lib.sprite.Sprite;
import lib.sprite.SpriteSheet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by azivanovic on 9/11/16.
 */
public class MapLoader extends GameActor {

    private final DatabaseController db = GameController.databaseController;

    private final static double[] staticMapSize = new double[]{
            CoreGameController.WIDTH + 64,
            CoreGameController.HEIGHT + 64
    };

    private final StaticMap staticMap = new StaticMap();

    private final HashMap<double[], ArrayList<Sprite>> mapSprites = new HashMap<>();

    private final ArrayList<Sprite> baseLayerSprites = new ArrayList<>();

    private NavigationPath path;

    public MapLoader() {
        GameController.coreController.addActor(staticMap);
    }

    @Override
    public void onBegin() {
        if (GameController.coreController.getCurrentLevel() == null) {
            return;
        }

        path = (NavigationPath) GameController.coreController.getCurrentLevel().getProperty("navigation_path");

        try {
            loadLevelMap();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

    public void loadLevelMap() throws SQLException {
        Statement statement = db.getConnection().createStatement();

        String query = String.format(
                "SELECT `maps`.* FROM `maps` WHERE `maps`.`level` = '%s';",
                GameController.coreController.getCurrentLevel().getLevelIdentifier()
        );

        ResultSet mapData = statement.executeQuery(query);

        mapSprites.clear();

        while (mapData.next()) {

            if (1 == mapData.getShort("is_path") && null != path) {
                path.addNode(Math.round(mapData.getInt("x") / 64), Math.round(mapData.getInt("y") / 64));

                continue;
            }

            if (0 == mapData.getShort("walkable")) {
                GameController.coreController.addActor(new CollisionBlock(mapData.getInt("x"), mapData.getInt("y"), 64, 64));

                continue;
            }

            SpriteSheet spriteSheet = SpriteSheet.getSpriteSheet(mapData.getString("spritesheet") + ".png", 64, 64);

            Sprite sprite = spriteSheet.getNewSprite(mapData.getInt("sprite"));

            if (mapData.getString("layer").equals(GameLoopController.Layer.BASE.toString())) {
                sprite.setOffset(mapData.getInt("x"), mapData.getInt("y"));

                baseLayerSprites.add(sprite);

                continue;
            }

            MapActor mapActor = new MapActor(sprite);

            setMapActorLayer(mapData.getString("layer").toLowerCase(), mapActor);

            spawnActor(mapData.getInt("x"), mapData.getInt("y"), mapActor);
        }

        mapSprites.put(staticMapSize, baseLayerSprites);

        staticMap.setBody(mapSprites);

        staticMap.setIsActive(true);
    }

    private void spawnActor(int x, int y, MapActor actor) {
        actor.setPosition(x, y);

        actor.setIsActive(true);

        GameController.coreController.addActor(actor);
    }

    private void setMapActorLayer(String layer, MapActor actor) {
        switch (layer) {
            case "first":
                actor.setLayer(GameLoopController.Layer.FIRST);

                break;
            case "second":
                actor.setLayer(GameLoopController.Layer.SECOND);

                break;
            case "top":
                actor.setLayer(GameLoopController.Layer.TOP);

                break;
            case "gui":
                actor.setLayer(GameLoopController.Layer.GUI);

                break;
        }
    }

    @Override
    public String getTag() {
        return "loaded_map";
    }
}
