package com.malcolmdeck.adventofcode2022.levels.level22;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Level22 {

    private static final boolean USE_TEST_DATA = false;

    private static final int NUM_COLUMNS = 150;
    private static final int NUM_ROWS = 200;

    private static final int TEST_NUM_COLUMNS = 16;
    private static final int TEST_NUM_ROWS = 12;

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level22\\level22data.txt");
        try {
            // Read in the grid, using null to demarcate cells to be ignored
            Scanner scanner = new Scanner(file);
            int numColumns, numRows;
            if (USE_TEST_DATA) {
                numColumns = TEST_NUM_COLUMNS;
                numRows = TEST_NUM_ROWS;
            } else {
                numColumns = NUM_COLUMNS;
                numRows = NUM_ROWS;
            }
            Boolean[][] grid = new Boolean[numRows][numColumns];
            int whichRow = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equals("")) {
                    break;
                }
                for (int i = 0; i < line.length(); ++i) {
                    if (line.charAt(i) == ' ') {
                        grid[whichRow][i] = null;
                    } else {
                        grid[whichRow][i] = line.charAt(i) == '.';
                    }
                }
                whichRow++;
            }
            // Find the starting position in the first row
            int currY = 0;
            int currX = 0;
            while (grid[currY][currX] == null || !grid[currY][currX]) {
                ++currX;
            }
            Position position = new Position(currX, currY, 0);

            String commandListLine = scanner.nextLine();
            int commandIndex = 0;
            while (commandIndex < commandListLine.length()) {
                if (Character.isAlphabetic(commandListLine.charAt(commandIndex))) {
                    // Command is a turn
                    position.turn(commandListLine.charAt(commandIndex));
                    commandIndex++;
                } else {
                    //Command is a number of steps to take
                    int nextCommandStart = commandIndex;
                    while (nextCommandStart < commandListLine.length() &&
                            Character.isDigit(commandListLine.charAt(nextCommandStart))) {
                        ++nextCommandStart;
                    }
                    int numSteps = Integer.parseInt(commandListLine.substring(commandIndex, nextCommandStart));
                    position.goForward(numSteps, grid);
                    commandIndex = nextCommandStart;
                }
            }

            System.out.println("Final Password: " + position.toFinalPassword());
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level22\\level22data.txt");
        try {
            // Read in the grid, using null to demarcate blank spaces
            Scanner scanner = new Scanner(file);
            int numColumns, numRows;
            if (USE_TEST_DATA) {
                numColumns = TEST_NUM_COLUMNS;
                numRows = TEST_NUM_ROWS;
            } else {
                numColumns = NUM_COLUMNS;
                numRows = NUM_ROWS;
            }
            Boolean[][] grid = new Boolean[numRows][numColumns];
            int whichRow = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equals("")) {
                    break;
                }
                for (int i = 0; i < line.length(); ++i) {
                    if (line.charAt(i) == ' ') {
                        grid[whichRow][i] = null;
                    } else {
                        grid[whichRow][i] = line.charAt(i) == '.';
                    }
                }
                whichRow++;
            }
            // Find the starting position
            int currY = 0;
            int currX = 0;
            while (grid[currY][currX] == null || !grid[currY][currX]) {
                ++currX;
            }
            // Use new class to do cube wrapping
            PositionOnCube position = new PositionOnCube(currX, currY, 0);

            String commandListLine = scanner.nextLine();
            int commandIndex = 0;
            while (commandIndex < commandListLine.length()) {
                if (Character.isAlphabetic(commandListLine.charAt(commandIndex))) {
                    // Command is a turn
                    position.turn(commandListLine.charAt(commandIndex));
                    commandIndex++;
                } else {
                    // Command is a number of steps to take
                    int nextCommandStart = commandIndex;
                    while (nextCommandStart < commandListLine.length() &&
                            Character.isDigit(commandListLine.charAt(nextCommandStart))) {
                        ++nextCommandStart;
                    }
                    int numSteps = Integer.parseInt(commandListLine.substring(commandIndex, nextCommandStart));
                    position.goForward(numSteps, grid);
                    commandIndex = nextCommandStart;
                }
            }

            System.out.println("Final Password: " + position.toFinalPassword());
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    private static class Position {
        int x;
        int y;

        // 0 - right >
        // 1 - down  v
        // 2 - left  <
        // 3 - up    ^
        int direction;

        Position(int x, int y, int direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }

        Integer toFinalPassword() {
            return (y + 1) * 1000 + (x + 1) * 4 + direction;
        }

        void turn(char whichDirection) {
            if (whichDirection == 'L') {
                direction--;
            } else if (whichDirection == 'R') {
                direction++;
            } else {
                throw new RuntimeException("Unknown direction: " + whichDirection);
            }
            direction = (direction + 4) % 4;
        }

        void goForward(int howMany, Boolean[][] grid) {
            for (int i = 0; i < howMany; ++i) {
                goForwardOne(grid);
            }
        }

        private void goForwardOne(Boolean[][] grid) {
            if (direction == 0) {
                int nextX = x+1;
                nextX %= grid[y].length;
                while (grid[y][nextX] == null) {
                    nextX++;
                    nextX %= grid[y].length;
                }
                if (grid[y][nextX]) {
                    x = nextX;
                }
            } else if (direction == 1) {
                int nextY = y+1;
                nextY %= grid.length;
                while (grid[nextY][x] == null) {
                    nextY++;
                    nextY %= grid.length;
                }
                if (grid[nextY][x]) {
                    y = nextY;
                }
            } else if (direction == 2) {
                int nextX = x-1;
                nextX += grid[y].length;
                nextX %= grid[y].length;
                while (grid[y][nextX] == null) {
                    nextX--;
                    nextX += grid[y].length;
                    nextX %= grid[y].length;
                }
                if (grid[y][nextX]) {
                    x = nextX;
                }
            } else if (direction == 3) {
                int nextY = y-1;
                nextY += grid.length;
                nextY %= grid.length;
                while (grid[nextY][x] == null) {
                    nextY--;
                    nextY += grid.length;
                    nextY %= grid.length;
                }
                if (grid[nextY][x]) {
                    y = nextY;
                }
            }
        }
    }

    private static class PositionOnCube {
        int x;
        int y;

        // 0 - right >
        // 1 - down  v
        // 2 - left  <
        // 3 - up    ^
        int direction;

        PositionOnCube(int x, int y, int direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }

        Integer toFinalPassword() {
            return (y + 1) * 1000 + (x + 1) * 4 + direction;
        }

        void turn(char whichDirection) {
            if (whichDirection == 'L') {
                direction--;
            } else if (whichDirection == 'R') {
                direction++;
            } else {
                throw new RuntimeException("Unknown direction: " + whichDirection);
            }
            direction = (direction + 4) % 4;
        }

        void goForward(int howMany, Boolean[][] grid) {
            for (int i = 0; i < howMany; ++i) {
                goForwardOne(grid);
            }
        }

        private void goForwardOne(Boolean[][] grid) {
            if (direction == 0) {
                int nextX = x+1;
                if (nextX >= grid[y].length || grid[y][nextX] == null) {
                    walkOffFace(nextX, y, grid);
                } else if (grid[y][nextX]) {
                    x = nextX;
                }
            } else if (direction == 1) {
                int nextY = y+1;
                if (nextY >= grid.length || grid[nextY][x] == null) {
                    walkOffFace(x, nextY, grid);
                } else if (grid[nextY][x]) {
                    y = nextY;
                }
            } else if (direction == 2) {
                int nextX = x-1;
                if (nextX < 0 || grid[y][nextX] == null) {
                    walkOffFace(nextX, y, grid);
                } else if (grid[y][nextX]) {
                    x = nextX;
                }
            } else if (direction == 3) {
                int nextY = y-1;
                if (nextY < 0 || grid[nextY][x] == null) {
                    walkOffFace(x, nextY, grid);
                } else if (grid[nextY][x]) {
                    y = nextY;
                }
            }
        }

        private void walkOffFace(int nextX, int nextY, Boolean[][] grid) {
            switch(whichFace(x, y)) {
                case 1:
                    if (nextY < 0) {
                        // Moving towards face 6
                        nextY = x + 100;
                        nextX = 0;
                        if (grid[nextY][nextX]) {
                            x = nextX;
                            y = nextY;
                            direction = 0;
                        }
                    } else if (nextX < 50) {
                        // Moving into face 5
                        nextY = 149 - y;
                        nextX = 0;
                        if (grid[nextY][nextX]) {
                            x = nextX;
                            y = nextY;
                            direction = 0;
                        }
                    }
                    return;
                case 2:
                    if (nextY < 0) {
                        // Moving into face 6
                        nextX = x - 100;
                        nextY = 199;
                        if (grid[nextY][nextX]) {
                            x = nextX;
                            y = nextY;
                            direction = 3;
                        }
                    } else if (nextY > 49) {
                        // Moving into face 3
                        nextX = 99;
                        nextY = x - 50;
                        if (grid[nextY][nextX]) {
                            x = nextX;
                            y = nextY;
                            direction = 2;
                        }
                    } else if (nextX > 149) {
                        //Moving into face 4
                        nextX = 99;
                        nextY = 149 - y;
                        if (grid[nextY][nextX]) {
                            x = nextX;
                            y = nextY;
                            direction = 2;
                        }
                    }
                    return;
                case 3:
                    if (nextX < 50) {
                        // Moving into face 5
                        nextX = y - 50;
                        nextY = 100;
                        if (grid[nextY][nextX]) {
                            x = nextX;
                            y = nextY;
                            direction = 1;
                        }
                    } else if (nextX > 99) {
                        // Moving into face 2
                        nextX = y + 50;
                        nextY = 49;
                        if (grid[nextY][nextX]) {
                            x = nextX;
                            y = nextY;
                            direction = 3;
                        }
                    }
                    break;
                case 4:
                    if (nextX > 99) {
                        // Moving into face 2
                        nextX = 149;
                        nextY = 149 - y;
                        if (grid[nextY][nextX]) {
                            x = nextX;
                            y = nextY;
                            direction = 2;
                        }
                    } else if (nextY > 149) {
                        // Moving into face 6
                        nextX = 49;
                        nextY = x + 100;
                        if (grid[nextY][nextX]) {
                            x = nextX;
                            y = nextY;
                            direction = 2;
                        }
                    }
                    return;
                case 5:
                    if (nextY < 100) {
                        // Moving into face 3
                        nextX = 50;
                        nextY = x + 50;
                        if (grid[nextY][nextX]) {
                            x = nextX;
                            y = nextY;
                            direction = 0;
                        }
                    } else if (nextX < 0) {
                        // Moving into face 1
                        nextX = 50;
                        nextY = 149 - y;
                        if (grid[nextY][nextX]) {
                            x = nextX;
                            y = nextY;
                            direction = 0;
                        }
                    }
                    return;
                case 6:
                    if (nextX < 0) {
                        // Moving into face 1
                        nextX = y - 100;
                        nextY = 0;
                        if (grid[nextY][nextX]) {
                            x = nextX;
                            y = nextY;
                            direction = 1;
                        }
                    } else if (nextX > 49) {
                        // Moving into face 4
                        nextX = y - 100;
                        nextY = 149;
                        if (grid[nextY][nextX]) {
                            x = nextX;
                            y = nextY;
                            direction = 3;
                        }
                    } else if (nextY > 199) {
                        // Moving into face 2
                        nextX = x + 100;
                        nextY = 0;
                        if (grid[nextY][nextX]) {
                            x = nextX;
                            y = nextY;
                            direction = 1;
                        }
                    }
                    break;
            }
        }

        private int whichFace(int x, int y) {
            if (x < 50) {
                if (y < 149) {
                    return 5;
                } else {
                    return 6;
                }
            } else if (x < 100) {
                if (y < 50) {
                    return 1;
                } else if (y < 100) {
                    return 3;
                } else {
                    return 4;
                }
            } else {
                return 2;
            }
        }
    }
}
