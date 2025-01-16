package pathfinding;

import java.util.*;

public class Benchmark {

    public static void main(String[] args) {
        // Instantiate Dijkstra once (assuming thread-safe usage)
        Dijkstra dijkstra = new Dijkstra();

        // Grid sizes you want to test
        int[] gridSizes = {20, 60, 100, 140, 180, 220, 260, 300};

        // Obstacle densities in percent
        int[] obstacleDensities = {5, 10, 15, 20, 25, 30, 35, 40,
                45, 50, 55, 60, 65, 70, 75, 80};

        // Number of trials per (gridSize, obstacleDensity) pair
        int trials = 100;

        System.out.println("Running Dijkstra benchmarks...");
        System.out.println("Format: gridSize, obstacleDensity(%), successRate");

        for (int size : gridSizes) {
            for (int density : obstacleDensities) {

                int successCount = 0; // how many times we found a valid path

                for (int t = 0; t < trials; t++) {
                    // Create and set up the grid
                    Grid grid = new Grid(size);

                    // Randomly assign start and goal
                    grid.setRandomStartAndGoal();


                    // Place obstacles randomly
                    grid.setRandomObstacles(density);

                    // Run Dijkstra
                    grid = dijkstra.run(grid);

                    // Construct the path from goal (if it exists)
                    dijkstra.constructPath(grid.findGoal());

                    // Check path length
                    if (grid.getShortestPathCost() > 0) {
                        successCount++;
                    }
                }

                // Compute average success rate
                double successRate = (double) successCount / trials;

                // Print or store results
                // Example format: "Grid: size=20, density=5%, successRate=0.95"
                System.out.printf("Grid=%dx%d, Density=%d%%, SuccessRate=%.3f%n",
                        size, size, density, successRate);
            }
        }
    }
}
