package com.malcolmdeck.adventofcode2022.levels.level1;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Level1 {

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level1\\level1data.txt");
        try {
            Scanner scanner = new Scanner(file);
            int maxCalories = -1;
            while (scanner.hasNextLine()) {
                int caloriesSoFar = 0;
                String line = scanner.nextLine();
                while (line != "") {
                    int calories = Integer.parseInt(line);
                    caloriesSoFar += calories;
                    if (scanner.hasNextLine()) {
                        line = scanner.nextLine();
                    } else {
                        break;
                    }
                }
                maxCalories = (maxCalories < caloriesSoFar) ? caloriesSoFar : maxCalories;
            }
            System.out.println("Max Calories: " + maxCalories);
        } catch (Exception e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level1\\level1data.txt");
        try {
            Scanner scanner = new Scanner(file);
            PriorityQueue<Integer> heap = new PriorityQueue<>();
            int maxCalories = -1;
            while (scanner.hasNextLine()) {
                int caloriesSoFar = 0;
                String line = scanner.nextLine();
                while (line != "") {
                    int calories = Integer.parseInt(line);
                    caloriesSoFar += calories;
                    if (scanner.hasNextLine()) {
                        line = scanner.nextLine();
                    } else {
                        break;
                    }
                }
                heap.add(caloriesSoFar);
                while (heap.size() > 3) {
                    heap.remove();
                }
            }
            int topThreeCalories = heap.remove() + heap.remove() + heap.remove();
            if (heap.size() > 0) {
                throw new RuntimeException("Heap still has " + heap.size() + " elements.");
            }
            System.out.println("Top Three Calorie Total: " + topThreeCalories);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

}
