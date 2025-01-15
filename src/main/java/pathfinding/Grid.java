package pathfinding;
import java.util.*;

public class Grid {
    Node start;
    Node goal;
    public static Node[][] grid;
    Random random = new Random();

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
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                grid[row][col].isStart = false;
                grid[row][col].isGoal = false;
                grid[row][col].isObstacle = false;
                grid[row][col].isShortestPath = false;
            }
        }
    }

    public void clearObstacles() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                grid[row][col].isObstacle = false;
            }
        }
    }

    public void setRandomStartAndGoal() {
        clearGrid();
        int row = random.nextInt(grid.length);
        int col = random.nextInt(grid.length);
        grid[row][col].isStart = true;
        row = random.nextInt(grid.length);
        col = random.nextInt(grid.length);
        grid[row][col].isGoal = true;
    }

    public void setStart(int row, int col) {

    }

    public void setGoal(int row, int col) {

    }

    public void setRandomObstacles(int percent) {
        clearObstacles();
    }
}
