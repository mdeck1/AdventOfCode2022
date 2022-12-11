package com.malcolmdeck.adventofcode2022.levels.level10;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import javax.sound.midi.SysexMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Level10 {

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level10\\level10data.txt");
        try {
            Scanner scanner = new Scanner(file);
            int X = 1;
            int cycleCount = 1;
            int[] importantSignalStrengths = new int[6];
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] command = line.split(" ");
                if (command[0].equals("noop")) { // Takes one cycle
                    cycleCount += 1;
                    logSignalStrengthIfNecessary(cycleCount, X, importantSignalStrengths);
                } else { //addx, which takes two cycles
                    cycleCount += 1;
                    logSignalStrengthIfNecessary(cycleCount, X, importantSignalStrengths);
                    cycleCount += 1;
                    X += Integer.parseInt(command[1]);
                    logSignalStrengthIfNecessary(cycleCount, X, importantSignalStrengths);

                }
            }
            int sum = 0;
            for (int i = 0; i < importantSignalStrengths.length; ++i) {
                sum += importantSignalStrengths[i];
            }
            System.out.println("Sum: " + sum);
        } catch (Exception e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    /**
     * On cycleCounts 20, 60, 100, ... etc., logs (cycleCount * X) to the given
     * array in the appropriate slot.
     */
    private static void logSignalStrengthIfNecessary(
            int cycleCount,
            int X,
            int[] signalStrengths) {
        if ((cycleCount - 20) % 40 == 0) {
            signalStrengths[((cycleCount - 20) / 40)] = X * cycleCount;
        }
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level10\\level10data.txt");
        try {
            Scanner scanner = new Scanner(file);
            int X = 1;
            int cycleCount = 1;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] command = line.split(" ");
                if (command[0].equals("noop")) { // Takes one cycle
                    drawPixelIfNecessary(cycleCount, X);
                    cycleCount += 1;
                } else { //addx, which takes two cycles
                    drawPixelIfNecessary(cycleCount, X);
                    cycleCount += 1;
                    drawPixelIfNecessary(cycleCount, X);
                    cycleCount += 1;
                    X += Integer.parseInt(command[1]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    /**
     * Figures out which pixel is to be drawn, then determines if the sprite
     * is within the window to light up that pixel or not.
     */
    private static void drawPixelIfNecessary(int cycleCount, int X) {
        int whichPixel = (cycleCount - 1) % 40;
        if (Math.abs(X - whichPixel) < 2) {
            System.out.print("#");
        } else {
            System.out.print(".");
        }
        if (whichPixel == 39) {
            System.out.println();
        }
    }

}
