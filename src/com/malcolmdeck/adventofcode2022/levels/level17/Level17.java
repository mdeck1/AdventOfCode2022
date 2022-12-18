package com.malcolmdeck.adventofcode2022.levels.level17;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Clock;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Level17 {

    private static final boolean DEBUG = false;


    private static final int HEIGHT = 10000;
    private static final int WIDTH = 7;

    private static final int CACHE_NAME_LENGTH = 2000;

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level17\\level17data.txt");
        try {
            Scanner scanner = new Scanner(file);
            String instructionList = scanner.nextLine();
            boolean[][] grid = new boolean[HEIGHT][WIDTH];
            RockGenerator rockGenerator = RockGenerator.getInstance();
            int y = -1;
            int windDirection = 0;
            for (int i = 0; i < 2022; ++i) {
                // Figure out where new rock should start
                Rock rock = rockGenerator.getNextRock(grid, y + 4);
                int thisRocksHeight;
                while (true) {
                    // Simulate wind push
                    rock.move(instructionList.charAt(windDirection));
                    windDirection = (windDirection + 1) % instructionList.length();
                    // Simulate fall
                    thisRocksHeight = rock.drop();
                    // Break if rock could not move down; put rock in array
                    if (thisRocksHeight != -1) {
                        break;
                    }
                }
                if (thisRocksHeight > y) {
                    y = thisRocksHeight;
                }
            }
            if (DEBUG) {
                drawGridUpTo(grid, y);
            }
            System.out.println("Height: " + (y+1));
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level17\\level17data.txt");
        try {
            Scanner scanner = new Scanner(file);
            String instructionList = scanner.nextLine();
            boolean[][] grid = new boolean[HEIGHT][WIDTH];
            RockGenerator rockGenerator = RockGenerator.getInstance();
            int y = -1;
            int windDirection = 0;
            long numberOfHeightAdjusts = 0l;
            long distanceOfHeightAdjust = 0l;
            boolean cacheAlreadyHit = false;

            Map<String, HeightAndNumBricks> cache = new HashMap<>();

            for (long i = 0; i < 1000000000000l; ++i) {
                // Check the cache for periodicity; if we find it, skip all of the middle
                String cacheCheckName = getCacheNameFromGrid(grid, y);
                if (!cacheAlreadyHit && cacheCheckName != null && cache.containsKey(cacheCheckName)) {
                    cacheAlreadyHit = true;
                    HeightAndNumBricks cachedHeightAndNumBricks = cache.get(cacheCheckName);
                    distanceOfHeightAdjust = y - cachedHeightAndNumBricks.height;
                    long numBricksDuringPeriod = i - cachedHeightAndNumBricks.numBricks;
                    while (i < 1000000000000l) {
                        i += numBricksDuringPeriod;
                        numberOfHeightAdjusts++;
                    }
                    i -= numBricksDuringPeriod;
                    numberOfHeightAdjusts--;
                } else if (!cacheAlreadyHit && cacheCheckName != null) {
                    cache.put(cacheCheckName, new HeightAndNumBricks(y, i));
                }

                // Figure out where new rock should start
                Rock rock = rockGenerator.getNextRock(grid, y + 4);
                int thisRocksHeight = y;
                while (true) {
                    // Simulate wind push
                    rock.move(instructionList.charAt(windDirection));
                    windDirection = (windDirection + 1) % instructionList.length();
                    // Simulate fall
                    thisRocksHeight = rock.drop();
                    // Break if rock could not move down; put rock in array
                    if (thisRocksHeight != -1) {
                        break;
                    }
                }
                if (thisRocksHeight > y) {
                    y = thisRocksHeight;
                }

                if (DEBUG) {
                    drawGridUpTo(grid, y);
                }
            }
            long height = y + 1;
            height += numberOfHeightAdjusts * distanceOfHeightAdjust;
            System.out.println("Height: " + height);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    private static String getCacheNameFromGrid(boolean[][] grid, int y) {
        if (y < CACHE_NAME_LENGTH) {
            return null;
        }
        String cacheName = "";
        for (int i = 0; i < CACHE_NAME_LENGTH; ++i) {
            int value = 0;
            for (int x = 0; x < WIDTH; ++x) {
                value <<= 1;
                if (grid[y-i][x]) {
                    value += 1;
                }
            }
            cacheName += value + ".";
        }
        return cacheName;
    }

    private static class HeightAndNumBricks {
        int height;
        long numBricks;

        HeightAndNumBricks(int height, long numBricks) {
            this.height = height;
            this.numBricks = numBricks;
        }
    }

    public static void lookAtOutput() throws Exception {
        File file = FileHelper.getFile("level17\\level17outputdata.txt");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int x = 0;
            for (int i = 0; i < line.length(); ++i) {
                x <<= 1;
                if (line.charAt(i) == '#') {
                    x += 1;
                }
            }
            System.out.println(x);
        }
    }

    private static void drawGridUpTo(boolean[][] grid, int y) {
        for (int i = y; i > -1; --i) {
            for (int x = 0; x < WIDTH; ++x) {
                if (grid[i][x]) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
        System.out.println("-------");
        System.out.println();
    }

    private static class RockGenerator {
        private static RockGenerator instance = new RockGenerator();
        private static int whichRock = 0;

        private RockGenerator() {}

        static RockGenerator getInstance() {
            return instance;
        }

        Rock getNextRock(boolean[][]grid, int y) {
            Rock rock;
            switch (whichRock) {
                case 0:
                    rock = new HorizontalRock(grid, y);
                    break;
                case 1:
                    rock = new PlusRock(grid, y);
                    break;
                case 2:
                    rock = new LRock(grid, y);
                    break;
                case 3:
                    rock = new VertRock(grid, y);
                    break;
                case 4:
                    rock = new SquareRock(grid, y);
                    break;
                default:
                    throw new RuntimeException("getNextRock() failed");
            }
            whichRock++;
            whichRock %= 5;
            return rock;
        }

    }

    private interface Rock {
        // The rock moves itself the given direction, if possible
        void move(Character direction);

        // The rock moves itself down and returns -1;
        // Otherwise, inserts itself into grid and
        // returns the height of its highest element.
        int drop();
    }


    /**
     * Rock with shape
     * ####
     */
    private static class HorizontalRock implements Rock {
        private boolean[][] grid;
        private int y;
        private int x;

        HorizontalRock(boolean[][] grid, int height) {
            this.grid = grid;
            this.y = height;
            this.x = 2;
        }

        @Override
        public void move(Character direction) {
            if (direction == '<') {
                if (x == 0) {
                    return;
                }
                if (grid[y][x-1]) {
                    return;
                }
                x--;
                return;
            } else if (direction == '>') {
                if (x == WIDTH - 4) {
                    return;
                }
                if (grid[y][x+4]) {
                    return;
                }
                x++;
                return;
            }
        }

        @Override
        public int drop() {
            if (y == 0 ||
                    grid[y-1][x] ||
                    grid[y-1][x+1] ||
                    grid[y-1][x+2] ||
                    grid[y-1][x+3]) {
                grid[y][x] = true;
                grid[y][x+1] = true;
                grid[y][x+2] = true;
                grid[y][x+3] = true;
                return y;
            } else {
                y--;
                return -1;
            }
        }
    }

    /**
     * Rock with shape
     * .#.
     * ###
     * .#.
     */
    private static class PlusRock implements Rock {
        private boolean[][] grid;
        private int y;
        private int x;

        PlusRock(boolean[][] grid, int height) {
            this.grid = grid;
            this.y = height;
            this.x = 2;
        }

        @Override
        public void move(Character direction) {
            if (direction == '<') {
                if (x == 0) {
                    return;
                }
                if (grid[y+2][x] ||
                        grid[y+1][x-1] ||
                        grid[y][x]) {
                    return;
                }
                x--;
                return;
            } else if (direction == '>') {
                if (x == WIDTH - 3) {
                    return;
                }
                if (grid[y+2][x+2] ||
                        grid[y+1][x+3] ||
                        grid[y][x+2]) {
                    return;
                }
                x++;
                return;
            }
        }

        @Override
        public int drop() {
            if (y == 0 ||
                    grid[y][x] ||
                    grid[y-1][x+1] ||
                    grid[y][x+2]) {
                grid[y][x+1] = true;
                grid[y+1][x] = true;
                grid[y+1][x+1] = true;
                grid[y+1][x+2] = true;
                grid[y+2][x+1] = true;
                return y+2;
            } else {
                y--;
                return -1;
            }
        }
    }

    /**
     * Rock with shape
     * ..#
     * ..#
     * ###
     */
    private static class LRock implements Rock {
        private boolean[][] grid;
        private int y;
        private int x;

        LRock(boolean[][] grid, int height) {
            this.grid = grid;
            this.y = height;
            this.x = 2;
        }

        @Override
        public void move(Character direction) {
            if (direction == '<') {
                if (x == 0) {
                    return;
                }
                if (grid[y+2][x+1] ||
                        grid[y+1][x+1] ||
                        grid[y][x-1]) {
                    return;
                }
                x--;
                return;
            } else if (direction == '>') {
                if (x == WIDTH - 3) {
                    return;
                }
                if (grid[y+2][x+3] ||
                        grid[y+1][x+3] ||
                        grid[y][x+3]) {
                    return;
                }
                x++;
                return;
            }
        }

        @Override
        public int drop() {
            if (y == 0 ||
                    grid[y-1][x] ||
                    grid[y-1][x+1] ||
                    grid[y-1][x+2]) {
                grid[y][x] = true;
                grid[y][x+1] = true;
                grid[y][x+2] = true;
                grid[y+1][x+2] = true;
                grid[y+2][x+2] = true;
                return y+2;
            } else {
                y--;
                return -1;
            }
        }
    }

    /**
     * Rock with shape
     * #
     * #
     * #
     * #
     */
    private static class VertRock implements Rock {
        private boolean[][] grid;
        private int y;
        private int x;

        VertRock(boolean[][] grid, int height) {
            this.grid = grid;
            this.y = height;
            this.x = 2;
        }

        @Override
        public void move(Character direction) {
            if (direction == '<') {
                if (x == 0) {
                    return;
                }
                if (grid[y][x-1] ||
                        grid[y+1][x-1] ||
                        grid[y+2][x-1] ||
                        grid[y+3][x-1]) {
                    return;
                }
                x--;
                return;
            } else if (direction == '>') {
                if (x == WIDTH - 1) {
                    return;
                }
                if (grid[y][x+1] ||
                        grid[y+1][x+1] ||
                        grid[y+2][x+1] ||
                        grid[y+3][x+1]) {
                    return;
                }
                x++;
                return;
            }
        }

        @Override
        public int drop() {
            if (y == 0 ||
                grid[y-1][x]) {
                grid[y][x] = true;
                grid[y+1][x] = true;
                grid[y+2][x] = true;
                grid[y+3][x] = true;
                return y+3;
            } else {
                y--;
                return -1;
            }
        }
    }

    /**
     * Rock with shape
     * ##
     * ##
     */
    private static class SquareRock implements Rock {
        private boolean[][] grid;
        private int y;
        private int x;

        SquareRock(boolean[][] grid, int height) {
            this.grid = grid;
            this.y = height;
            this.x = 2;
        }

        @Override
        public void move(Character direction) {
            if (direction == '<') {
                if (x == 0) {
                    return;
                }
                if (grid[y+1][x-1] ||
                        grid[y][x-1]) {
                    return;
                }
                x--;
                return;
            } else if (direction == '>') {
                if (x == WIDTH - 2) {
                    return;
                }
                if (grid[y+1][x+2] ||
                        grid[y][x+2]) {
                    return;
                }
                x++;
                return;
            }
        }

        @Override
        public int drop() {
            if (y == 0 ||
                    grid[y-1][x] ||
                    grid[y-1][x+1]) {
                grid[y][x] = true;
                grid[y][x+1] = true;
                grid[y+1][x] = true;
                grid[y+1][x+1] = true;
                return y+1;
            } else {
                y--;
                return -1;
            }
        }
    }
}