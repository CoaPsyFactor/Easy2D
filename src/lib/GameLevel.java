package lib;

import controllers.GameController;
import controllers.SoundController;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import lib.gameactor.GameActor;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public abstract class GameLevel {

    protected final SoundController soundController = (SoundController) GameController.getSharedInstance(SoundController.class.getName());
    private final HashMap<String, GameActor> levelProperties = new HashMap<>();
    private AudioClip backgroundMusic;

    public GameLevel addProperty(String name, GameActor gameActor) {
        if (levelProperties.containsKey(name)) {
            levelProperties.replace(name, gameActor);

            return this;
        }

        levelProperties.put(name, gameActor);

        return this;
    }

    public HashSet<GameActor> getByTag(String tag) {
        final HashSet<GameActor> tagActors = new HashSet<>();

        levelProperties.forEach((s, actor) -> {
            if (actor.getTag().equals(tag)) {
                tagActors.add(actor);
            }
        });

        return tagActors;
    }

    public Collection<GameActor> getProperties() {
        return levelProperties.values();
    }

    public GameActor getProperty(String name) {
        return levelProperties.containsKey(name) ? levelProperties.get(name) : null;
    }

    public GameLevel removeProperty(String name) {
        if (levelProperties.containsKey(name)) {
            levelProperties.remove(name);
        }

        return this;
    }

    public GameLevel removeProperty(GameActor property) {
        if (levelProperties.containsValue(property)) {
            levelProperties.forEach((s, actor) -> {
                if (actor == property) {
                    levelProperties.remove(s);
                }
            });
        }

        return this;
    }

    public void clearProperties() {
        levelProperties.clear();
    }

    public void onLoad() {

        if (null != getBackgroundSoundPath()) {
            backgroundMusic = soundController.getAudioClip(getBackgroundSoundPath());
        }

        if (null != backgroundMusic) {
            backgroundMusic.setCycleCount(AudioClip.INDEFINITE);

            backgroundMusic.play();
        }

        onLevelLoad();

        levelProperties.forEach((s, gameActor) -> {
            gameActor.onBegin();
        });
    }

    public void onUnload() {
        if (null != backgroundMusic) {
            backgroundMusic.stop();

            backgroundMusic = null;
        }

        levelProperties.forEach((s, gameActor) -> {
            if (null != gameActor.getSprite()) {
                gameActor.getSprite().clearAnimation();
            }

            gameActor.onEnd();
        });

        onLevelUnload();

        clearProperties();
    }

    public abstract boolean onMouseMoved(MouseEvent event);

    public abstract boolean onMousePressed(MouseEvent event);

    public abstract boolean onMouseReleased(MouseEvent event);

    public abstract boolean onMouseClicked(MouseEvent event);

    public abstract boolean onKeyPressed(KeyEvent event);

    public abstract boolean onKeyReleased(KeyEvent event);

    public abstract String getBackgroundSoundPath();

    public abstract void onLevelLoad();

    public abstract void onLevelUnload();

    public abstract String getLevelIdentifier();
}
