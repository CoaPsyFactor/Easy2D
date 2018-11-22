package actors.enemy;

import actors.Weapon;
import actors.debug.Block;
import actors.movable.MovableActor;
import actors.navigation.NavigationNode;
import actors.navigation.NavigationPath;
import controllers.GameController;
import lib.gameactor.GameActor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by azivanovic on 7/28/16.
 */
public abstract class EnemyAI extends MovableActor {

    private NavigationPath path = null;

    private List<NavigationNode> moveNodes;

    private Iterator<NavigationNode> moveNodesIterator;

    private List<AIState> activeStates = new ArrayList<>(4);

    private NavigationNode node;

    private Weapon currentWeapon;

    private GameActor attackTarget;

    private GameActor followTarget;

    public EnemyAI() {
        if (null == GameController.coreController.getCurrentLevel()) {
            throw new RuntimeException("Trying to instance EnemyAI without level.");
        }

        try {
            path = (NavigationPath) GameController.coreController.getCurrentLevel().getProperty("navigation_path");
        } catch (Exception exception) {
            System.out.println("Failed to get navigation_path");

            exception.printStackTrace();
        }
    }

    @Override
    public void render(double delta) {
        super.render(delta);

        if (isStateActive(AIState.SEARCHING)) {

        }

        if (isStateActive(AIState.ATTACKING)) {
            if (null == attackTarget || null == currentWeapon) {
                removeActiveState(AIState.ATTACKING);
            } else if (false == targetInRange(attackTarget, currentWeapon.getWeaponRange()) && false == isStateActive(AIState.FOLLOW)) {
                follow(attackTarget);
            }
        }

        if (isStateActive(AIState.FOLLOW)) {
            if (null != moveNodesIterator) {
                step(delta);
            } else if (null == moveNodesIterator || false == moveNodesIterator.hasNext()) {

                if (null != moveNodes) {
                    moveNodes.clear();

                    moveNodes = null;
                }

                if (null != moveNodesIterator) {
                    moveNodesIterator.remove();
                }

                removeActiveState(AIState.FOLLOW);
            }
        }

    }

    public boolean getPositionNodes(GameActor actor) {
        if (null != moveNodes) {
            moveNodes.clear();

            moveNodes = null;
        }

        moveNodes = path.findPath(
                ((getPositionX() + getSprite().getView().getFitWidth() / 2) / 64),
                ((getPositionY() - getSprite().getView().getFitHeight() / 2) / 64),
                ((actor.getPositionX() + actor.getSprite().getView().getFitWidth() / 2) / 64),
                (((actor.getPositionY() - actor.getSprite().getView().getFitHeight() / 2) / 64) + 1)
        );

        if (moveNodes == null) {
            System.out.println("Unable to find path");

            return false;
        }

        moveNodes.forEach(navigationNode -> {
            Block actsor = new Block(5);

            actsor.setIsActive(true);

            actsor.setPosition(navigationNode.getX() * 64, navigationNode.getY() * 64);

            GameController.coreController.addActor(actsor);
        });

        try {
            moveNodesIterator = moveNodes.iterator();
        } catch (NullPointerException exception) {
            System.out.println("Unable to find path");

            return false;
        }

        return true;
    }

    public void attack(GameActor actor) {
        attackTarget = actor;

        addActiveState(AIState.ATTACKING);
    }

    public void follow(GameActor actor) {
        if (getPositionNodes(actor)) {
            followTarget = actor;

            addActiveState(AIState.FOLLOW);
        }
    }

    public void setCurrentWeapon(Weapon weapon) {
        currentWeapon = weapon;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public void addActiveState(AIState state) {
        if (activeStates.contains(state)) {
            return;
        }

        activeStates.add(state);
    }

    public void removeActiveState(AIState state) {
        if (activeStates.contains(state)) {
            activeStates.remove(state);
        }
    }

    public boolean isStateActive(AIState state) {
        return activeStates.contains(state);
    }

    private boolean targetInRange(GameActor actor, double distance) {
        return getDistanceFromActor(actor) <= distance;
    }

    private double getDistanceFromActor(GameActor actor) {
        double dX = actor.getPositionX() - getPositionX();

        double dY = actor.getPositionY() - getPositionY();

        return Math.sqrt(dX * dX + dY * dY);
    }

    private void step(double delta) {

        if (null == moveNodesIterator) {
            return;
        }

        if (null == node && moveNodesIterator.hasNext()) {
            node = moveNodesIterator.next();
        } else if (null == node && false == moveNodesIterator.hasNext()) {
            removeActiveState(AIState.FOLLOW);

            moveNodes.clear();

            moveNodesIterator.remove();

            return;
        }

        double moveX = 0;

        double moveY = 0;

        if ((getPositionX() - node.getX() * 64) * 0.02 == 0 && (getPositionY() - node.getY() * 64) * 0.02 == 0 && moveNodesIterator.hasNext()) {
            node = moveNodesIterator.next();
        } else if (null == node && false == moveNodesIterator.hasNext()) {
            removeActiveState(AIState.FOLLOW);

            moveNodes.clear();

            moveNodesIterator.remove();

            return;
        }

        if (node.getX() * 64 > getPositionX()) {
            moveX = 0.5;
        } else if (node.getX() * 64 < getPositionX()) {
            moveX = -0.5;
        }

        if (node.getY() * 64 > getPositionY()) {
            moveY = 0.5;
        } else if (node.getY() * 64 < getPositionY()) {
            moveY = -0.5;
        } else if (moveX == 0 && moveNodesIterator.hasNext()) {
            node = moveNodesIterator.next();
        }

        moveFor(moveX, moveY);
    }

    public abstract double getSenseRange();
}
