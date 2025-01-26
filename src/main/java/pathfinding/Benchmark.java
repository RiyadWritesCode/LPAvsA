package pathfinding;

import java.util.*;

public class Benchmark {

    private static final int[] GRID_SIZES = {25};
    private static final int[] OBSTACLE_PERCENTAGES = {30};

    public static void main(String[] args) {
        Dijkstra dijkstra = new Dijkstra();
        AStar astar = new AStar();
        LPAStar lpastar = new LPAStar();

        for (int size : GRID_SIZES) {
            for (int obstaclePct : OBSTACLE_PERCENTAGES) {
                for (double movePct = 0; movePct <= 10; movePct+=0.1) {

                    Grid grid = new Grid(size);
                    grid.setRandomStartAndGoal();
                    grid.setRandomObstacles(obstaclePct);

                    // --- INITIAL RUNS ---
                    // Dijkstra initial
                    long dijkstraStart = System.nanoTime();
                    dijkstra.run(grid);
                    long dijkstraEnd = System.nanoTime();
                    dijkstra.constructPath(grid.findGoal());
                    logResults("INITIAL_Dijkstra", size, obstaclePct, grid, dijkstraEnd - dijkstraStart);

                    grid.clearGrid(); // Clear pathfinding data

                    // A* initial
                    long astarStart = System.nanoTime();
                    astar.run(grid);
                    long astarEnd = System.nanoTime();
                    astar.constructPath(grid.findGoal());
                    logResults("INITIAL_A*", size, obstaclePct, grid, astarEnd - astarStart);

                    grid.clearGrid(); // Clear pathfinding data

                    // LPA* initial
                    long lpaStart = System.nanoTime();
                    lpastar.run(grid);
                    long lpaEnd = System.nanoTime();
                    lpastar.constructPath(grid, grid.findGoal());
                    logResults("INITIAL_LPA*", size, obstaclePct, grid, lpaEnd - lpaStart);

                    // --- INCREMENTAL UPDATES ---
                    // For each movePct from 1% to 100%, do 99 incremental updates
                    for (int i = 1; i <= 10; i++) {
                        // Move obstacles
                        List<Node> updatedNodes = grid.moveObstacles(movePct);
                        // Dijkstra from scratch
                        grid.clearGridLPA();
                        long dijkstraStartInc = System.nanoTime();
                        dijkstra.run(grid);
                        long dijkstraEndInc = System.nanoTime();
                        dijkstra.constructPath(grid.findGoal());
                        logResults("MOVE_" + movePct + "_Dijkstra", size, obstaclePct, grid, dijkstraEndInc - dijkstraStartInc);


                        // A* from scratch
                        grid.clearGridLPA();
                        long astarStartInc = System.nanoTime();
                        astar.run(grid);
                        long astarEndInc = System.nanoTime();
                        astar.constructPath(grid.findGoal());
                        logResults("MOVE_" + movePct + "_A*", size, obstaclePct, grid, astarEndInc - astarStartInc);

                        grid.clearGridLPA();
                        // LPA* incremental update
                        long lpaStartInc = System.nanoTime();
                        lpastar.runUpdate(grid, updatedNodes);
                        long lpaEndInc = System.nanoTime();
                        lpastar.constructPath(grid, grid.findGoal());
                        logResults("MOVE_" + movePct + "_LPA*", size, obstaclePct, grid, lpaEndInc - lpaStartInc);
                    }
                }
            }
        }

        System.out.println("All benchmarks complete.");
    }

    /**
     * Logs results in a single line:
     * Format: label, gridSize, obstaclePct, pathLength, pathCost, timeNs
     */
    private static void logResults(String label, int gridSize, double obstaclePct, Grid grid, long nanos) {
        System.out.println(label + ","
                + gridSize + "x" + gridSize + ","
                + obstaclePct + "%,"
                + grid.pathLength() + ","
                + grid.getShortestPathCost() + ","
                + nanos);
    }


}
