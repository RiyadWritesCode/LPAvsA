package pathfinding;

public class Node {
    Node parent = null;

    public boolean isObstacle = false;
    public boolean isStart = false;
    public boolean isGoal = false;
    public boolean isShortestPath = false;

    public int row, col;
    public int lG;
    public int lRHS;

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
