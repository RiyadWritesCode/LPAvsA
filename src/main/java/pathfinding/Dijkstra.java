package pathfinding;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra {
    public Grid run(Grid grid) {
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(node -> node.distance));
        pq.add(grid.findStart());
        grid.findStart().distance = 0;
        // right, left, up, down, top-right, top-left, bottom-right, bottom-left
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        while(!pq.isEmpty()) {
            Node current = pq.poll();
            if (current.visited) continue;
            current.visited = true;
            if (current == grid.findGoal()) {
                constructPath(current);
                return grid;
            }
            for (int[] dir: directions) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];

                if (grid.isValidCoord(newRow, newCol) && !grid.grid[newRow][newCol].isObstacle) {
                    Node neighbor = grid.grid[newRow][newCol];
                    int moveCost = (dir[0] != 0 && dir[1] != 0) ? 14 : 10;
                    int newDist = current.distance + moveCost;
                    if (newDist < neighbor.distance) {
                        neighbor.distance = newDist;
                        neighbor.parent = current;
                        pq.add(neighbor);
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
