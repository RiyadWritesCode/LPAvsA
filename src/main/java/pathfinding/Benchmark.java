package pathfinding;
import java.util.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class Benchmark {
    public static void main(String[] args) {
        Dijkstra dijkstra = new Dijkstra();
        AStar astar = new AStar();
        LPAStar lpastar = new LPAStar();

        // Create and set up the grid
        Grid grid = new Grid(100);
        grid.setRandomStartAndGoal();
        grid.setRandomObstacles(20);

        Runtime runtime = Runtime.getRuntime();
        System.out.println(runtime.totalMemory()-runtime.freeMemory());

        // 1) Dijkstra
        long dijkstraMemoryBefore = getUsedMemory();
        long dijkstraStart = System.nanoTime();
        grid = dijkstra.run(grid);
        long dijkstraEnd = System.nanoTime();
        long dijkstraMemoryAfter = getUsedMemory();
        dijkstra.constructPath(grid.findGoal());
        long dijkstraMemoryUsed = dijkstraMemoryAfter - dijkstraMemoryBefore;
        System.out.println("Dijkstra path length: " + grid.pathLength());
        System.out.println("Dijkstra path cost: " + grid.getShortestPathCost());
        System.out.println("Dijkstra used memory: " + dijkstraMemoryUsed + " bytes");
        System.out.println("Dijkstra took " + (dijkstraEnd - dijkstraStart)/1000000 + " ms");
        System.out.println(runtime.totalMemory()-runtime.freeMemory());

        grid.clearGrid();

        // 2) A*
        long astarStart = System.nanoTime();
        grid = astar.run(grid);
        long astarEnd = System.nanoTime();
        astar.constructPath(grid.findGoal());
        System.out.println("A* path length: " + grid.pathLength());
        System.out.println("A* path cost: " + grid.getShortestPathCost());
        System.out.println("A* took " + (astarEnd - astarStart)/1000000 + " ms");
        System.out.println(runtime.totalMemory()-runtime.freeMemory());

        grid.clearGrid();

        // 3) LPA*
        long lpaStart = System.nanoTime();
        grid = lpastar.run(grid);
        long lpaEnd = System.nanoTime();
        lpastar.constructPath(grid, grid.findGoal());
        System.out.println("LPA* path length: " + grid.pathLength());
        System.out.println("LPA* path cost: " + grid.getShortestPathCost());
        System.out.println("LPA* took " + (lpaEnd - lpaStart)/1000000 + " ms");
        System.out.println(runtime.totalMemory()-runtime.freeMemory());

        // Move obstacles and update LPA* again
        List<Node> updatedNodes = grid.moveObstacles(1);

        long lpaUpdateStart = System.nanoTime();
        grid = lpastar.runUpdate(grid, updatedNodes);
        long lpaUpdateEnd = System.nanoTime();
        lpastar.constructPath(grid, grid.findGoal());
        System.out.println("LPA* (after update) path length: " + grid.pathLength());
        System.out.println("LPA* (after update) path cost: " + grid.getShortestPathCost());
        System.out.println("LPA* update took " + (lpaUpdateEnd - lpaUpdateStart)/1000000 + " ms");
        System.out.println(runtime.totalMemory()-runtime.freeMemory());
    }
}
