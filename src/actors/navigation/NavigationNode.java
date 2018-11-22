package actors.navigation;

import java.util.HashMap;

/**
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public class NavigationNode {
    private static HashMap<String, NavigationNode> nodes = new HashMap<>();

    private int gCost;

    private int hCost;

    private int fCost;

    private int x;

    private int y;

    private NavigationNode parent;

    public NavigationNode(int posX, int posY, int endX, int endY, NavigationNode parentNode) {

        calcHCost(endX, endY);

        update(posX, posY);

        nodes.put(posX + "x" + posY, this);
    }

    public static NavigationNode getNode(int posX, int posY, int endX, int endY, NavigationNode parent) {
        if (nodes.containsKey(posX + "x" + posY)) {
            return nodes.get(posX + "x" + posY);
        }

        return new NavigationNode(posX, posY, endX, endY, parent);
    }

    public void update(int posX, int posY) {

        x = posX;

        y = posY;

        gCost = getOffset();

        if (null != parent) {
            gCost += parent.getGCost();
        }

        fCost = gCost + hCost;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getFCost() {
        return fCost;
    }

    public int getGCost() {
        return gCost;
    }

    public int getHCost() {
        return hCost;
    }

    public int getFCostForParent(NavigationNode parent) {
        return getGCost() + parent.getGCost() + getHCost();
    }

    public NavigationNode setParentNode(NavigationNode parentNode) {
        parent = parentNode;

        update(getX(), getY());

        return this;
    }

    public NavigationNode getParent() {
        return parent;
    }

    private int getOffset() {
        if (this == parent) {
            return 0;
        }

        if (null != parent && getX() != parent.getX() && getY() != parent.getY()) {
            return 14;
        }

        return 10;
    }

    private void calcHCost(int endX, int endY) {
        hCost = Math.abs(endX - getX()) + Math.abs(endY - getY());
    }
}
