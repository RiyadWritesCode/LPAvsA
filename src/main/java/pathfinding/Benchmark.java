package pathfinding;

import java.util.List;

public class Benchmark {

    // Keep these as you had them
    private static final int[] GRID_SIZES = {20, 40, 60, 80, 100, 120, 140, 160, 180, 200};
    private static final int[] OBSTACLE_PERCENTAGES = {5, 10, 15, 20, 25};
    private static final int MOVES_COUNT = 99;

    public static void main(String[] args) {
        Dijkstra dijkstra = new Dijkstra();
        AStar astar = new AStar();
        LPAStar lpastar = new LPAStar();

        for (int size : GRID_SIZES) {
            for (int obstaclePct : OBSTACLE_PERCENTAGES) {
                // Create a brand-new grid
                Grid baseGrid = new Grid(size);
                baseGrid.setRandomStartAndGoal();
                baseGrid.setRandomObstacles(obstaclePct);

                System.out.println("======================================================");
                System.out.println("Grid Size: " + size + "x" + size);
                System.out.println("Initial Obstacle %: " + obstaclePct + "%"); // <-- Fixed

                // DIJKSTRA (initial)
                long dijkstraStart = System.nanoTime();
                baseGrid = dijkstra.run(baseGrid);
                long dijkstraEnd = System.nanoTime();
                dijkstra.constructPath(baseGrid.findGoal());
                logResults("INITIAL Dijkstra", baseGrid, dijkstraEnd - dijkstraStart);

                // Clear pathfinding data only (keep obstacles/start/goal)
                baseGrid.clearGrid();

                // ASTAR (initial)
                long astarStart = System.nanoTime();
                baseGrid = astar.run(baseGrid);
                long astarEnd = System.nanoTime();
                astar.constructPath(baseGrid.findGoal());
                logResults("INITIAL A*", baseGrid, astarEnd - astarStart);

                // Clear pathfinding data only
                baseGrid.clearGrid();

                // LPA* (initial)
                long lpaStart = System.nanoTime();
                baseGrid = lpastar.run(baseGrid);
                long lpaEnd = System.nanoTime();
                lpastar.constructPath(baseGrid, baseGrid.findGoal());
                logResults("INITIAL LPA*", baseGrid, lpaEnd - lpaStart);

                // Now the incremental updates
                for (int movePct = 1; movePct <= MOVES_COUNT; movePct++) {
                    // Move some obstacles
                    List<Node> updatedNodes = baseGrid.moveObstacles(movePct);
                    Grid clonedGrid = baseGrid.clone();
                    clonedGrid.clearGrid();

                    // LPA* incremental update
                    long lpaStartInc = System.nanoTime();
                    baseGrid = lpastar.runUpdate(baseGrid, updatedNodes);
                    long lpaEndInc = System.nanoTime();
                    lpastar.constructPath(baseGrid, baseGrid.findGoal());
                    logResults("MOVE " + movePct + "% LPA*", baseGrid, lpaEndInc - lpaStartInc);

                    // A* from scratch
                    long astarStartInc = System.nanoTime();
                    clonedGrid = astar.run(clonedGrid);
                    long astarEndInc = System.nanoTime();
                    astar.constructPath(clonedGrid.findGoal());
                    logResults("MOVE " + movePct + "% A*", clonedGrid, astarEndInc - astarStartInc);



                    // Clear pathfinding data only
                    clonedGrid.clearGrid();
// DIJKSTRA from scratch
                    long dijkstraStartInc = System.nanoTime();
                    clonedGrid = dijkstra.run(clonedGrid);
                    long dijkstraEndInc = System.nanoTime();
                    dijkstra.constructPath(clonedGrid.findGoal());
                    logResults("MOVE " + movePct + "% Dijkstra", clonedGrid, dijkstraEndInc - dijkstraStartInc);

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
