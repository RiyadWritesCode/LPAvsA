package pathfinding;

public class Node {
    public boolean visited;
    Node parent = null;

    public int distance = 999999999;
    public boolean isObstacle = false;
    public boolean isStart = false;
    public boolean isGoal = false;
    public boolean isShortestPath = false;
    public boolean inQ = false;

    public int row, col;
    public int lG = 999999999;
    public int lRHS = 999999999;
    public int aG;
    public int aH;
    public int aF;

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // Returns array of min(rhs,g)+h and min(rhs,g)
    public int[] lCalculateKey(Node goal) {
        int[] key = new int[2];
        int minGRHS = Math.min(lG, lRHS);
        key[0] = minGRHS + this.heuristic(goal); // h(x)
        key[1] = minGRHS;
        return key;
    }

    public int heuristic(Node goal) {
        int dx = Math.abs(this.col - goal.col);
        int dy = Math.abs(this.row - goal.row);

        return 10 * (dx + dy);
    }
}
