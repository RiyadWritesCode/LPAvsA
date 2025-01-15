package pathfinding;
import java.util.*;

public class Grid {
    Node start;
    Node goal;
    public static Node[][] grid;
    Random random = new Random();
    int size = grid.length;

    public Grid(int size) {
        grid = new Node[size][size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                grid[row][col] = new Node(row, col);
            }
        }
    }

    // Clears all nodes of their types
    public void clearGrid() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                grid[row][col].isStart = false;
                grid[row][col].isGoal = false;
                grid[row][col].isObstacle = false;
                grid[row][col].isShortestPath = false;
            }
        }
    }

    public void clearObstacles() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                grid[row][col].isObstacle = false;
            }
        }
    }

    public void setRandomStartAndGoal() {
        clearGrid();
        int startRow = random.nextInt(size);
        int startCol = random.nextInt(size);
        grid[startRow][startCol].isStart = true;

        int goalRow, goalCol;
        do {
            goalRow = random.nextInt(grid.length);
            goalCol = random.nextInt(grid.length);
        } while (goalRow == startRow && goalCol == startCol);

        grid[goalRow][goalCol].isGoal = true;
    }

    public void setStart(int row, int col) {
        clearGrid();
        grid[row][col].isStart = true;
    }

    public void setGoal(int row, int col) {
        clearGrid();
        grid[row][col].isGoal = true;
    }

    public Node findStart() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (grid[row][col].isStart) {
                    return grid[row][col];
                }
            }
        }
        return null;
    }

    public Node findGoal() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (grid[row][col].isGoal) {
                    return grid[row][col];
                }
            }
        }
        return null;
    }

    public void setRandomObstacles(int percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        clearObstacles();

        int totalNodes = size * size;
        // Reserved nodes are of goal or start type
        int reservedNodes = 0;
        if (findStart() != null) reservedNodes++;
        if (findGoal() != null) reservedNodes++;

        // Available nodes are !reserved
        int availableNodesCount = totalNodes - reservedNodes;
        int obstacleCount = (totalNodes * percent) / 100;

        if (obstacleCount > availableNodesCount) {
            throw new IllegalArgumentException("Impossible configuration: Not enough free nodes to create " + percent + "% obstacles.");
        }

        // Collect all available Nodes
        List<Node> availableNodes = new ArrayList<>();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Node node = grid[row][col];
                if (!node.isStart && !node.isGoal) {
                    availableNodes.add(node);
                }
            }
        }

        Collections.shuffle(availableNodes);
        for (int i = 0; i < obstacleCount; i++) {
            availableNodes.get(i).isObstacle = true;
        }
    }
}
