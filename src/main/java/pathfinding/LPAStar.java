package pathfinding;

import java.util.*;

public class LPAStar {
    PriorityQueue<Node> pq;

    public Grid run(Grid grid) {
        Node start = grid.findStart();
        Node goal = grid.findGoal();
        pq = new PriorityQueue<>((a, b) -> {
            int[] keyA = a.lCalculateKey(goal);
            int[] keyB = b.lCalculateKey(goal);
            return compareKeys(keyA, keyB);
        });

        start.lRHS = 0;
        pq.add(start);
        computeShortestPath(grid, goal);
        return grid;
    }

    public void computeShortestPath(Grid grid, Node goal) {
        while (!pq.isEmpty() && (compareKeys(pq.peek().lCalculateKey(goal), goal.lCalculateKey(goal)) == -1 || goal.lRHS != goal.lG)) {
            Node u = pq.poll();
            if (u.lG > u.lRHS) {
                u.lG = u.lRHS;
            } else {
                u.lG = 999999;
                updateNode(grid, u);
            }
            List<Node> neighbors = getNeighbors(grid, u);
            for (Node neighbor : neighbors) {
                updateNode(grid, neighbor);
            }
        }
    }

    public Grid runUpdate(Grid grid, List<Node> updatedNodes) {
        for (int i = 0; i < updatedNodes.size(); i++) {
            Node updatedNode = updatedNodes.get(i);

            List<Node> neighbors = getNeighbors(grid, updatedNode);
            for (Node neighbor : neighbors) {
                updateNode(grid, neighbor);
            }
//            if (!updatedNode.isObstacle) {
                updateNode(grid, updatedNode);
//            }
        }
        Node goal = grid.findGoal();
        computeShortestPath(grid, goal);
        return grid;
    }

    void updateNode(Grid grid, Node u) {
        if (u != grid.findStart()) {
            List<Node> neighbors = getNeighbors(grid, u);
            int minRHS = 999999;
            for (Node neighbor : neighbors) {
                int tentativeRHS = neighbor.lG + getCost(grid, neighbor, u);
                if (tentativeRHS < minRHS) {
                    minRHS = tentativeRHS;
                }
            }
            u.lRHS = minRHS;
        }
        pq.remove(u);
        if (u.lG != u.lRHS) {
            pq.add(u);
        }
    }

    public static int getCost(Grid grid, Node a, Node b) {
        int cost = 10;
        if (Math.abs(a.row - b.row) == 1 && Math.abs(a.col - b.col) == 1) {
            cost = 14;
        }
        return cost;
    }

    void constructPath(Grid grid, Node current) {
        current.isShortestPath = true;
        if (!current.isStart) {
            List<Node> neighbors = getNeighbors(grid, current);
            int minCost = 999999;
            Node bestNeighbor = null;
            for (Node neighbor : neighbors) {
                if (neighbor.lG < minCost && !neighbor.isShortestPath) {
                    minCost = neighbor.lG;
                    bestNeighbor = neighbor;
                }
            }
            current.parent = bestNeighbor;
            constructPath(grid, bestNeighbor);
        }
    }

    List<Node> getNeighbors(Grid grid, Node node) {
        List<Node> neighbors = new ArrayList<>();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        for (int[] dir : directions) {
            int newRow = node.row + dir[0];
            int newCol = node.col + dir[1];
//            if (grid.isValidCoord(newRow, newCol) && !grid.grid[newRow][newCol].isObstacle) {
            if (grid.isValidCoord(newRow, newCol) && !grid.grid[newRow][newCol].isObstacle) {
            Node neighbor = grid.grid[newRow][newCol];
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    int compareKeys(int[] keyA, int[] keyB) {
        if (keyA[0] < keyB[0]) return -1;
        if (keyA[0] > keyB[0]) return 1;
        if (keyA[1] < keyB[1]) return -1;
        if (keyA[1] > keyB[1]) return 1;
        return 0;  // Keys are equal
    }
}
