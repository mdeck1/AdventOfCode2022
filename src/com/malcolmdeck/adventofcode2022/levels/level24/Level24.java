package com.malcolmdeck.adventofcode2022.levels.level24;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Level24 {

    private static final boolean USE_TEST_DATA = false;
    private static final boolean DEBUG = true;

    private static final int TEST_ROW_WIDTH = 6;
    private static final int TEST_COLUMN_HEIGHT = 4;
    private static final int ROW_WIDTH = 100;
    private static final int COLUMN_HEIGHT = 35;

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level24\\level24data.txt");
        try {
            Scanner scanner = new Scanner(file);
            BlizzardGrid blizzardGrid;
            if (USE_TEST_DATA) {
                blizzardGrid = new BlizzardGrid(TEST_ROW_WIDTH, TEST_COLUMN_HEIGHT);
            } else {
                blizzardGrid = new BlizzardGrid(ROW_WIDTH, COLUMN_HEIGHT);
            }
            int whichRow = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                for (int i = 0; i < blizzardGrid.rowWidth + 1; ++i) {
                    if (line.charAt(i) != '.') {
                        blizzardGrid.addBlizzard(
                                line.charAt(i), i - 1, whichRow - 1);
                    }
                }
                whichRow++;
            }

            System.out.println("Shortest Route time: " +
                    (shortestRoute(
                            blizzardGrid,
                            0,
                            -1,
                            blizzardGrid.rowWidth - 1,
                            blizzardGrid.columnHeight - 1,
                            0) +
                            1));
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }


    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level24\\level24data.txt");
        try {
            Scanner scanner = new Scanner(file);
            BlizzardGrid blizzardGrid;
            if (USE_TEST_DATA) {
                blizzardGrid = new BlizzardGrid(TEST_ROW_WIDTH, TEST_COLUMN_HEIGHT);
            } else {
                blizzardGrid = new BlizzardGrid(ROW_WIDTH, COLUMN_HEIGHT);
            }
            int whichRow = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                for (int i = 0; i < blizzardGrid.rowWidth + 1; ++i) {
                    if (line.charAt(i) != '.') {
                        blizzardGrid.addBlizzard(
                                line.charAt(i), i - 1, whichRow - 1);
                    }
                }
                whichRow++;
            }

            int firstLeg = shortestRoute(
                    blizzardGrid,
                    0,
                    -1,
                    blizzardGrid.rowWidth - 1,
                    blizzardGrid.columnHeight - 1,
                    0) + 1;
            int secondLeg =
                    shortestRoute(
                            blizzardGrid,
                            blizzardGrid.rowWidth - 1,
                            blizzardGrid.columnHeight,
                            0,
                            0,
                            firstLeg) + 1;
            System.out.println("Shortest total Route time: " +
                    (shortestRoute(
                            blizzardGrid,
                            0,
                            -1,
                            blizzardGrid.rowWidth - 1,
                            blizzardGrid.columnHeight - 1,
                            secondLeg) + 1));
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    private static int shortestRoute(
            BlizzardGrid blizzardGrid,
            int startX,
            int startY,
            int endX,
            int endY,
            int startTime) {
        PriorityQueue<CurrentPathPoint> queue = new PriorityQueue<>(new Comparator<CurrentPathPoint>() {
            @Override
            public int compare(CurrentPathPoint o1, CurrentPathPoint o2) {
                return o1.timePassedSoFar - o2.timePassedSoFar;
            }
        });
        queue.add(new CurrentPathPoint(startX, startY, startTime));

        while (true) {
            CurrentPathPoint currentPathPoint = queue.poll();
            if (currentPathPoint.x == endX &&
                currentPathPoint.y == endY) {
                if (DEBUG) {
                    System.out.println("Printing out path: ");
                    for (CurrentPathPoint point : currentPathPoint.previousPoints) {
                        System.out.println(point);
                    }
                    System.out.println(currentPathPoint);
                    System.out.println();
                }
                return currentPathPoint.timePassedSoFar;
            }
            if (currentPathPoint.x > 0 &&
                    currentPathPoint.y > -1 &&
                    currentPathPoint.y < blizzardGrid.columnHeight &&
                    blizzardGrid.pointAvailableAtTime(
                        currentPathPoint.x - 1,
                        currentPathPoint.y,
                        currentPathPoint.timePassedSoFar + 1)) {
                CurrentPathPoint next = currentPathPoint.nextPointFromStep(
                        currentPathPoint.x - 1,
                        currentPathPoint.y);
                addToQueueIfNotAlreadyThere(queue, next);
            }
            if (currentPathPoint.x < blizzardGrid.rowWidth - 1 &&
                    currentPathPoint.y > -1 &&
                    currentPathPoint.y < blizzardGrid.columnHeight &&
                    blizzardGrid.pointAvailableAtTime(
                        currentPathPoint.x + 1,
                        currentPathPoint.y,
                        currentPathPoint.timePassedSoFar + 1)) {
                CurrentPathPoint next = currentPathPoint.nextPointFromStep(
                        currentPathPoint.x + 1,
                        currentPathPoint.y);
                addToQueueIfNotAlreadyThere(queue, next);
            }
            if (currentPathPoint.y > 0 &&
                    blizzardGrid.pointAvailableAtTime(
                            currentPathPoint.x,
                            currentPathPoint.y - 1,
                            currentPathPoint.timePassedSoFar + 1)) {
                CurrentPathPoint next = currentPathPoint.nextPointFromStep(
                        currentPathPoint.x,
                        currentPathPoint.y - 1);
                addToQueueIfNotAlreadyThere(queue, next);
            }
            if (currentPathPoint.y < blizzardGrid.columnHeight - 1 &&
                    blizzardGrid.pointAvailableAtTime(
                            currentPathPoint.x,
                            currentPathPoint.y + 1,
                            currentPathPoint.timePassedSoFar + 1)) {
                CurrentPathPoint next = currentPathPoint.nextPointFromStep(
                        currentPathPoint.x,
                        currentPathPoint.y + 1);
                addToQueueIfNotAlreadyThere(queue, next);
            }
            if (blizzardGrid.pointAvailableAtTime(
                    currentPathPoint.x,
                    currentPathPoint.y,
                    currentPathPoint.timePassedSoFar + 1)) {
                CurrentPathPoint next = currentPathPoint.nextPointFromStep(
                        currentPathPoint.x,
                        currentPathPoint.y);
                addToQueueIfNotAlreadyThere(queue, next);
            }
        }
    }

    private static void addToQueueIfNotAlreadyThere(
            PriorityQueue<CurrentPathPoint> queue,
            CurrentPathPoint candidate) {
        if (!queue.contains(candidate)) {
            queue.add(candidate);
        }
    }


    static class CurrentPathPoint {
        List<CurrentPathPoint> previousPoints;
        int x;
        int y;
        int timePassedSoFar;

        CurrentPathPoint(int x, int y, int timePassedSoFar) {
            this.x = x;
            this.y = y;
            this.timePassedSoFar = timePassedSoFar;
            previousPoints = new ArrayList<>();
        }

        CurrentPathPoint nextPointFromStep(int x, int y) {
            CurrentPathPoint nextPoint = new CurrentPathPoint(x, y, timePassedSoFar + 1);
            nextPoint.previousPoints = new ArrayList<>(previousPoints);
            nextPoint.previousPoints.add(this);
            return nextPoint;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof CurrentPathPoint)) {
                return false;
            }
            CurrentPathPoint o = (CurrentPathPoint) obj;
            return this.x == o.x && this.y == o.y && this.timePassedSoFar == o.timePassedSoFar;
        }

        @Override
        public String toString() {
            return "Time " + this.timePassedSoFar + ": (" + this.x + ", " + this.y + ")";
        }
    }

    static class BlizzardGrid {
        int rowWidth;
        int columnHeight;
        List<BlizzardRowOrColumn> blizzardRows;
        List<BlizzardRowOrColumn> blizzardColumns;

        BlizzardGrid(int rowWidth, int columnHeight) {
            this.rowWidth = rowWidth;
            this.columnHeight = columnHeight;
            blizzardRows = new ArrayList<>(columnHeight);
            blizzardColumns = new ArrayList<>(rowWidth);
            for (int i = 0; i < columnHeight; ++i) {
                blizzardRows.add(new BlizzardRowOrColumn(rowWidth));
            }
            for (int i = 0; i < rowWidth; ++i) {
                blizzardColumns.add(new BlizzardRowOrColumn(columnHeight));
            }
        }

        boolean pointAvailableAtTime(int x, int y, int t) {
            if ((x == 0 && y == -1) || (x == rowWidth - 1 && y == columnHeight)) {
                return true;
            }
            return !blizzardColumns.get(x).invalidPositionsAtTime(t).contains(y) &&
                    !blizzardRows.get(y).invalidPositionsAtTime(t).contains(x);
        }

        void addBlizzard(Character c, int x, int y) {
            switch (c) {
                case '^':
                    blizzardColumns.get(x).addBackwardsFacingBlizzard(y);
                    break;
                case 'v':
                    blizzardColumns.get(x).addForwardsFacingBlizzard(y);
                    break;
                case '<':
                    blizzardRows.get(y).addBackwardsFacingBlizzard(x);
                    break;
                case '>':
                    blizzardRows.get(y).addForwardsFacingBlizzard(x);
                    break;
            }
        }
    }

    static class BlizzardRowOrColumn {
        int rowWidthOrColumnHeight;
        List<Integer> initialBackwardsTraversingBlizzardPositions;
        List<Integer> initialForwardTraversingBlizzardPositions;

        BlizzardRowOrColumn(int rowWidthOrColumnHeight) {
            this.rowWidthOrColumnHeight = rowWidthOrColumnHeight;
            initialBackwardsTraversingBlizzardPositions = new ArrayList<>();
            initialForwardTraversingBlizzardPositions = new ArrayList<>();
        }

        void addBackwardsFacingBlizzard(int index) {
            initialBackwardsTraversingBlizzardPositions.add(index);
        }

        void addForwardsFacingBlizzard(int index) {
            initialForwardTraversingBlizzardPositions.add(index);
        }

        Set<Integer> invalidPositionsAtTime(int minutesPassed) {
            Set<Integer> newBlizzardPositions = new HashSet<>(
                    initialBackwardsTraversingBlizzardPositions.size() +
                            initialForwardTraversingBlizzardPositions.size());
            for (Integer leftStartingIndex : initialBackwardsTraversingBlizzardPositions) {
                newBlizzardPositions.add(
                        ((leftStartingIndex - minutesPassed) % rowWidthOrColumnHeight + rowWidthOrColumnHeight) % rowWidthOrColumnHeight);
            }
            for (Integer rightStartingIndex : initialForwardTraversingBlizzardPositions) {
                newBlizzardPositions.add(
                        (rightStartingIndex + minutesPassed) % rowWidthOrColumnHeight);
            }
            return newBlizzardPositions;
        }
    }
}
