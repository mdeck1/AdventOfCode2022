package com.malcolmdeck.adventofcode2022.levels.level20;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Level20 {

    private static final boolean DEBUG = true;

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level20\\level20data.txt");
        try {
            // Read in list, create list of "have we touched this index yet?"
            Scanner scanner = new Scanner(file);
            List<Integer> intList = new LinkedList<>();
            List<Boolean> boolList = new LinkedList<>();
            while (scanner.hasNextLine()) {
                intList.add(Integer.parseInt(scanner.nextLine()));
                boolList.add(false);
            }

            if (DEBUG) {
                printList(intList);
                printList(boolList);
                System.out.println();
            }

            while (boolList.contains(false)) {
                // Find the right index; the bool list works because untouched items
                // stay in order relative to one another
                int i = 0;
                while (boolList.get(i)) {
                    ++i;
                }
                // Get the value at that index
                int value = intList.get(i);

                // Compute where it goes; this is done using (list.size() - 1) since you can essentially
                // treat the list as excluding the current item for the purpose of moving it around.
                int newIndex = (int) ((value + (long) i) % ((long)intList.size() - 1));
                // Rectify negative values
                while (newIndex < 0) {
                    newIndex += intList.size() - 1;
                }

                // Remove from both lists
                intList.remove(i);
                boolList.remove(i);

                // Add into both lists
                intList.add(newIndex, value);
                boolList.add(newIndex, true);

                if (DEBUG) {
                    printList(intList);
                    printList(boolList);
                    System.out.println();
                }
            }
            int startIndex = intList.indexOf(0);

            System.out.println("Grove Coordinates: " +
                    (intList.get((startIndex + 1000) % intList.size()) +
                            intList.get((startIndex + 2000) % intList.size()) +
                            intList.get((startIndex + 3000) % intList.size())));
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level20\\level20data.txt");
        try {
            // Read in the list, and keep track of their indices
            Scanner scanner = new Scanner(file);
            List<Long> longList = new LinkedList<>();
            List<Integer> indexList = new LinkedList<>();
            int j = 0;
            while (scanner.hasNextLine()) {
                longList.add(Long.parseLong(scanner.nextLine()) * 811589153l);
                indexList.add(j);
                ++j;
            }

            if (DEBUG) {
                printList(longList);
                printList(indexList);
                System.out.println();
            }

            // Run mixing on the list 10 times
            for (int encryptionRunNumber = 0; encryptionRunNumber < 10; ++encryptionRunNumber) {
                for(int i = 0; i < longList.size(); ++i) {
                    // Find the right index, which matches 'i'
                    int index = indexList.indexOf(i);
                    // Get the value at that index
                    long value = longList.get(index);

                    // Compute where it goes; this is done using (list.size() - 1) since you can essentially
                    // treat the list as excluding the current item for the purpose of moving it around.
                    long newIndex = (index + value) % (longList.size() - 1);
                    while (newIndex < 0) {
                        newIndex += longList.size() - 1;
                    }

                    // Remove from both lists
                    longList.remove(index);
                    indexList.remove(index);

                    // Add into both lists
                    longList.add((int) newIndex, value);
                    indexList.add((int) newIndex, i);
                }

                if (DEBUG) {
                    printList(longList);
                    printList(indexList);
                    System.out.println();
                }
            }
            int startIndex = longList.indexOf(0l);

            System.out.println("Grove Coordinates: " +
                    (longList.get((startIndex + 1000) % longList.size()) +
                            longList.get((startIndex + 2000) % longList.size()) +
                            longList.get((startIndex + 3000) % longList.size())));
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }


    private static void printList(List<?> intList) {
        for(int i = 0; i < intList.size() - 1; ++i) {
            System.out.print(intList.get(i) + ", ");
        }
        System.out.println(intList.get(intList.size() - 1));
    }
}
