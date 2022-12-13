package com.malcolmdeck.adventofcode2022.levels.level13;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Level13 {

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level13\\level13data.txt");
        try {
            Scanner scanner = new Scanner(file);
            int index = 1;
            int sumOfIndices = 0;
            while (scanner.hasNextLine()) {
                // Grab each pair of lines, compare them, add to total if they're in order
                String left = scanner.nextLine();
                String right = scanner.nextLine();
                if (isInOrder(parseStringIntoList(left), parseStringIntoList(right))) {
                    sumOfIndices += index;
                }
                index++;
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }
            }
            System.out.println("Sum of indices: " + sumOfIndices);
        } catch (Exception e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level13\\level13data.txt");
        try {
            Scanner scanner = new Scanner(file);
            List<List<Object>> listOfPackets = new ArrayList<>();
            // Grab each line, check if empty, put into list
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.equals("")) {
                    listOfPackets.add(parseStringIntoList(line));
                }
            }
            // Insert signal packets
            List<Object> firstSpacer = parseStringIntoList("[[2]]");
            listOfPackets.add(firstSpacer);
            List<Object> secondSpacer = parseStringIntoList("[[6]]");
            listOfPackets.add(secondSpacer);

            // Sort the list using the comparison operation we generated in #partOne()
            listOfPackets.sort(new Comparator<List<Object>>() {
                @Override
                public int compare(List<Object> o1, List<Object> o2) {
                    Boolean isInOrder = isInOrder(o1, o2);
                    if (isInOrder == null) {
                        return 0;
                    } else if (isInOrder) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
            // Find the indices of the spacer packets, use them to compute the answer
            int firstIndex = listOfPackets.indexOf(firstSpacer) + 1;
            int secondIndex = listOfPackets.indexOf(secondSpacer) + 1;
            System.out.println("Decoder Key: " + (firstIndex * secondIndex));
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    /**
     * Returns if `left` is before `right` according to the puzzle semantics (see readme).
     */
    private static Boolean isInOrder(List<Object> left, List<Object> right) {
        for (int i = 0; i < Math.min(left.size(), right.size()); ++i) {
            Object leftElem = left.get(i);
            Object rightElem = right.get(i);
            if (leftElem instanceof Integer &&
                rightElem instanceof Integer) {
                // Both integers
                if ((int) leftElem > (int) rightElem) {
                    return false;
                } else if ((int) leftElem < (int) rightElem) {
                    return true;
                }
            } else if (leftElem instanceof List<?> &&
                rightElem instanceof List<?>) {
                // Both lists
                Boolean listsInOrder = isInOrder((List<Object>) leftElem, (List<Object>) rightElem);
                if (listsInOrder != null) {
                    return listsInOrder;
                }
            } else if (leftElem instanceof Integer &&
                    rightElem instanceof List<?>) {
                // Left Integer, Right list
                List<Object> wrappedInteger = new ArrayList<>();
                wrappedInteger.add(leftElem);
                Boolean listsInOrder = isInOrder(wrappedInteger, (List<Object>) rightElem);
                if (listsInOrder != null) {
                    return listsInOrder;
                }
            } else if (leftElem instanceof List<?> &&
                    rightElem instanceof Integer) {
                // Left List, Right Integer
                List<Object> wrappedInteger = new ArrayList<>();
                wrappedInteger.add(rightElem);
                Boolean listsInOrder = isInOrder((List<Object>) leftElem, wrappedInteger);
                if (listsInOrder != null) {
                    return listsInOrder;
                }
            } else if (rightElem == null && leftElem != null) {
                return false;
            } else if (leftElem == null && rightElem != null) {
                return true;
            }
        }
        // Check if one array ended before the other.
        if (left.size() < right.size()) {
            return true;
        } else if (left.size() > right.size()) {
            return false;
        }
        // These two are the same, ignore for the purpose of checking higher order lists.
        return null;
    }

    /**
     * Returns a List<Object> from the given String, parsing all inner lists as
     * necessary. Will return an empty list if fed "[]".
     */
    private static List<Object> parseStringIntoList(String string) {
        if (string.charAt(0) != '[') {
            throw new RuntimeException("Failed to parse String into List: " + string);
        }
        List<Object> list = new ArrayList<>();
        int pointer = 1; //Skip the first bracket
        while (pointer < string.length() - 1) {
            if (string.charAt(pointer) == '[') {
                // Find the length of this inner list, parse that, and insert
                int startOfInnerList = pointer;
                pointer++;
                int bracketCount = 1;
                while (bracketCount > 0) {
                    if (string.charAt(pointer) == '[') {
                        bracketCount++;
                    } else if (string.charAt(pointer) == ']') {
                        bracketCount--;
                    }
                    pointer++;
                }
                List<Object> innerList = parseStringIntoList(string.substring(startOfInnerList, pointer));
                list.add(innerList);
            } else if (Character.isDigit(string.charAt(pointer))) {
                // Parse until the end of this integer, then insert
                int firstIndex = pointer;
                while (string.charAt(pointer) != ',' && string.charAt(pointer) != ']') {
                    ++pointer;
                }
                Integer value = Integer.parseInt(string.substring(firstIndex, pointer));
                list.add(value);
            } else {
                list.add(null);
            }
            // Correct for end of integer issues.
            if (string.charAt(pointer) == ',') {
                pointer++;
            }
        }
        return list;
    }

}
