package pathfinding;
import java.util.*;

public class Benchmark {

    public static void main(String[] args) {
        Grid grid;
        Dijkstra dijkstra;
        AStar astar;
        LPAStar lpastar;

        dijkstra = new Dijkstra();
        astar = new AStar();
        lpastar = new LPAStar();
        grid = new Grid(200);

        grid.setRandomStartAndGoal();
        grid.setRandomObstacles(40);

        grid = dijkstra.run(grid);
        System.out.println(grid.pathLength());
        System.out.println(grid.getShortestPathCost());


        grid.clearGrid();

        grid = astar.run(grid);
        System.out.println(grid.pathLength());
        System.out.println(grid.getShortestPathCost());

        grid.clearGrid();
        grid = lpastar.run(grid);
        System.out.println(grid.pathLength());
        System.out.println(grid.getShortestPathCost());

        List<Node> updatedNodes = grid.moveObstacles(10);
        grid = lpastar.runUpdate(grid, updatedNodes);
        System.out.println(grid.pathLength());
        System.out.println(grid.getShortestPathCost());
    }
}
