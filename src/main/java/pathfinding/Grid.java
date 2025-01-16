package pathfinding;
import java.util.*;

public class Grid {
    Node start;
    Node goal;
    public Node[][] grid;
    Random random = new Random();
    int size;

    public Grid(int size) {
        this.size = size;
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
//                grid[row][col].isStart = false;
//                grid[row][col].isGoal = false;
//                grid[row][col].isObstacle = false;
                grid[row][col].isShortestPath = false;
                grid[row][col].aF = 0;
                grid[row][col].aG = 0;
                grid[row][col].aH = 0;
                grid[row][col].distance = 999999999;
                grid[row][col].lRHS = 999999999;
                grid[row][col].lG = 999999999;
                grid[row][col].parent = null;
                grid[row][col].inQ = false;
                grid[row][col].visited = false;

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

    public List<Node> moveObstacles(int percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }

        List<Node> availableNodes = new ArrayList<>();
        List<Node> obstacleNodes = new ArrayList<>();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (!grid[row][col].isStart && !grid[row][col].isGoal) {
                    if (grid[row][col].isObstacle) {
                        obstacleNodes.add(grid[row][col]);
                    } else {
                        availableNodes.add(grid[row][col]);
                    }
                }
            }
        }

        int obstaclesToMove = (int) (obstacleNodes.size() * (percent / 100.0));
        if (availableNodes.size() < obstaclesToMove) {
            throw new IllegalArgumentException("Impossible configuration: Not enough free nodes to move " + percent + "% obstacles.");
        }

        Collections.shuffle(availableNodes);
        Collections.shuffle(obstacleNodes);

        List<Node> updatedNodes = new ArrayList<>();
        for (int i = 0; i < obstaclesToMove; i++) {
            Node obstacleNode = obstacleNodes.get(i);
            Node availableNode = availableNodes.get(i);
            obstacleNode.isObstacle = false;
            availableNode.isObstacle = true;
            updatedNodes.add(obstacleNode);
            updatedNodes.add(availableNode);
        }
        return updatedNodes;
    }

    // Check if a node is within bounds
    public boolean isValidCoord(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    public int pathLength() {
        int count = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (grid[row][col].isShortestPath) {
                    count++;
                }
            }
        }
        clearShortestPath();
        return count;
    }

    public void clearShortestPath() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                grid[row][col].isShortestPath = false;
            }
        }
    }

    public int getShortestPathCost() {
        Node goalNode = findGoal();
        Node current = goalNode;
        int totalCost = 0;
        while (current.parent != null) {
            if (current.isObstacle) {
                throw new IllegalStateException(
                        "Invalid path: Node at (" + current.row + ", " + current.col + ") is an obstacle."
                );
            }
            int rowDiff = Math.abs(current.row - current.parent.row);
            int colDiff = Math.abs(current.col - current.parent.col);

            if (rowDiff > 1 || colDiff > 1) {
                throw new IllegalStateException(
                        "Invalid path: Jump detected between (" + current.row + ", " + current.col + ") and "
                                + "(" + current.parent.row + ", " + current.parent.col + ")."
                );
            }

            if (rowDiff == 1 && colDiff == 1) {
                totalCost += 14;
            } else {
                totalCost += 10;
            }
            current = current.parent;
        }
        if (current.isObstacle) {
            throw new IllegalStateException(
                    "Invalid path: Start node at (" + current.row + ", " + current.col + ") is an obstacle."
            );
        }
        return totalCost;
    }

    public Grid clone() {
        // Create a new Grid with the same size
        Grid copy = new Grid(this.size);

        // Copy each Node's fields
        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {
                Node original = this.grid[row][col];
                Node cloned = new Node(original.row, original.col);

                // Copy all relevant node fields
                cloned.visited        = original.visited;
                cloned.isObstacle     = original.isObstacle;
                cloned.isStart        = original.isStart;
                cloned.isGoal         = original.isGoal;
                cloned.isShortestPath = original.isShortestPath;
                cloned.inQ            = original.inQ;
                cloned.distance       = original.distance;
                cloned.lG             = original.lG;
                cloned.lRHS           = original.lRHS;
                cloned.aG             = original.aG;
                cloned.aH             = original.aH;
                cloned.aF             = original.aF;
                cloned.parent = null;
                /*
                 * "Parent" references can be tricky. If you do a full deep copy,
                 * you'd need to map old parents to new nodes. Often in pathfinding,
                 * we re-compute 'parent' anyway, so we skip copying it. But if you
                 * want to preserve the entire path structure, you'd have to build
                 * a mapping from original Node -> cloned Node, then assign parents
                 * after this double-loop finishes. For now, let's skip it:
                 *
                 *  cloned.parent = null;
                 */

                copy.grid[row][col] = cloned;
            }
        }

        // If the Grid tracks 'start' and 'goal' as fields, fix them up too:
        Node oldStart = this.findStart();
        if (oldStart != null) {
            // Make 'copy.start' point to the *cloned* node
            copy.start = copy.grid[oldStart.row][oldStart.col];
        }

        Node oldGoal = this.findGoal();
        if (oldGoal != null) {
            // Make 'copy.goal' point to the *cloned* node
            copy.goal = copy.grid[oldGoal.row][oldGoal.col];
        }

        return copy;
    }
}
