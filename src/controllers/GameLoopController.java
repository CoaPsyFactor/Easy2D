package controllers;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import lib.gameactor.GameActor;
import lib.gameactor.GameActorColliderInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public class GameLoopController extends GameController {

    /**
     * Holds all layers that are rendered
     */
    private final HashMap<Layer, Canvas> layers = new HashMap<>();


    private final ArrayList<GameActor> addActorQueue = new ArrayList<>();

    private final ArrayList<GameActor> actors = new ArrayList<>();

    public GameLoopController() {
        for (Layer layer : Layer.values()) {
            layers.put(layer, new Canvas(CoreGameController.WIDTH, CoreGameController.HEIGHT));
        }
    }

    void addActor(GameActor actor) {
        if (actors.contains(actor) || addActorQueue.contains(actor)) {
            return;
        }

        addActorQueue.add(actor);
    }

    boolean containsActor(GameActor actor) {
        return actors.contains(actor);
    }

    void clear() {
        actors.forEach(actor -> {
            actor.onEnd();

            actor.delete();
        });

        actors.clear();
    }

    void startRender(Group root) {

        GameLoop loop = new GameLoop();

        for (Layer layer : Layer.values()) {
            root.getChildren().add(layers.get(layer));
        }

        loop.start();
    }

    public enum Layer {
        BASE,
        FIRST,
        SECOND,
        TOP,
        GUI
    }

    private class GameLoop extends AnimationTimer {

        private long previous = 0;

        private final HashSet<GameActor> removeActorsQueue = new HashSet<>();

        @Override
        public void handle(long now) {

            double delta = ((now - previous) / 1000000);

            if (false == addActorQueue.isEmpty()) {
                addActorQueue.forEach(actor -> {
                    actors.add(actor);
                });

                addActorQueue.clear();
            }

            layers.forEach((layer, canvas) -> {
                canvas.getGraphicsContext2D().clearRect(0, 0, CoreGameController.WIDTH, CoreGameController.HEIGHT);
            });

            actors.forEach(actor -> {
                if (actor.isDeleted()) {
                    removeActorsQueue.add(actor);

                    return;
                }

                if (false == actor.isActive()) {
                    return;
                }

                if (actor instanceof GameActorColliderInterface) {
                    actors.forEach(collidingActor -> {
                        if (collidingActor == null || false == collidingActor instanceof GameActorColliderInterface) {
                            return;
                        }

                        if (
                                collidingActor != actor &&
                                        ((GameActorColliderInterface) collidingActor).getCollider() != null &&
                                        ((GameActorColliderInterface) collidingActor).getCollider().inCollision(((GameActorColliderInterface) actor).getCollider())

                                ) {
                            ((GameActorColliderInterface) actor).onCollision(collidingActor);
                        }
                    });
                }

                actor.render(delta);

                actor.draw(layers.get(actor.getLayer()));
            });

            removeActorsQueue.forEach(actor -> {
                actor.onEnd();

                actors.remove(actor);

                actor.dispose();
            });

            removeActorsQueue.clear();

            previous = now;
        }
    }
}
