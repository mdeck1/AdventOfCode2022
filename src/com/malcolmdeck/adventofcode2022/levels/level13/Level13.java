package com.malcolmdeck.adventofcode2022.levels.level13;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Level13 {

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level13\\level13data.txt");
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
        File file = FileHelper.getFile("level13\\level13data.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {

            }
            System.out.println(": ");
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }


}