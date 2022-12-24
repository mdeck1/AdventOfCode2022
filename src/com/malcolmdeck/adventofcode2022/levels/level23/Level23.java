package com.malcolmdeck.adventofcode2022.levels.level23;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Level23 {

    private static final boolean DEBUG = true;

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level23\\level23data.txt");
        try {
            Scanner scanner = new Scanner(file);
            List<Elf> elfLocations = new ArrayList<>();
            int whichRow = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                for (int whichColumn = 0; whichColumn < line.length(); ++whichColumn) {
                    if (line.charAt(whichColumn) == '#') {
                        elfLocations.add(new Elf(whichColumn, whichRow));
                    }
                }
                ++whichRow;
            }
            for (int i = 0; i < 10; ++i) {
                if (DEBUG) {
                    System.out.println("Elf Locations after " + i + " rounds: ");
                    printOutAllElfLocations(elfLocations);
                }
                simulateRound(elfLocations, i % 4);
            }
            if (DEBUG) {
                System.out.println("Elf Locations after 10 rounds: ");
                printOutAllElfLocations(elfLocations);
            }
            System.out.println("Number of Empty Groud tiles: " + numberOfEmptyGroundTiles(elfLocations));
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level23\\level23data.txt");
        try {
            Scanner scanner = new Scanner(file);
            List<Elf> elfLocations = new ArrayList<>();
            int whichRow = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                for (int whichColumn = 0; whichColumn < line.length(); ++whichColumn) {
                    if (line.charAt(whichColumn) == '#') {
                        elfLocations.add(new Elf(whichColumn, whichRow));
                    }
                }
                ++whichRow;
            }
            int whichRound = 0;
            while (true) {
                if (DEBUG && whichRound % 100 == 0) {
                    System.out.println("On round: " + whichRound);
                }
                int numElvesMoved = simulateRound(elfLocations, whichRound % 4);
                ++whichRound;
                if (numElvesMoved == 0) {
                    break;
                }
            }
            if (DEBUG) {
                System.out.println("Elf Locations after 10 rounds: ");
                printOutAllElfLocations(elfLocations);
            }
            System.out.println("First round with no movement: " + whichRound);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    private static void printOutAllElfLocations(List<Elf> elfLocations) {
        int minX = 0;
        int maxX = 13;
        int minY = 0;
        int maxY = 11;
        // TODO: adjust bounds based on input if we need to test prod data
        for (int y = minY; y <= maxY; ++y) {
            for (int x = minX; x <= maxX; ++x) {
                if (elfLocations.contains(new Elf(x, y))) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }

        System.out.println();
    }

    private static int numberOfEmptyGroundTiles(List<Elf> elfLocations) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (Elf elf : elfLocations) {
            if (minX > elf.x) {
                minX = elf.x;
            }
            if (minY > elf.y) {
                minY = elf.y;
            }
            if (maxX < elf.x) {
                maxX = elf.x;
            }
            if (maxY < elf.y) {
                maxY = elf.y;
            }
        }
        return (maxX - minX + 1) * (maxY - minY + 1) - elfLocations.size();
    }

    private static int simulateRound(List<Elf> elfLocations, int directionOrder) {
        List<ProposedElfLocation> proposedElfLocations =
                new ArrayList<>(elfLocations.size());
        for (Elf currentElf : elfLocations) {
            if (currentElf.isAnyoneNextToMe(elfLocations)) {
                ProposedElfLocation nextLocation =
                        currentElf.getNextProposedLocation(elfLocations, directionOrder);
                if (nextLocation != null) {
                    proposedElfLocations.add(nextLocation);
                }
            }
        }
        proposedElfLocations.sort(new Comparator<ProposedElfLocation>() {
            @Override
            public int compare(ProposedElfLocation o1, ProposedElfLocation o2) {
                return (o1.newLocation.x - o2.newLocation.x) * 10000 +
                        (o1.newLocation.y - o2.newLocation.y);
            }
        });
        for (int i = 0; i < proposedElfLocations.size() - 1; ++i) {
            if (proposedElfLocations.get(i).equals(proposedElfLocations.get(i+1))) {
                while (i < proposedElfLocations.size() - 1 &&
                        proposedElfLocations.get(i).equals(proposedElfLocations.get(i + 1))) {
                    proposedElfLocations.remove(i + 1);
                }
                proposedElfLocations.remove(i);
                --i;
            }
        }
        for (ProposedElfLocation proposedElfLocation : proposedElfLocations) {
            elfLocations.remove(proposedElfLocation.oldLocation);
            elfLocations.add(proposedElfLocation.newLocation);
        }
        return proposedElfLocations.size();
    }

    static class Elf {
        int x;
        int y;

        Elf(int x, int y) {
            this.x = x;
            this.y = y;
        }

        ProposedElfLocation getNextProposedLocation(List<Elf> elfLocations, int directionOrder) {
            int count = 0;
            ProposedElfLocation nextLocation = null;
            switch (directionOrder) {
                case 0: // N,S,W,E
                    nextLocation = getNorthLocationOrNull(elfLocations);
                    if (nextLocation != null) {
                        return nextLocation;
                    }
                    ++count;
                case 1: // S,W,E,N
                    nextLocation = getSouthLocationOrNull(elfLocations);
                    if (nextLocation != null) {
                        return nextLocation;
                    }
                    ++count;
                case 2: // W,E,N,S
                    nextLocation = getWestLocationOrNull(elfLocations);
                    if (nextLocation != null) {
                        return nextLocation;
                    }
                    ++count;
                case 3: // E,N,S,W
                    nextLocation = getEastLocationOrNull(elfLocations);
                    if (nextLocation != null) {
                        return nextLocation;
                    }
                    ++count;
            }
            if (count < 4) {
                nextLocation = getNorthLocationOrNull(elfLocations);
                if (nextLocation != null) {
                    return nextLocation;
                }
                ++count;
            }
            if (count < 4) {
                nextLocation = getSouthLocationOrNull(elfLocations);
                if (nextLocation != null) {
                    return nextLocation;
                }
                ++count;
            }
            if (count < 4) {
                nextLocation = getWestLocationOrNull(elfLocations);
                if (nextLocation != null) {
                    return nextLocation;
                }
                ++count;
            }
            return null;
        }

        ProposedElfLocation getNorthLocationOrNull(List<Elf> elfLocations) {
            if (!elfLocations.contains(new Elf(x-1, y-1)) &&
                    !elfLocations.contains(new Elf(x, y-1)) &&
                    !elfLocations.contains(new Elf(x+1, y-1))) {
                return new ProposedElfLocation(this, new Elf(x, y-1));
            }
            return null;
        }

        ProposedElfLocation getSouthLocationOrNull(List<Elf> elfLocations) {
            if (!elfLocations.contains(new Elf(x-1, y+1)) &&
                    !elfLocations.contains(new Elf(x, y+1)) &&
                    !elfLocations.contains(new Elf(x+1, y+1))) {
                return new ProposedElfLocation(this, new Elf(x, y+1));
            }
            return null;
        }

        ProposedElfLocation getWestLocationOrNull(List<Elf> elfLocations) {
            if (!elfLocations.contains(new Elf(x-1, y-1)) &&
                    !elfLocations.contains(new Elf(x-1, y)) &&
                    !elfLocations.contains(new Elf(x-1, y+1))) {
                return new ProposedElfLocation(this, new Elf(x-1, y));
            }
            return null;
        }

        ProposedElfLocation getEastLocationOrNull(List<Elf> elfLocations) {
            if (!elfLocations.contains(new Elf(x+1, y-1)) &&
                    !elfLocations.contains(new Elf(x+1, y)) &&
                    !elfLocations.contains(new Elf(x+1, y+1))) {
                return new ProposedElfLocation(this, new Elf(x+1, y));
            }
            return null;
        }

        boolean isAnyoneNextToMe(List<Elf> elfLocations) {
            return elfLocations.contains(new Elf (x-1, y-1)) ||
                    elfLocations.contains(new Elf (x-1, y)) ||
                    elfLocations.contains(new Elf (x-1, y+1)) ||
                    elfLocations.contains(new Elf (x, y-1)) ||
                    elfLocations.contains(new Elf (x, y+1)) ||
                    elfLocations.contains(new Elf (x+1, y-1)) ||
                    elfLocations.contains(new Elf (x+1, y)) ||
                    elfLocations.contains(new Elf (x+1, y+1));
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Elf)) {
                return false;
            }
            Elf otherElf = (Elf) o;
            return this.x == otherElf.x && this.y == otherElf.y;
        }
    }

    static class ProposedElfLocation {
        Elf oldLocation;
        Elf newLocation;

        ProposedElfLocation(Elf oldLocation, Elf newLocation) {
            this.oldLocation = oldLocation;
            this.newLocation = newLocation;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof ProposedElfLocation)) {
                return false;
            }
            ProposedElfLocation otherElf = (ProposedElfLocation) o;
            return this.newLocation.x == otherElf.newLocation.x &&
                    this.newLocation.y == otherElf.newLocation.y;
        }


    }
}
