package com.malcolmdeck.adventofcode2022.levels.level3;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;

public class Level3 {

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level3\\level3data.txt");
        try {
            Scanner scanner = new Scanner(file);
            int prioritySum = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                HashSet<Character> set = new HashSet<>();
                for (int i = 0; i < line.length() / 2; ++i) {
                    set.add(line.charAt(i));
                }
                for (int i = line.length() / 2; i < line.length(); ++i) {
                    if (set.contains(line.charAt(i))) {
                        char misplaced = line.charAt(i);
                        prioritySum += charToPriority(misplaced);
                        break;
                    }
                }
            }
            System.out.println("prioritySum: " + prioritySum);
        } catch (Exception e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level3\\level3data.txt");
        try {
            Scanner scanner = new Scanner(file);
            int prioritySum = 0;
            while (scanner.hasNextLine()) {
                String firstLine = scanner.nextLine();
                String secondLine = scanner.nextLine();
                String thirdLine = scanner.nextLine();
                HashSet<Character> firstLineSet = new HashSet<>();
                for (int i = 0; i < firstLine.length(); ++i) {
                    firstLineSet.add(firstLine.charAt(i));
                }
                HashSet<Character> firstTwoLinesSet = new HashSet<>();
                for (int i = 0; i < secondLine.length(); ++i) {
                    if (firstLineSet.contains(secondLine.charAt(i))) {
                        firstTwoLinesSet.add(secondLine.charAt(i));
                    }
                }
                for (int i = 0; i < thirdLine.length(); ++i) {
                    if (firstTwoLinesSet.contains(thirdLine.charAt(i))) {
                        char badgeCharacter = thirdLine.charAt(i);
                        prioritySum += charToPriority(badgeCharacter);
                        break;
                    }
                }
            }
            System.out.println("prioritySum: " + prioritySum);
        } catch (Exception e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    private static int charToPriority(char a) {
        if (((int) a) > 96) {
            // Lower case
            return ((int) a) - 96;
        } else {
            //Upper case
            return ((int) a) - 38;
        }
    }
}
