package actors.navigation;

import lib.gameactor.GameActor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public class NavigationPath extends GameActor {

    private final HashMap<String, Boolean> nodes = new HashMap<>();

    public void addNode(int x, int y) {
        nodes.put(x + "x" + y, true);
    }

    public boolean isWalkable(int x, int y) {
        return nodes.containsKey(x + "x" + y) ? nodes.get(x + "x" + y) : false;
    }

    public List<NavigationNode> findPath(double x, double y, double ex, double ey) {
        return findPath((int) x, (int) y, (int) ex, (int) ey);
    }


    public List<NavigationNode> findPath(int x, int y, int ex, int ey) {

        if (false == isWalkable(x, y) || false == isWalkable(ex, ey)) {
            return null;
        }

        List<NavigationNode> openList = new ArrayList<>();

        List<NavigationNode> closedList = new ArrayList<>();

        List<NavigationNode> siblingNodes = new ArrayList<>(8);

        openList.add(NavigationNode.getNode(x, y, ex, ey, null));

        NavigationNode navigateNode = getClosest(openList);

        while (null != navigateNode) {

            siblingNodes.clear();

            openList.remove(navigateNode);

            closedList.add(navigateNode);

            if (navigateNode.getX() == ex && navigateNode.getY() == ey) {
                break;
            }

            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {

                    if (i == 0 && j == 0) {
                        continue;
                    }

                    siblingNodes.add(NavigationNode.getNode(navigateNode.getX() + i, navigateNode.getY() + j, ex, ey, navigateNode));
                }
            }

            for (NavigationNode navigationNode : siblingNodes) {
                if (closedList.contains(navigationNode) || false == isWalkable(navigationNode.getX(), navigationNode.getY())) {
                    continue;
                }

                if (navigationNode.getFCost() > navigationNode.getFCostForParent(navigateNode) || false == openList.contains(navigationNode)) {
                    navigationNode.setParentNode(navigateNode);

                    if (false == openList.contains(navigationNode)) {
                        openList.add(navigationNode);
                    }
                }
            }

            navigateNode = getClosest(openList);
        }

        siblingNodes.clear();

        openList.clear();

        return getPath(closedList);
    }

    private NavigationNode getClosest(List<NavigationNode> nodes) {

        int smallestFCost = -1;

        NavigationNode wantedNode = null;

        for (NavigationNode navigationNode : nodes) {
            if (navigationNode.getFCost() > smallestFCost && smallestFCost != -1) {
                continue;
            }

            smallestFCost = navigationNode.getFCost();

            wantedNode = navigationNode;
        }

        return wantedNode;
    }

    private List<NavigationNode> getPath(List<NavigationNode> nodes) {
        List<NavigationNode> path = new ArrayList<>();

        if (null == nodes) {
            return path;
        }

        NavigationNode node = null;

        try {
            node = nodes.get(nodes.size() - 1);
        } catch (ArrayIndexOutOfBoundsException exception) {

            System.out.println(exception.getMessage());

            return null;
        }

        path.add(node);

        NavigationNode parent = node.getParent();

        while (null != parent && false == path.contains(parent)) {
            path.add(parent);

            parent = parent.getParent();
        }

        nodes.clear();

        Collections.reverse(path);

        return path;
    }

    @Override
    public String getTag() {
        return "navigation_path";
    }
}
