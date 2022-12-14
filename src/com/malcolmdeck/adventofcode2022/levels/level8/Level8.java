package com.malcolmdeck.adventofcode2022.levels.level8;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Level8 {

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level8\\level8data.txt");
        try {
            // Read in file
            Scanner scanner = new Scanner(file);
            int WIDTH = 99;
            int HEIGHT = 99;
            int[][] treeGrid = new int[HEIGHT][WIDTH];
            int whichLine = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                for (int i = 0 ; i < line.length(); ++i) {
                    treeGrid[whichLine][i] = Integer.parseInt(String.valueOf(line.charAt(i)));
                }
                whichLine++;
            }
            boolean[][] treeVisible = new boolean[HEIGHT][WIDTH];
            // Look down each column from the top
            for (int i = 0; i < WIDTH; ++i) {
                int tallestSoFar = -1;
                for (int j = 0; j < HEIGHT; ++j) {
                    if (treeGrid[j][i] > tallestSoFar) {
                        treeVisible[j][i] = true;
                        tallestSoFar = treeGrid[j][i];
                    }
                }
            }
            // Look up each column from the bottom
            for (int i = WIDTH - 1; i > -1; --i) {
                int tallestSoFar = -1;
                for (int j = HEIGHT - 1; j > -1; --j) {
                    if (treeGrid[j][i] > tallestSoFar) {
                        treeVisible[j][i] = true;
                        tallestSoFar = treeGrid[j][i];
                    }
                }
            }
            // Look across each row from the left
            for (int i = 0; i < HEIGHT; ++i) {
                int tallestSoFar = -1;
                for (int j = 0; j < WIDTH; ++j) {
                    if (treeGrid[i][j] > tallestSoFar) {
                        treeVisible[i][j] = true;
                        tallestSoFar = treeGrid[i][j];
                    }
                }
            }
            // Look across each row from the right
            for (int i = HEIGHT - 1; i > -1; --i) {
                int tallestSoFar = -1;
                for (int j = WIDTH - 1; j > -1; --j) {
                    if (treeGrid[i][j] > tallestSoFar) {
                        treeVisible[i][j] = true;
                        tallestSoFar = treeGrid[i][j];
                    }
                }
            }
            // Sum all visible trees
            int count = 0;
            for (int i = 0; i < HEIGHT; ++i) {
                for (int j = 0; j < WIDTH; ++j) {
                    if (treeVisible[i][j]) {
                        ++count;
                    }
                }
            }
            System.out.println("Trees visible: " + count);
        } catch (Exception e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level8\\level8data.txt");
        try {
            // Read in tree grid
            Scanner scanner = new Scanner(file);
            int WIDTH = 99;
            int HEIGHT = 99;
            int[][] treeGrid = new int[HEIGHT][WIDTH];
            int whichLine = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                for (int i = 0 ; i < line.length(); ++i) {
                    treeGrid[whichLine][i] = Integer.parseInt(String.valueOf(line.charAt(i)));
                }
                whichLine++;
            }
            // Compute each scenic score, keeping track of the maximum as we go along
            int maxScore = -1;
            for (int i = 1; i < HEIGHT - 1; ++i) {
                for (int j = 1; j < WIDTH - 1; ++j) {
                    int treeScore = getScenicScore(treeGrid, i, j);
                    if (treeScore > maxScore) {
                        maxScore = treeScore;
                    }
                }
            }
            System.out.println("Max scenic score found: " + maxScore);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    /**
     * Compute the scenic score for the tree at index (x, y)
     */
    private static int getScenicScore(int[][] treeGrid, int y, int x) {
        int HEIGHT = treeGrid.length;
        int WIDTH = treeGrid[0].length;
        int currentHeight = treeGrid[y][x];
        int upScore = 0;
        int downScore = 0;
        int leftScore = 0;
        int rightScore = 0;
        // Count the tree visible to the north of it
        for (int i = y-1; i > -1; --i) {
            upScore++;
            if (treeGrid[i][x] >= currentHeight) {
                break;
            }
        }
        // Count the trees visible to the south of it
        for (int i = y+1; i < HEIGHT; ++i) {
            downScore++;
            if (treeGrid[i][x] >= currentHeight) {
                break;
            }
        }
        // Count the trees visible to the west of it
        for (int i = x-1; i > -1; --i) {
            leftScore++;
            if (treeGrid[y][i] >= currentHeight) {
                break;
            }
        }
        // Count the trees visible to the east of it
        for (int i = x+1; i < WIDTH; ++i) {
            rightScore++;
            if (treeGrid[y][i] >= currentHeight) {
                break;
            }
        }
        // Return the four counts multiplied together to get the score.
        return upScore * downScore * leftScore * rightScore;
    }
}
