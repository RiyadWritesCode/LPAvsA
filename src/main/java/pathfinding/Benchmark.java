package pathfinding;

import java.util.List;

public class Benchmark {

    private static final int[] GRID_SIZES = {50};
    private static final int[] OBSTACLE_PERCENTAGES = {5, 10, 15, 20, 25};

    public static void main(String[] args) {
        Dijkstra dijkstra = new Dijkstra();
        AStar astar = new AStar();
        LPAStar lpastar = new LPAStar();

        for (int size : GRID_SIZES) {
            for (int obstaclePct : OBSTACLE_PERCENTAGES) {
                for (int movePct = 1; movePct <= 100; movePct++) {

                    // Create and initialize the grid
                Grid grid = new Grid(size);
                grid.setRandomStartAndGoal();
                grid.setRandomObstacles(obstaclePct);
                grid.gridder();

                // --- INITIAL RUNS ---
                // Dijkstra initial
                long dijkstraStart = System.nanoTime();
                grid = dijkstra.run(grid);
                long dijkstraEnd = System.nanoTime();
                dijkstra.constructPath(grid.findGoal());
                logResults("INITIAL_Dijkstra", size, obstaclePct, grid, dijkstraEnd - dijkstraStart);

                grid.clearGrid(); // Clear pathfinding data

                // A* initial
                long astarStart = System.nanoTime();
                grid = astar.run(grid);
                long astarEnd = System.nanoTime();
                astar.constructPath(grid.findGoal());
                logResults("INITIAL_A*", size, obstaclePct, grid, astarEnd - astarStart);

                grid.clearGrid(); // Clear pathfinding data

                // LPA* initial
                long lpaStart = System.nanoTime();
                grid = lpastar.run(grid);
                long lpaEnd = System.nanoTime();
                lpastar.constructPath(grid, grid.findGoal());
                logResults("INITIAL_LPA*", size, obstaclePct, grid, lpaEnd - lpaStart);

                // --- INCREMENTAL UPDATES ---
                // For each movePct from 1% to 100%, do 99 incremental updates
                    for (int i = 1; i <= 5; i++) {
                        // Move obstacles
                        grid.clearGridLPA();
                        List<Node> updatedNodes = grid.moveObstacles(movePct);

                        // LPA* incremental update
                        long lpaStartInc = System.nanoTime();
                        lpastar.constructPath(grid, grid.findGoal());
                        grid = lpastar.runUpdate(grid, updatedNodes);
                        long lpaEndInc = System.nanoTime();
                        logResults("MOVE_" + movePct + "_LPA*", size, obstaclePct, grid, lpaEndInc - lpaStartInc);

                        // A* from scratch
                        grid.clearGridLPA();
                        long astarStartInc = System.nanoTime();
                        grid = astar.run(grid);
                        long astarEndInc = System.nanoTime();
                        astar.constructPath(grid.findGoal());
                        logResults("MOVE_" + movePct + "_A*", size, obstaclePct, grid, astarEndInc - astarStartInc);

                        // Dijkstra from scratch
                        grid.clearGridLPA();
                        long dijkstraStartInc = System.nanoTime();
                        grid = dijkstra.run(grid);
                        long dijkstraEndInc = System.nanoTime();
                        dijkstra.constructPath(grid.findGoal());
                        logResults("MOVE_" + movePct + "_Dijkstra", size, obstaclePct, grid, dijkstraEndInc - dijkstraStartInc);
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
    private static void logResults(String label, int gridSize, int obstaclePct, Grid grid, long nanos) {
        System.out.println(label + ","
                + gridSize + "x" + gridSize + ","
                + obstaclePct + "%,"
                + grid.pathLength() + ","
                + grid.getShortestPathCost() + ","
                + nanos);
    }
}
