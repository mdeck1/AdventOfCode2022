package com.malcolmdeck.adventofcode2022.levels.level9;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Level9 {

    private static final boolean DEBUG = true;

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level9\\level9data.txt");
        try {
            Scanner scanner = new Scanner(file);
            Point head = new Point(0, 0);
            Point tail = new Point(0, 0);
            Set<Point> tailLocations = new HashSet<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] command = line.split(" ");
                // Simulate each step of the command given separately
                for (int i = 0; i < Integer.parseInt(command[1]); ++i) {
                    // Move the head
                    if (command[0].equals("U")) {
                        head.move(0, 1);
                    } else if (command[0].equals("D")) {
                        head.move(0, -1);
                    } else if (command[0].equals("L")) {
                        head.move(-1, 0);
                    } else { // Right
                        head.move(1, 0);
                    }
                    // Move and track the tail
                    tail.moveTowardsIfNecessary(head);
                    tailLocations.add(tail.copy());
                }
            }
            System.out.println("Number of tail locations: "  + tailLocations.size());
        } catch (Exception e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level9\\level9data.txt");
        try {
            Scanner scanner = new Scanner(file);
            Point[] ropePoints = new Point[10];
            for (int i = 0; i < 10; ++i) {
                ropePoints[i] = new Point(0, 0);
            }
            Set<Point> tailLocations = new HashSet<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] command = line.split(" ");
                // Simulate each step of the command separately
                for (int i = 0; i < Integer.parseInt(command[1]); ++i) {
                    // Move the head
                    if (command[0].equals("U")) {
                        ropePoints[0].move(0, 1);
                    } else if (command[0].equals("D")) {
                        ropePoints[0].move(0, -1);
                    } else if (command[0].equals("L")) {
                        ropePoints[0].move(-1, 0);
                    } else { // Right
                        ropePoints[0].move(1, 0);
                    }
                    // Move each middle and tail piece, reconciling one-by-one, and track the tail
                    for (int j = 1; j < 10; ++j) {
                        ropePoints[j].moveTowardsIfNecessary(ropePoints[j-1]);
                    }
                    tailLocations.add(ropePoints[9].copy());
                }
            }
            System.out.println("Number of tail locations: "  + tailLocations.size());
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    /* Helper class to represent a point */
    private static class Point {
        private int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        void move(int xDiff, int yDiff) {
            x += xDiff;
            y += yDiff;
        }

        /**
         * The meat of the problem. If neither x nor y are at least 2 apart, you
         * don't move the point. Otherwise, increment the direction that triggered the flag,
         * and then also move the opposite coordinate if they are different by one.
         * *Don't* try to optimize by setting the opposite coordinate to the head's value of
         * that coordinate - that optimization only works for the two point case.
         */
        void moveTowardsIfNecessary(Point head) {
            if (Math.abs(this.x - head.x) > 1) {
                if (this.x > head.x) {
                    this.x--;
                } else {
                    this.x++;
                }
                if (this.y < head.y) {
                    this.y++;
                } else if (this.y > head.y) {
                    this.y--;
                }
            } else if (Math.abs(this.y - head.y) > 1) {
                if (this.y > head.y) {
                    this.y--;
                } else {
                    this.y++;
                }
                if (this.x < head.x) {
                    this.x++;
                } else if (this.x > head.x) {
                    this.x--;
                }
            }
        }

        Point copy() {
            return new Point(this.x, this.y);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Point)) {
                return false;
            }
            Point p = (Point) o;
            return this.x == p.x && this.y == p.y;
        }

        /* Used to make this class play nice with the HashSet */
        @Override
        public int hashCode() {
            // 1000000 is greater than the highest y value expected in this problem
            // Not suitable for general purpose use.
            return this.x * 1000000 + this.y;
        }

    }
}
