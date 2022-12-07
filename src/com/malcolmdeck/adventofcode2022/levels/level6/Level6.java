package com.malcolmdeck.adventofcode2022.levels.level6;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;

public class Level6 {

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level6\\level6data.txt");
        try {
            Scanner scanner = new Scanner(file);
            String inputStream = scanner.nextLine();
            // Looking for 4 characters, so start at the fourth character
            for (int i = 3; i < inputStream.length(); ++i) {
                Character a = inputStream.charAt(i - 3);
                Character b = inputStream.charAt(i - 2);
                Character c = inputStream.charAt(i - 1);
                Character d = inputStream.charAt(i);
                // Conditional: if all of the characters are different
                if (a != b &&
                        a != c &&
                        a != d &&
                        b != c &&
                        b != d &&
                        c != d) {
                    System.out.println("Found beginning at index: " + (i + 1));
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level6\\level6data.txt");
        try {
            Scanner scanner = new Scanner(file);
            String inputStream = scanner.nextLine();
            // Looking for 14 characters, so start at the fourth character
            for (int i = 13; i < inputStream.length(); ++i) {
                HashSet<Character> set = new HashSet<>();
                for (int j = i - 13; j <= i; ++j) {
                    set.add(inputStream.charAt(j));
                }
                // The set will only have size 14 if all of the characters are different.
                if (set.size() == 14) {
                    System.out.println("Found beginning at index: " + (i + 1));
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

}
