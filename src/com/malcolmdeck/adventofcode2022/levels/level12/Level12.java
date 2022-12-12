package com.malcolmdeck.adventofcode2022.levels.level12;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Level12 {

    private static final Boolean DEBUG = true;

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level12\\level12data.txt");
        try {
            // Read in the grid, keeping track of the start and end points, and
            // converting the characters into their implicits heights
            Scanner scanner = new Scanner(file);
            List<List<Integer>> grid = new ArrayList<>();
            int lineCount = 0;
            int startLine = -1;
            int startIndex = -1;
            int endLine = -1;
            int endIndex = -1;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                List<Integer> thisLine = new ArrayList<>();
                for (int i = 0; i < line.length(); ++i) {
                    Character currentChar = line.charAt(i);
                    if (currentChar == 'S') {
                        startLine = lineCount;
                        startIndex = i;
                        thisLine.add(0);
                    } else if (currentChar == 'E') {
                        endLine = lineCount;
                        endIndex = i;
                        thisLine.add(25);
                    } else {
                        thisLine.add(((int) currentChar) - 97);
                    }
                }
                lineCount++;
                grid.add(thisLine);
            }
            // Call helper function for start point, return minSteps
            int minSteps =  minSteps(grid, startLine, startIndex, endLine, endIndex);
            System.out.println("Min steps taken: " + minSteps);
        } catch (Exception e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    /**
     * Uses the naive solution of running partOne over and over again. A better solution
     * may be building the minimum paths starting from the exit and using reverse conditional logic
     * (e.g. can step *down* at most a height of once) for the whole grid. Then you can just get the solution in a
     * single pass, but our input was only ~8000 in size, so it still works as-is.
     */
    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level12\\level12data.txt");
        try {
            // Read in the grid, keeping track of the end point, and
            // converting the characters into their implicits heights
            Scanner scanner = new Scanner(file);List<List<Integer>> grid = new ArrayList<>();
            int lineCount = 0;
            int endLine = -1;
            int endIndex = -1;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                List<Integer> thisLine = new ArrayList<>();
                for (int i = 0; i < line.length(); ++i) {
                    Character currentChar = line.charAt(i);
                    if (currentChar == 'S') {
                        thisLine.add(0);
                    } else if (currentChar == 'E') {
                        endLine = lineCount;
                        endIndex = i;
                        thisLine.add(25);
                    } else {
                        thisLine.add(((int) currentChar) - 97);
                    }
                }
                lineCount++;
                grid.add(thisLine);
            }
            // Calls helper function for each possible starting point, keeping
            // track of the one that has the lowest value
            Integer minSteps = Integer.MAX_VALUE;
            for (int line = 0; line < grid.size(); ++line) {
                for (int index = 0; index < grid.get(0).size(); ++index) {
                    if (grid.get(line).get(index) == 0) {
                        int minForThisLocation =
                                minSteps(
                                        grid,
                                        line,
                                        index,
                                        endLine,
                                        endIndex);
                        minSteps = Math.min(minSteps, minForThisLocation);
                    }
                }
            }
            System.out.println("Min possible steps: " + minSteps);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    /**
     * Given a grid, a place to start, and a place to end, searches for the
     * shortest path from start to end where the value in grid in adjacent
     * steps can increment by at most one. Returns how many steps that path takes.
     * Uses BFS.
     */
    private static int minSteps(List<List<Integer>> grid,
                                int startLine,
                                int startIndex,
                                int endLine,
                                int endIndex) {
        PriorityQueue<SeekPoint> queue = new PriorityQueue<>(new Comparator<SeekPoint>() {
            @Override
            public int compare(SeekPoint o1, SeekPoint o2) {
                return o1.stepsSoFar - o2.stepsSoFar;
            }
        });
        queue.add(new SeekPoint(startLine, startIndex, 0));
        boolean[][] seenSoFar = new boolean[grid.size()][grid.get(0).size()];
        seenSoFar[startLine][startIndex] = true;
        int nodesProcessed = 0;
        while(true) {
            if (DEBUG && nodesProcessed % 1000 == 0) {
                System.out.println("Nodes Processed: " + nodesProcessed);
                System.out.println("Queue size: " + queue.size());
            }
            nodesProcessed++;
            SeekPoint currentPoint = queue.poll();
            if (currentPoint == null) {
                // The current point doesn't have a path to the top (e.g. too deep a gully, no smooth route out)
                return Integer.MAX_VALUE;
            }
            if (currentPoint.line == endLine && currentPoint.index == endIndex) {
                return currentPoint.stepsSoFar;
            }
            if (currentPoint.line > 0 &&
                    !seenSoFar[currentPoint.line - 1][currentPoint.index] &&
                    (grid.get(currentPoint.line - 1).get(currentPoint.index) <
                            (grid.get(currentPoint.line).get(currentPoint.index) + 2))) {
                queue.add(new SeekPoint(
                        currentPoint.line - 1,
                        currentPoint.index,
                        currentPoint.stepsSoFar + 1));
                seenSoFar[currentPoint.line - 1][currentPoint.index] = true;
            }
            if (currentPoint.line < grid.size() - 1 &&
                    !seenSoFar[currentPoint.line + 1][currentPoint.index] &&
                    (grid.get(currentPoint.line + 1).get(currentPoint.index) <
                            (grid.get(currentPoint.line).get(currentPoint.index) + 2))) {
                queue.add(new SeekPoint(
                        currentPoint.line + 1,
                        currentPoint.index,
                        currentPoint.stepsSoFar + 1));
                seenSoFar[currentPoint.line + 1][currentPoint.index] = true;
            }
            if (currentPoint.index > 0 &&
                    !seenSoFar[currentPoint.line][currentPoint.index - 1] &&
                    (grid.get(currentPoint.line).get(currentPoint.index - 1) <
                            (grid.get(currentPoint.line).get(currentPoint.index) + 2))) {
                queue.add(new SeekPoint(
                        currentPoint.line,
                        currentPoint.index - 1,
                        currentPoint.stepsSoFar + 1));
                seenSoFar[currentPoint.line][currentPoint.index - 1] = true;
            }
            if (currentPoint.index < grid.get(0).size() - 1 &&
                    !seenSoFar[currentPoint.line][currentPoint.index + 1] &&
                    (grid.get(currentPoint.line).get(currentPoint.index + 1) <
                            (grid.get(currentPoint.line).get(currentPoint.index) + 2))) {
                queue.add(new SeekPoint(
                        currentPoint.line,
                        currentPoint.index + 1,
                        currentPoint.stepsSoFar + 1));
                seenSoFar[currentPoint.line][currentPoint.index + 1] = true;
            }
        }
    }

    static class SeekPoint {
        int line;
        int index;
        int stepsSoFar;

        SeekPoint(int line, int index, int stepsSoFar) {
            this.line = line;
            this.index = index;
            this.stepsSoFar = stepsSoFar;
        }


    }

}
