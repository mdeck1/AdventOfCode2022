package com.malcolmdeck.adventofcode2022.levels.level18;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Level18 {

    private static final boolean DEBUG = true;

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level18\\level18data.txt");
        try {
            // Read in all coordinates, insert into set
            Scanner scanner = new Scanner(file);
            Set<Integer> dropletSet = new HashSet<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] coordinates = line.split(",");
                int x = Integer.parseInt(coordinates[0]);
                int y = Integer.parseInt(coordinates[1]);
                int z = Integer.parseInt(coordinates[2]);
                dropletSet.add((x * 100 * 100) + (y * 100) + z);
            }
            // For each coordinate, if its neighbor in each direction isn't there, add 1 to total surface area.
            int surfaceArea = 0;
            for (Integer droplet : dropletSet) {
                int x = droplet / 100 / 100;
                int y = (droplet % 10000) / 100;
                int z = (droplet % 100);
                // x++
                if (!dropletSet.contains(((x+1) * 100 * 100) + (y * 100) + z)) {
                    surfaceArea++;
                }
                // x--
                if (!dropletSet.contains(((x-1) * 100 * 100) + (y * 100) + z)) {
                    surfaceArea++;
                }
                // y++
                if (!dropletSet.contains(((x) * 100 * 100) + ((y+1) * 100) + z)) {
                    surfaceArea++;
                }
                // y--
                if (!dropletSet.contains(((x) * 100 * 100) + ((y-1) * 100) + z)) {
                    surfaceArea++;
                }
                // z++
                if (!dropletSet.contains(((x) * 100 * 100) + (y * 100) + z + 1)) {
                    surfaceArea++;
                }
                // z--
                if (!dropletSet.contains(((x) * 100 * 100) + (y * 100) + z - 1)) {
                    surfaceArea++;
                }
            }
            System.out.println("Surface Area: "  + surfaceArea);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level18\\level18data.txt");
        try {
            // Read in all coordinates, offset by 1 so we don't get edge weirdness, put into set and grid.
            Scanner scanner = new Scanner(file);
            Set<Integer> dropletSet = new HashSet<>();
            Integer[][][] cube = new Integer[100][100][100];
            for (int i = 0; i < 100; ++i) {
                for (int j = 0; j < 100; ++j) {
                    for (int k = 0; k < 100; ++k) {
                        cube[i][j][k] = 0;
                    }
                }
            }
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] coordinates = line.split(",");
                int x = Integer.parseInt(coordinates[0]) + 1;
                int y = Integer.parseInt(coordinates[1]) + 1;
                int z = Integer.parseInt(coordinates[2]) + 1;
                cube[x][y][z] = null;
                dropletSet.add((x * 100 * 100) + (y * 100) + z);
            }
            // Flood fill the external volume with the value 1
            floodFill(cube);

            if (DEBUG) {
                countLavaBits(cube);
                countOutsideBits(cube);
                countInsideBits(cube);
            }

            // Turn the droplet from null to 0
            turnNullsToZeroes(cube);
            // For each droplet, add the value in the grid to its surface area.
            // This works because other droplets and interior sections will still have value 0, but
            // the exterior was floodfilled to have value 1.
            int surfaceArea = 0;
            for (Integer droplet : dropletSet) {
                int x = droplet / 100 / 100;
                int y = (droplet % 10000) / 100;
                int z = (droplet % 100);
                // x++
                    surfaceArea += cube[x+1][y][z];
                // x--
                    surfaceArea += cube[x-1][y][z];
                // y++
                    surfaceArea += cube[x][y+1][z];
                // y--
                    surfaceArea += cube[x][y-1][z];
                // z++
                    surfaceArea += cube[x][y][z+1];
                // z--
                    surfaceArea += cube[x][y][z-1];
            }
            System.out.println("Surface Area: "  + surfaceArea);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    // Flood fill the exterior of the given grid with 1s, stopping anytime you find
    // a null, since that implies the cell is already taken, or a 1, since you don't want
    // to be flood filling forever.
    private static void floodFill(Integer[][][] cube) {
        Queue<Integer> coordinateQueue = new ArrayDeque<>();
        coordinateQueue.add(99*100*100+99*100+99);
        while (!coordinateQueue.isEmpty()) {
            int currentElement = coordinateQueue.poll();
            int x = currentElement / 10000;
            int y = (currentElement % 10000) / 100;
            int z = currentElement % 100;
            if (x < 0 || x > 99 || y < 0 || y > 99 || z < 0 || z > 99) {
                continue;
            }
            if (cube[x][y][z] == null || cube[x][y][z] == 1) {
                continue;
            }
            cube[x][y][z] = 1;
            coordinateQueue.add((x+1)*100*100 + y * 100 + z);
            coordinateQueue.add((x-1)*100*100 + y * 100 + z);
            coordinateQueue.add(x*100*100 + (y+1) * 100 + z);
            coordinateQueue.add(x*100*100 + (y-1) * 100 + z);
            coordinateQueue.add(x*100*100 + y * 100 + z + 1);
            coordinateQueue.add(x*100*100 + y * 100 + z - 1);
        }
        return;
    }

    private static void turnNullsToZeroes(Integer[][][] cube) {
        for (int i = 0; i < 100; ++i) {
            for (int j = 0; j < 100; ++j) {
                for (int k = 0; k < 100; ++k) {
                    if (cube[i][j][k] == null) {
                        cube[i][j][k] = 0;
                    }
                }
            }
        }
    }

    private static void countLavaBits(Integer[][][] cube) {
        int sum = 0;
        for (int i = 0; i < 100; ++i) {
            for (int j = 0; j < 100; ++j) {
                for (int k = 0; k < 100; ++k) {
                    if (cube[i][j][k] == null) {
                        sum++;
                    }
                }
            }
        }
        System.out.println("Number of lava bits from grid: " + sum);
    }

    private static void countOutsideBits(Integer[][][] cube) {
        int sum = 0;
        for (int i = 0; i < 100; ++i) {
            for (int j = 0; j < 100; ++j) {
                for (int k = 0; k < 100; ++k) {
                    if (cube[i][j][k] != null && cube[i][j][k] == 1) {
                        sum++;
                    }
                }
            }
        }
        System.out.println("Number of outside bits from grid: " + sum);
    }

    private static void countInsideBits(Integer[][][] cube) {
        int sum = 0;
        for (int i = 0; i < 100; ++i) {
            for (int j = 0; j < 100; ++j) {
                for (int k = 0; k < 100; ++k) {
                    if (cube[i][j][k] != null && cube[i][j][k] == 0) {
                        sum++;
                        System.out.println("Inside bit: (" + i + ", " + j + ", " + k + ")");
                    }
                }
            }
        }
        System.out.println("Number of inside bits from grid: " + sum);
    }
}
