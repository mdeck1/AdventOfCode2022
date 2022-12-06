package com.malcolmdeck.adventofcode2022.levels.level5;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Level5 {

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level5\\level5data.txt");
        try {
            // Initializing big data structures
            Scanner scanner = new Scanner(file);
            ArrayList<Stack<Character>> loadingDock = new ArrayList<>();
            for (int i = 0; i < 9; ++i) {
                loadingDock.add(new Stack<Character>());
            }
            // Read in the crate stack
            int lineCount = 8;
            while (lineCount > 0) {
                String line = scanner.nextLine();
                for (int i = 1; i < line.length() && i < 36; i += 4) {
                    if (line.charAt(i) != ' ') {
                        int whichStack = (i-1)/4;
                        loadingDock.get(whichStack).push(line.charAt(i));
                    }
                }
                --lineCount;
            }
            // Reverse each Stack because we read them in from top to bottom
            for (int i = 0; i < 9; ++i) {
                Character[] temp = new Character[loadingDock.get(i).size()];
                loadingDock.get(i).toArray(temp);
                loadingDock.get(i).clear();
                for (int j = 0; j < temp.length; ++j) {
                    loadingDock.get(i).push(temp[temp.length - j - 1]);
                }
            }
            // Align data
            scanner.nextLine();
            scanner.nextLine();
            // Parse and apply each crane move
            while (scanner.hasNextLine()) {
                String[] commands = scanner.nextLine().split(" ");
                int howMany = Integer.parseInt(commands[1]);
                int fromStack = Integer.parseInt(commands[3]) - 1;
                int toStack = Integer.parseInt(commands[5]) - 1;
                // Crates move one at a time, so we can just push/pop them directly
                for (int i = 0; i < howMany; ++i) {
                    loadingDock.get(toStack).push(
                        loadingDock.get(fromStack).pop());
                }
            }
            System.out.print("Top boxes: ");
            for (int i = 0; i < 9; ++i) {
                System.out.print(loadingDock.get(i).peek());
            }
        } catch (Exception e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level5\\level5data.txt");
        try {
            // Initializing big data structures
            Scanner scanner = new Scanner(file);
            ArrayList<Stack<Character>> loadingDock = new ArrayList<>();
            for (int i = 0; i < 9; ++i) {
                loadingDock.add(new Stack<Character>());
            }
            // Read in the crate stack
            int lineCount = 8;
            while (lineCount > 0) {
                String line = scanner.nextLine();
                for (int i = 1; i < line.length() && i < 36; i += 4) {
                    if (line.charAt(i) != ' ') {
                        int whichStack = (i-1)/4;
                        loadingDock.get(whichStack).push(line.charAt(i));
                    }
                }
                --lineCount;
            }
            // Reverse each Stack because we read them in from top to bottom
            for (int i = 0; i < 9; ++i) {
                Character[] temp = new Character[loadingDock.get(i).size()];
                loadingDock.get(i).toArray(temp);
                loadingDock.get(i).clear();
                for (int j = 0; j < temp.length; ++j) {
                    loadingDock.get(i).push(temp[temp.length - j - 1]);
                }
            }
            // Align data
            scanner.nextLine();
            scanner.nextLine();
            // Read and apply each move
            while (scanner.hasNextLine()) {
                String[] commands = scanner.nextLine().split(" ");
                int howMany = Integer.parseInt(commands[1]);
                int fromStack = Integer.parseInt(commands[3]) - 1;
                int toStack = Integer.parseInt(commands[5]) - 1;
                // Crates move all at once; using an intermediary stack re-reverses them before insertion, so
                // we can use partOne's solution with only 4 extra lines.
                Stack<Character> tempStack = new Stack<>();
                for (int i = 0; i < howMany; ++i) {
                    tempStack.push(
                            loadingDock.get(fromStack).pop());
                }
                for (int i = 0; i < howMany; ++i) {
                    loadingDock.get(toStack).push(
                            tempStack.pop());
                }
            }
            System.out.print("Top boxes: ");
            for (int i = 0; i < 9; ++i) {
                System.out.print(loadingDock.get(i).peek());
            }
        } catch (Exception e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }
}
