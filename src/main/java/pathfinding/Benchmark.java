package pathfinding;

public class Benchmark {

    public static void main(String[] args) {
        Grid grid;
        Dijkstra dijkstra;
        AStar astar;
        LPAStar lpastar;

        dijkstra = new Dijkstra();
        astar = new AStar();
        lpastar = new LPAStar();
        grid = new Grid(50);

        grid.setRandomStartAndGoal();
        grid.setRandomObstacles(40);

        grid = dijkstra.run(grid);
        System.out.println(grid.pathLength());

        grid.clearGrid();

        grid = astar.run(grid);
        System.out.println(grid.pathLength());

        grid.clearGrid();
        grid = lpastar.run(grid);
        System.out.println(grid.pathLength());


    }
}
