package pathfinding;

import java.util.*;

public class AStar {
    public Grid run(Grid grid) {
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(node -> node.aF));
        Set<Node> closedList = new HashSet<>();

        Node start = grid.findStart();
        Node goal = grid.findGoal();

        start.aG = 0;
        start.aH = start.heuristic(goal);
        start.aF = start.aG + start.aH;

        openList.add(start);
        // right, left, up, down, top-right, top-left, bottom-right, bottom-left
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        while(!openList.isEmpty()) {
            Node current = openList.poll();
            if (current == goal) {
                return grid;
            }

            closedList.add(current);
            for (int[] dir : directions) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];

                if (grid.isValidCoord(newRow, newCol)) {
                    Node neighbor = grid.grid[newRow][newCol];
                    if (neighbor.isObstacle || closedList.contains(neighbor)) {
                        continue;
                    }
                    int moveCost = (dir[0] != 0 && dir[1] != 0) ? 14 : 10;
                    int tentativeG = current.aG + moveCost;
                    if (!openList.contains(neighbor) || tentativeG < neighbor.aG) {
                        neighbor.aG = tentativeG;
                        neighbor.aH = neighbor.heuristic(goal);
                        neighbor.aF = neighbor.aG + neighbor.aH;
                        neighbor.parent = current;
                        // Add the child to the open list if not already there
                        if (!openList.contains(neighbor)) {
                            openList.add(neighbor);
                        }
                    }
                }
            }
        }
        return grid;
    }

    void constructPath(Node node) {
        node.isShortestPath = true;
        if (node.parent != null) {
            constructPath(node.parent);
        }
    }
}