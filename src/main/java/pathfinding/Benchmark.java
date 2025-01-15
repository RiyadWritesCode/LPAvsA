package pathfinding;

import java.util.*;

public class Benchmark {

    public static void main(String[] args) {
        // Create instances of the algorithms
        Dijkstra dijkstra = new Dijkstra();
        AStar astar = new AStar();
        LPAStar lpastar = new LPAStar();

        // Create and set up the grid
        Grid grid = new Grid(200);
        grid.setRandomStartAndGoal();
        grid.setRandomObstacles(40);

        // 1) Dijkstra
        long dijkstraStart = System.nanoTime();
        grid = dijkstra.run(grid);
        long dijkstraEnd = System.nanoTime();
        System.out.println("Dijkstra path length: " + grid.pathLength());
        System.out.println("Dijkstra path cost: " + grid.getShortestPathCost());
        System.out.println("Dijkstra took " + (dijkstraEnd - dijkstraStart) + " ns");

        // Clear grid for next algorithm
        grid.clearGrid();

        // 2) A*
        long astarStart = System.nanoTime();
        grid = astar.run(grid);
        long astarEnd = System.nanoTime();
        System.out.println("A* path length: " + grid.pathLength());
        System.out.println("A* path cost: " + grid.getShortestPathCost());
        System.out.println("A* took " + (astarEnd - astarStart) + " ns");

        // Clear grid for next algorithm
        grid.clearGrid();

        // 3) LPA*
        long lpaStart = System.nanoTime();
        grid = lpastar.run(grid);
        long lpaEnd = System.nanoTime();
        System.out.println("LPA* path length: " + grid.pathLength());
        System.out.println("LPA* path cost: " + grid.getShortestPathCost());
        System.out.println("LPA* took " + (lpaEnd - lpaStart) + " ns");

        // Move obstacles and update LPA* again
        List<Node> updatedNodes = grid.moveObstacles(10);

        long lpaUpdateStart = System.nanoTime();
        grid = lpastar.runUpdate(grid, updatedNodes);
        long lpaUpdateEnd = System.nanoTime();
        System.out.println("LPA* (after update) path length: " + grid.pathLength());
        System.out.println("LPA* (after update) path cost: " + grid.getShortestPathCost());
        System.out.println("LPA* update took " + (lpaUpdateEnd - lpaUpdateStart) + " ns");
    }
}
