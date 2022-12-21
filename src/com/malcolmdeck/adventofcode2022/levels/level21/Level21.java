package com.malcolmdeck.adventofcode2022.levels.level21;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Level21 {

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level21\\level21data.txt");
        try {
            Scanner scanner = new Scanner(file);
            Map<String, Monkey> monkeyMap = new HashMap<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    monkeyMap.put(
                            parts[0].substring(0, parts[0].length() - 1),
                            new NumberMonkey(
                                    parts[0].substring(0, parts[0].length() - 1),
                                    Long.parseLong(parts[1])));
                } else if (parts.length == 4) {
                    int operation = -1;
                    switch (parts[2]) {
                        case "+":
                            operation = 0;
                            break;
                        case "-":
                            operation = 1;
                            break;
                        case "*":
                            operation = 2;
                            break;
                        case "/":
                            operation = 3;
                            break;
                    }
                    monkeyMap.put(
                            parts[0].substring(0, parts[0].length() - 1),
                            new OperationMonkey(
                                    parts[0].substring(0, parts[0].length() - 1),
                                    parts[1],
                                    parts[3],
                                    operation));
                }
            }
            System.out.println("Root value: " + monkeyMap.get("root").compute(monkeyMap));
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level21\\level21data.txt");
        try {
            Scanner scanner = new Scanner(file);
            Map<String, Monkey> monkeyMap = new HashMap<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    monkeyMap.put(
                            parts[0].substring(0, parts[0].length() - 1),
                            new NumberMonkey(
                                    parts[0].substring(0, parts[0].length() - 1),
                                    Long.parseLong(parts[1])));
                } else if (parts.length == 4) {
                    int operation = -1;
                    switch (parts[2]) {
                        case "+":
                            operation = 0;
                            break;
                        case "-":
                            operation = 1;
                            break;
                        case "*":
                            operation = 2;
                            break;
                        case "/":
                            operation = 3;
                            break;
                    }
                    monkeyMap.put(
                            parts[0].substring(0, parts[0].length() - 1),
                            new OperationMonkey(
                                    parts[0].substring(0, parts[0].length() - 1),
                                    parts[1],
                                    parts[3],
                                    operation));
                }
            }
            OperationMonkey root = (OperationMonkey) monkeyMap.get("root");
            long humnValue;
            if (monkeyMap.get(root.leftChildName).containsHumnInSubtree(monkeyMap)) {
                humnValue = getTargetVal(
                        monkeyMap.get(root.rightChildName).compute(monkeyMap),
                        monkeyMap.get(root.leftChildName),
                        monkeyMap);
            } else {
                humnValue = getTargetVal(
                        monkeyMap.get(root.leftChildName).compute(monkeyMap),
                        monkeyMap.get(root.rightChildName),
                        monkeyMap);
            }
            System.out.println("Humn value: " + humnValue);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    private static long getTargetVal(
            long targetValue,
            Monkey sideOfMonkeyTreeContainingHumn,
            Map<String, Monkey> monkeyMap) {
        // Base case: we are at humn
        if (sideOfMonkeyTreeContainingHumn.name.equals("humn")) {
            return targetValue;
        }
        OperationMonkey currentMonkey = (OperationMonkey) sideOfMonkeyTreeContainingHumn;
        // Figure out which side humn is on
        if (monkeyMap.get(currentMonkey.leftChildName)
                .containsHumnInSubtree(monkeyMap)) {
            long otherValue = monkeyMap.get(currentMonkey.rightChildName).compute(monkeyMap);
            switch (currentMonkey.operation) {
                case 0: // +
                    return getTargetVal(targetValue - otherValue,
                            monkeyMap.get(currentMonkey.leftChildName),
                            monkeyMap);
                case 1: // -
                    return getTargetVal(targetValue + otherValue,
                            monkeyMap.get(currentMonkey.leftChildName),
                            monkeyMap);
                case 2: // *
                    return getTargetVal(targetValue / otherValue,
                            monkeyMap.get(currentMonkey.leftChildName),
                            monkeyMap);
                case 3: // /
                    return getTargetVal(targetValue * otherValue,
                            monkeyMap.get(currentMonkey.leftChildName),
                            monkeyMap);
            }
        } else {
            long otherValue = monkeyMap.get(currentMonkey.leftChildName).compute(monkeyMap);
            switch (currentMonkey.operation) {
                case 0: // +
                    return getTargetVal(targetValue - otherValue,
                            monkeyMap.get(currentMonkey.rightChildName),
                            monkeyMap);
                case 1: // -
                    return getTargetVal(otherValue - targetValue,
                            monkeyMap.get(currentMonkey.rightChildName),
                            monkeyMap);
                case 2: // *
                    return getTargetVal(targetValue / otherValue,
                            monkeyMap.get(currentMonkey.rightChildName),
                            monkeyMap);
                case 3: // /
                    return getTargetVal(otherValue / targetValue,
                            monkeyMap.get(currentMonkey.rightChildName),
                            monkeyMap);
            }
        }
        throw new RuntimeException("Got to the end of recursive function without recursing?!");
    }


    private static abstract class Monkey {
        String name;

        Monkey(String name) {
            this.name = name;
        }

        public abstract boolean containsHumnInSubtree(Map<String, Monkey> monkeyMap);

        public abstract long compute(Map<String, Monkey> monkeyMap);
    }

    private static class NumberMonkey extends Monkey {

        long number;

        NumberMonkey(String name, long number) {
            super(name);
            this.number = number;
        }

        @Override
        public boolean containsHumnInSubtree(Map<String, Monkey> monkeyMap) {
            return this.name.equals("humn");
        }

        @Override
        public long compute(Map<String, Monkey> monkeyMap) {
            return number;
        }
    }

    private static class OperationMonkey extends Monkey {
        // 0: +
        // 1: -
        // 2: *
        // 3: /
        int operation;
        String leftChildName;
        String rightChildName;

        OperationMonkey(String name, String leftChildName, String rightChildName, int operation) {
            super(name);
            this.leftChildName = leftChildName;
            this.rightChildName = rightChildName;
            this.operation = operation;
        }

        @Override
        public boolean containsHumnInSubtree(Map<String, Monkey> monkeyMap) {
            return monkeyMap.get(leftChildName).containsHumnInSubtree(monkeyMap) ||
                    monkeyMap.get(rightChildName).containsHumnInSubtree(monkeyMap);
        }

        @Override
        public long compute(Map<String, Monkey> monkeyMap) {
            switch(operation) {
                case 0:
                    return monkeyMap.get(leftChildName).compute(monkeyMap) +
                            monkeyMap.get(rightChildName).compute(monkeyMap);
                case 1:
                    return monkeyMap.get(leftChildName).compute(monkeyMap) -
                            monkeyMap.get(rightChildName).compute(monkeyMap);
                case 2:
                    return monkeyMap.get(leftChildName).compute(monkeyMap) *
                            monkeyMap.get(rightChildName).compute(monkeyMap);
                case 3:
                    return monkeyMap.get(leftChildName).compute(monkeyMap) /
                            monkeyMap.get(rightChildName).compute(monkeyMap);
                default:
                    throw new RuntimeException("Unknown Operation");
            }
        }
    }
}
