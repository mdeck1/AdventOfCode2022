package com.malcolmdeck.adventofcode2022.levels.level14;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Level14 {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 200;

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level14\\level14data.txt");
        try {
            // Read in grid of line segments
            Scanner scanner = new Scanner(file);
            boolean[][] grid = new boolean[WIDTH][HEIGHT];
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] lines = line.split(" -> ");
                for (int i = 0; i < lines.length - 1; ++i) {
                    fillInSegment(grid, lines[i], lines[i + 1]);
                }
            }
            // Simulate grains of sand falling and count them
            int grainCount = 0;
            while(true) {
                if (!insertAndReturnIfLandsInGrid(grid)) {
                    break;
                }
                ++grainCount;
            }
            System.out.println("Number of grains: " + grainCount);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level14\\level14data.txt");
        try {
            // Read in grid of line segements
            Scanner scanner = new Scanner(file);
            boolean[][] grid = new boolean[WIDTH][HEIGHT];
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] lines = line.split(" -> ");
                for (int i = 0; i < lines.length - 1; ++i) {
                    fillInSegment(grid, lines[i], lines[i + 1]);
                }
            }

            // Insert floor two below the lowest line segment point.
            fillInSegment(grid, "0,174", "999,174");

            // Simulate grains of sand falling and count them
            int grainCount = 0;
            while(true) {
                if (!insertAndReturnIfLandsInGrid(grid)) {
                    break;
                }
                ++grainCount;
            }
            System.out.println("Number of grains: " + grainCount);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    /**
     * Fills in a line segment from the first point to the second point in grid.
     */
    private static void fillInSegment(boolean[][] grid, String firstPoint, String secondPoint) {
        int startX = Integer.parseInt(firstPoint.split(",")[0]);
        int startY = Integer.parseInt(firstPoint.split(",")[1]);
        int endX = Integer.parseInt(secondPoint.split(",")[0]);
        int endY = Integer.parseInt(secondPoint.split(",")[1]);
        if (startX == endX) {
            for (int i = Math.min(startY, endY); i <= Math.max(startY, endY); ++i) {
                grid[startX][i] = true;
            }
        } else {
            for (int i = Math.min(startX, endX); i <= Math.max(startX, endX); ++i) {
                grid[i][startY] = true;
            }
        }
    }

    /**
     * Simulates a grain of sand falling, starting at point 500,0.
     * When the grain of sand comes to rest, it inserts it and returns true;
     * if the grain of sand would fall off the bottom of the grid or the entrance
     * point is already blocked, return false as a sentinel value.
     */
    private static boolean insertAndReturnIfLandsInGrid(boolean[][] grid) {
        int x = 500;
        int y = 0;
        if (grid[x][y] == true) {
            return false;
        }
        while (y < grid[0].length - 1) {
            if (grid[x][y + 1] == false) {
                y += 1;
            } else if (grid[x - 1][y + 1] == false) {
                x -= 1;
                y += 1;
            } else if (grid[x + 1][y + 1] == false) {
                x += 1;
                y += 1;
            } else {
                grid[x][y] = true;
                return true;
            }
        }
        return false;
    }
}
