package controllers;

import controllers.GameLoopController.Layer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lib.GameLevel;
import lib.gameactor.GameActor;
import lib.gameactor.GameActorKeyboardListener;
import lib.gameactor.GameActorMouseListener;
import lib.sprite.Sprite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

/**
 * CoreGameController sluzi da komunicira izmedju nivoa i game engine-a, ovde se podesavaju pocetne vrednosti i kontrolise tok igrice
 *
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public class CoreGameController extends GameController {

    /**
     * Sirina prozora
     */
    public static final double WIDTH = 1024;

    /**
     * Visina prozora
     */
    public static final double HEIGHT = 768;

    /**
     * Naslov prozora
     */
    public static String TITLE = "Shooter 2D";

    /**
     * Vrednost koja odredjuje da li korisnik moze da menja velicinu prozora
     */
    public static final boolean IS_RESIZABLE = false;

    /**
     * Lista "GameActor"-a koja slusa dogadjaje na tastaturi (press, release)
     */
    private final HashSet<GameActorKeyboardListener> keyboardListeners = new HashSet<>();

    /**
     * Lista trenutno pritisnutih tastera
     */
    private final HashSet<KeyCode> pressedKeys = new HashSet<>();

    /**
     * Lista "GameActor"-a koja slusa dogadjaje na misu. Zavisno je od sloja (layer)
     */
    private final HashMap<Layer, ArrayList<GameActorMouseListener>> mouseListeners = new HashMap<>();

    /**
     * "Stage" koji drzi sve elemente koji treba da se iscrtaju
     */
    private final Group stageItems = new Group();

    /**
     * Scena na koju idu svi papiri (Canvas) za crtanje
     */
    private final Scene scene = new Scene(stageItems, WIDTH, HEIGHT, Color.WHITE);

    /**
     * Glavni prozor
     */
    private Stage primaryStage;

    /**
     * GameActor na kome se desio neki "mouse event" - ovo imamo posto moze da se desi da ima 2 ili vise "GameActor"-a
     * na istoj poziciji samo ne na istom nivou/sloju (layer)
     */
    private GameActor triggerActor;

    /**
     * Trenutno ucitan nivo
     */
    private GameLevel currentLevel;

    /**
     * Dodavanje GameActora u game engine. Vezivanje dogadjaja za mis i tastaturu, i dodavanje u render tree (iscrtavanje)
     *
     * @param actor GameActor koji ce da bude dodat u engine
     * @return trenutni instancu CoreGameController-a
     */
    public CoreGameController addActor(GameActor actor) {

        if (null == actor || GameController.loopController.containsActor(actor)) {
            return this;
        }

        GameController.loopController.addActor(actor);

        if (actor instanceof GameActorMouseListener) {
            if (false == mouseListeners.containsKey(actor.getLayer())) {
                mouseListeners.put(actor.getLayer(), new ArrayList<>());
            }

            mouseListeners.get(actor.getLayer()).add((GameActorMouseListener) actor);
        }

        if (actor instanceof GameActorKeyboardListener) {
            keyboardListeners.add((GameActorKeyboardListener) actor);
        }

        return this;
    }

    /**
     * Uklanja GameActor-a iz game engina, odvezuje sve dogadjaje i brise iz render tree (liste za iscrtavanje)
     *
     * @param actor GameActor koji treba da bude uklonjen
     */
    public void removeActor(GameActor actor) {
        if (null == actor) {
            return;
        }

        if (mouseListeners.containsKey(actor.getLayer())) {
            mouseListeners.get(actor.getLayer()).remove(actor);
        }

        keyboardListeners.remove(actor);

        actor.delete();
    }

    /**
     * Brise sve GameActor-e koji su dodati u game engine.
     */
    public void clear() {
        mouseListeners.forEach((layer, gameActorMouseListeners) -> gameActorMouseListeners.clear());

        keyboardListeners.clear();

        GameController.loopController.clear();
    }

    /**
     * Menja trenutni stage na kome se sve trenutno odigrava
     *
     * @param stage Trenutni "stage" na kome se sve odigrava
     */
    public void setStage(Stage stage) {
        primaryStage = stage;

        primaryStage.setWidth(WIDTH);

        primaryStage.setHeight(HEIGHT);

        primaryStage.setScene(scene);

        primaryStage.setTitle(TITLE);

        primaryStage.setResizable(IS_RESIZABLE);
    }

    /**
     * Vraca instancu trenutno ucitanog nivoa
     *
     * @return Instanca nivoa
     */
    public GameLevel getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Pokrece igricu
     */
    public void start() {

        if (null == primaryStage) {
            throw new RuntimeException("Invalid Stage.");
        }

        GameController.loopController.startRender(stageItems);

        primaryStage.show();
    }

    /**
     * Pokrece (ucitava) novi nivo, brisajuci sve stare GameActore iz memorije
     * <p>
     * Prilikom ucitavanja nivoa se pokrece i "Garbage Collector" kako bi se obrisali sve visece reference
     *
     * @param level Novi nivo
     */
    public void loadLevel(GameLevel level) {
        if (null != currentLevel) {

            System.out.println("Clearing level " + currentLevel.getClass().getName());

            currentLevel.onUnload();

            currentLevel.clearProperties();
        }

        clear();

        currentLevel = level;

        level.onLoad();

        level.getProperties().forEach(gameActor -> addActor(gameActor));

        attachEvents();

        System.gc();

        System.out.println("Level changed to " + currentLevel.getClass().getName());
    }

    /**
     * Vraca GameActora (koji implementira GameActorColliderInterface interfejs) koji se nalazi na trazenoj XY poziciji
     * i na najvisem sloju (layer)
     *
     * @param x X pozicija
     * @param y Y pozicija
     * @return actora koji se nalazi na trazenoj X i Y poziciji i najvisem sloju (layer)
     */
    public GameActor getFirstOnPosition(double x, double y) {

        GameActor actor = null;

        for (Layer layer : Layer.values()) {
            GameActor currentActor = getFirstOnPosition(layer, x, y);

            if (null != currentActor) {
                actor = currentActor;
            }
        }

        return actor;
    }

    /**
     * Vraca GameActora (koji implementira GameActorColliderInterface interfejs) koji se nalazi na trazenoj XY poziciji
     * i na zadatom sloju (layer)
     *
     * @param layer Sloj na kome da gleda
     * @param x     X pozicija
     * @param y     Y pozicija
     * @return actora koji se nalazi na trazenoj X i Y poziciji i zadatom sloju
     */
    public GameActor getFirstOnPosition(Layer layer, double x, double y) {
        ArrayList<GameActorMouseListener> actors = mouseListeners.get(layer);

        if (null == actors) {
            return null;
        }

        Optional<GameActorMouseListener> optional = actors.stream().filter(actor -> {
            if (false == ((GameActor) actor).isActive()) {
                return false;
            }

            Sprite sprite = ((GameActor) actor).getSprite();

            return null != sprite && sprite.getView().intersects(x, y, 1, 1);
        }).findFirst();

        if (optional.isPresent()) {
            return (GameActor) optional.get();
        }

        return null;
    }

    /**
     * Vezuje sve dogadjaje za mis i tastaturu
     */
    private void attachEvents() {
        attachMouseEventsOnActors();
        attachKeyboardEventsOnActors();
    }

    /**
     * Vezuje sve dogadjaje za tastaturu
     */
    private void attachKeyboardEventsOnActors() {

        if (null == currentLevel) {
            System.out.println("Level is not loaded.");

            return;
        }

        scene.setOnKeyPressed(event -> {
            if (pressedKeys.contains(event.getCode())) {

                return;
            }

            pressedKeys.add(event.getCode());

            try {
                currentLevel.onKeyPressed(event);

                keyboardListeners.forEach(gameActor -> {
                    gameActor.onKeyPressed(triggerActor, event);
                });
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        scene.setOnKeyReleased(event -> {

            if (pressedKeys.contains(event.getCode())) {
                pressedKeys.remove(event.getCode());
            }

            try {
                currentLevel.onKeyReleased(event);

                keyboardListeners.forEach(gameActor -> {
                    gameActor.onKeyReleased(triggerActor, event);
                });
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                throw exception;
            }

        });

    }

    /**
     * Vezije sve dogadjaje za mis
     */
    private void attachMouseEventsOnActors() {
        if (null == currentLevel) {
            return;
        }

        /*
            Pribavljanje "triggerActor"-a se desava ovde, posto da bi se doslo i kliknulo na actora mora da se desi
            mouseMove event
         */
        scene.setOnMouseMoved(event -> {

            currentLevel.onMouseMoved(event);

            triggerActor = getFirstOnPosition(event.getX(), event.getY());

            if (null != triggerActor) {
                ((GameActorMouseListener) triggerActor).onMouseMoved(event);
            }
        });

        scene.setOnMousePressed(event -> {

            currentLevel.onMousePressed(event);

            if (null != triggerActor) {
                ((GameActorMouseListener) triggerActor).onMousePressed(event);
            }
        });

        scene.setOnMouseReleased(event -> {

            currentLevel.onMouseReleased(event);

            if (null != triggerActor) {
                ((GameActorMouseListener) triggerActor).onMouseReleased(event);
            }
        });

        scene.setOnMouseClicked(event -> {

            currentLevel.onMouseClicked(event);

            if (null != triggerActor) {
                ((GameActorMouseListener) triggerActor).onMouseClicked(event);
            }
        });
    }
}
