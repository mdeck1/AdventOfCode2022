package com.malcolmdeck.adventofcode2022.levels.level4;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.util.Scanner;

public class Level4 {

    private static final boolean DEBUG = false;

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level4\\level4data.txt");
        try {
            Scanner scanner = new Scanner(file);
            int countPairs = 0;
            while (scanner.hasNextLine()) {
                String sectionPair = scanner.nextLine();
                String[] elfAssignments = sectionPair.split(",");
                int firstLeft, firstRight, secondLeft, secondRight;
                firstLeft = Integer.parseInt(elfAssignments[0].split("-")[0]);
                firstRight = Integer.parseInt(elfAssignments[0].split("-")[1]);
                secondLeft = Integer.parseInt(elfAssignments[1].split("-")[0]);
                secondRight = Integer.parseInt(elfAssignments[1].split("-")[1]);
                if ((secondRight - secondLeft) > (firstRight - firstLeft)) {
                    if (isFullyContained(secondLeft, secondRight, firstLeft, firstRight)) {
                        countPairs++;
                    }
                } else {
                    if (isFullyContained(firstLeft, firstRight, secondLeft, secondRight)) {
                        countPairs++;
                    }
                }
            }
            System.out.println("CountPairs: " + countPairs);
        } catch (Exception e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    private static boolean isFullyContained(int bigLeft, int bigRight, int smallLeft, int smallRight) {
        return !((bigLeft > smallLeft) || (bigRight < smallRight));
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level4\\level4data.txt");
        try {
            Scanner scanner = new Scanner(file);
            int countPairs = 0;
            while (scanner.hasNextLine()) {
                String sectionPair = scanner.nextLine();
                String[] elfAssignments = sectionPair.split(",");
                int firstLeft, firstRight, secondLeft, secondRight;
                firstLeft = Integer.parseInt(elfAssignments[0].split("-")[0]);
                firstRight = Integer.parseInt(elfAssignments[0].split("-")[1]);
                secondLeft = Integer.parseInt(elfAssignments[1].split("-")[0]);
                secondRight = Integer.parseInt(elfAssignments[1].split("-")[1]);
                if ((secondRight - secondLeft) > (firstRight - firstLeft)) {
                    if (isOverlapping(secondLeft, secondRight, firstLeft, firstRight)) {
                        countPairs++;
                        if (DEBUG) {
                            System.out.print("Found Overlapping: ");
                            printPair(firstLeft, firstRight, secondLeft, secondRight);
                        }
                    } else {
                        if (DEBUG) {
                            System.out.print("Not Overlapping: ");
                            printPair(firstLeft, firstRight, secondLeft, secondRight);
                        }
                    }
                } else {
                    if (isOverlapping(firstLeft, firstRight, secondLeft, secondRight)) {
                        countPairs++;
                        if (DEBUG) {
                            System.out.print("Found Overlapping: ");
                            printPair(firstLeft, firstRight, secondLeft, secondRight);
                        }
                    } else {
                        if (DEBUG) {
                            System.out.print("Not Overlapping: ");
                            printPair(firstLeft, firstRight, secondLeft, secondRight);
                        }
                    }
                }
            }
            System.out.println("CountPairs: " + countPairs);
        } catch (Exception e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    private static final void printPair(int firstLeft, int firstRight, int secondLeft, int secondRight) {
        System.out.println("(" + firstLeft + ", " + firstRight + "), (" + secondLeft + ", " + secondRight + ")");
    }

    private static boolean isOverlapping(int firstLeft, int firstRight, int secondLeft, int secondRight) {
        return (firstLeft <= secondRight && firstRight >= secondLeft) ||
                (secondLeft <= firstRight && secondRight >= firstLeft));
    }

}
