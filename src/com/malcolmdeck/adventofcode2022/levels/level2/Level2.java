package com.malcolmdeck.adventofcode2022.levels.level2;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.util.Scanner;

public class Level2 {

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level2\\level2data.txt");
        try {
            Scanner scanner = new Scanner(file);
            int score = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                char opponentChoice = line.charAt(0);
                char myChoice = line.charAt(2);
                // Add points based on what I chose
                switch (myChoice) {
                    case 'X':
                        score += 1;
                        break;
                    case 'Y':
                        score += 2;
                        break;
                    case 'Z':
                        score += 3;
                        break;
                    default:
                        throw new RuntimeException("Unknown char thrown: " + myChoice);
                }
                //Compute if I won, lost, or drew and add those points.
                switch (opponentChoice) {
                    case 'A':
                        if (myChoice == 'Y') {
                            score += 6;
                        } else if (myChoice == 'X') {
                            score += 3;
                        }
                        break;
                    case 'B':
                        if (myChoice == 'Z') {
                            score += 6;
                        } else if (myChoice == 'Y') {
                            score += 3;
                        }
                        break;
                    case 'C':
                        if (myChoice == 'X') {
                            score += 6;
                        } else if (myChoice == 'Z') {
                            score += 3;
                        }
                        break;
                    default:
                        throw new RuntimeException("Unknown char thrown: " + myChoice);
                }
            }
            System.out.println("Score: " + score);
        } catch (Exception e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level2\\level2data.txt");
        try {
            Scanner scanner = new Scanner(file);
            int score = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                char opponentChoice = line.charAt(0);
                char gameOutcome = line.charAt(2);
                // Add points for win, loss, or draw.
                switch (gameOutcome) {
                    case 'X':
                        score += 0;
                        break;
                    case 'Y':
                        score += 3;
                        break;
                    case 'Z':
                        score += 6;
                        break;
                    default:
                        throw new RuntimeException("Unknown char thrown: " + gameOutcome);
                }
                // Compute what I threw and add its respective point score.
                switch (opponentChoice) {
                    case 'A':
                        if (gameOutcome == 'X') {
                            score += 3;
                        } else if (gameOutcome == 'Y') {
                            score += 1;
                        } else {
                            score += 2;
                        }
                        break;
                    case 'B':
                        if (gameOutcome == 'X') {
                            score += 1;
                        } else if (gameOutcome == 'Y') {
                            score += 2;
                        } else {
                            score += 3;
                        }
                        break;
                    case 'C':
                        if (gameOutcome == 'X') {
                            score += 2;
                        } else if (gameOutcome == 'Y') {
                            score += 3;
                        } else {
                            score += 1;
                        }
                        break;
                    default:
                        throw new RuntimeException("Unknown char thrown: " + gameOutcome);
                }
            }
            System.out.println("Score: " + score);
        } catch (Exception e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }
}
