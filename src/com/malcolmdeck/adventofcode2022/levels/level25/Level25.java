package com.malcolmdeck.adventofcode2022.levels.level25;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Level25 {

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level25\\level25data.txt");
        try {
            Scanner scanner = new Scanner(file);
            long sum = 0l;
            while (scanner.hasNextLine()) {
                String snafuNumber = scanner.nextLine();
                long snafuDecimalRepresentation = 0l;
                for (int i = 0; i < snafuNumber.length(); ++i) {
                    long placeValue = (long) Math.pow(5, snafuNumber.length() - 1 - i);
                    long placeDigit = charToInt(snafuNumber.charAt(i));
                    snafuDecimalRepresentation += placeValue * placeDigit;
                }
                sum += snafuDecimalRepresentation;
            }
            System.out.println("SnafuRep of sum: " + longToSnafuNumber(sum));
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    private static String longToSnafuNumber(long value) {
        // Get the base 5 representation
        String baseFiveRepresentation = Long.toString(value, 5);
        // Proceed right to left; fix each character along the way
        for (int i = baseFiveRepresentation.length() - 1; i > 0; --i) {
            int placeValue = charToInt(baseFiveRepresentation.charAt(i));
            if (placeValue > 2) {
                char newPlaceValue = intToChar(placeValue-5);
                StringBuilder builder = new StringBuilder(baseFiveRepresentation);
                builder.setCharAt(i, newPlaceValue);
                builder.setCharAt(i - 1,
                        intToChar(charToInt(baseFiveRepresentation.charAt(i-1)) + 1));
                baseFiveRepresentation = builder.toString();
            }
        }
        int placeValue = charToInt(baseFiveRepresentation.charAt(0));
        if (placeValue > 2) {
            char newPlaceValue = intToChar(placeValue-5);
            StringBuilder builder = new StringBuilder(baseFiveRepresentation);
            builder.setCharAt(0, newPlaceValue);
            baseFiveRepresentation = "1" + builder.toString();

        }
        return baseFiveRepresentation;
    }

    private static int charToInt(char c) {
        if (c == '=') {
            return -2;
        }
        if (c == '-') {
            return -1;
        }
        return Integer.parseInt(new String() + c);
    }

    private static char intToChar(int i) {
        if (i == -2) {
            return '=';
        }
        if (i == -1) {
            return '-';
        }
        return Integer.toString(i).charAt(0);
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level25\\level25data.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {

            }
            System.out.println(": " );
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }
}
