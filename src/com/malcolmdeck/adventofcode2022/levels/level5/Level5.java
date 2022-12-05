package com.malcolmdeck.adventofcode2022.levels.level5;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.util.Scanner;

public class Level5 {

    private static final boolean DEBUG = false;

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level5\\level5data.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {

            }
            System.out.println(": ");
        } catch (Exception e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level5\\level5data.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {

            }
            System.out.println(": " );
        } catch (Exception e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }
}