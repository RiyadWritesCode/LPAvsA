package pathfinding;

import java.util.List;

public class Benchmark {

    // Keep these as you had them
    private static final int[] GRID_SIZES = {20, 40, 60, 80, 100, 120, 140, 160, 180, 200};
    private static final int[] OBSTACLE_PERCENTAGES = {5, 10, 15, 20, 25};

    public static void main(String[] args) {
        Dijkstra dijkstra = new Dijkstra();
        AStar astar = new AStar();
        LPAStar lpastar = new LPAStar();

        for (int size : GRID_SIZES) {
            for (int obstaclePct : OBSTACLE_PERCENTAGES) {
                Grid grid = new Grid(size);
                grid.setRandomStartAndGoal();
                grid.setRandomObstacles(obstaclePct);

                System.out.println("======================================================");
                System.out.println("Grid Size: " + size + "x" + size);
                System.out.println("Initial Obstacle %: " + obstaclePct + "%"); // <-- Fixed

                // DIJKSTRA (initial)
                long dijkstraStart = System.nanoTime();
                grid = dijkstra.run(grid);
                long dijkstraEnd = System.nanoTime();
                dijkstra.constructPath(grid.findGoal());
                logResults("INITIAL Dijkstra", grid, dijkstraEnd - dijkstraStart);

                // Clear pathfinding data only (keep obstacles/start/goal)
                grid.clearGrid();

                // ASTAR (initial)
                long astarStart = System.nanoTime();
                grid = astar.run(grid);
                long astarEnd = System.nanoTime();
                astar.constructPath(grid.findGoal());
                logResults("INITIAL A*", grid, astarEnd - astarStart);

                // Clear pathfinding data only
                grid.clearGrid();

                // LPA* (initial)
                long lpaStart = System.nanoTime();
                grid = lpastar.run(grid);
                long lpaEnd = System.nanoTime();
                lpastar.constructPath(grid, grid.findGoal());
                logResults("INITIAL LPA*", grid, lpaEnd - lpaStart);

                // Now the incremental updates
                for (int i = 1; i <= 99; i++) {
                    // Move some obstacles
                    grid.clearGridLPA();
                    List<Node> updatedNodes = grid.moveObstacles(5);

                    // LPA* incremental update
                    long lpaStartInc = System.nanoTime();
                    lpastar.constructPath(grid, grid.findGoal());
                    grid = lpastar.runUpdate(grid, updatedNodes);
                    long lpaEndInc = System.nanoTime();
                    logResults("MOVE " + 5 + "% LPA*", grid, lpaEndInc - lpaStartInc);

                    grid.clearGridLPA();

                    // A* from scratch
                    long astarStartInc = System.nanoTime();
                    grid = astar.run(grid);
                    long astarEndInc = System.nanoTime();
                    astar.constructPath(grid.findGoal());
                    logResults("MOVE " + 5 + "% A*", grid, astarEndInc - astarStartInc);


                    // Clear pathfinding data only
                    grid.clearGridLPA();
// DIJKSTRA from scratch
                    long dijkstraStartInc = System.nanoTime();
                    grid = dijkstra.run(grid);
                    long dijkstraEndInc = System.nanoTime();
                    dijkstra.constructPath(grid.findGoal());
                    logResults("MOVE " + 5 + "% Dijkstra", grid, dijkstraEndInc - dijkstraStartInc);

                }
            }
        }

        System.out.println("All benchmarks complete.");
    }

    private static void logResults(String label, Grid grid, long nanos) {
        System.out.println("---------------------------------");
        System.out.println("[" + label + "] Path Length: " + grid.pathLength());
        System.out.println("[" + label + "] Path Cost:   " + grid.getShortestPathCost());
        System.out.println("[" + label + "] Time (ns):   " + nanos);
    }
}
