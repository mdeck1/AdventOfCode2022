package com.malcolmdeck.adventofcode2022.levels.level15;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Level15 {

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level15\\level15data.txt");
        try {
            Scanner scanner = new Scanner(file);
            // Read in all Sensor-Beacon pairs, put beacons into a set for later.
            List<SensorBeaconPair> sensorBeaconPairs = new ArrayList<>();
            Set<Beacon> beaconSet = new HashSet<>();
            while (scanner.hasNextLine()) {
                SensorBeaconPair sensorBeaconPair = new SensorBeaconPair(scanner.nextLine());
                sensorBeaconPairs.add(sensorBeaconPair);
                beaconSet.add(new Beacon(sensorBeaconPair.beaconX, sensorBeaconPair.beaconY));
            }

            int WHICH_LINE = 2000000;
            List<Interval> coveringOfTheLine =
                    getListOfCoveringFromSensorBeaconPairsAndLine(sensorBeaconPairs, WHICH_LINE);

            // Count the size of the coverings.
            int size = 0;
            for (Interval interval : coveringOfTheLine) {
                size += (interval.second - interval.first) + 1;
            }
            // Remove beacons that are on the line in question.
            for (Beacon beacon : beaconSet) {
                if (beacon.beaconY == WHICH_LINE) {
                    size--;
                }
            }
            System.out.println("Covering size: " + size);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level15\\level15data.txt");
        try {
            Scanner scanner = new Scanner(file);
            // Read in all Sensor-Beacon pairs
            List<SensorBeaconPair> sensorBeaconPairs = new ArrayList<>();
            while (scanner.hasNextLine()) {
                sensorBeaconPairs.add(new SensorBeaconPair(scanner.nextLine()));
            }
            // Isolate the y coordinate whose covering doesn't cover the entire row from 0
            // to SIZE. That row must contain the beacon.
            int SIZE = 4000000;
            int y;
            for (y = 0; y < SIZE; ++y) {
                if (y % 1000000 == 0) {
                    System.out.println("Now processing for y = " + y);
                }
                List<Interval> coveringOfTheLine = getListOfCoveringFromSensorBeaconPairsAndLine(sensorBeaconPairs, y);
                coveringOfTheLine = truncateIntervals(coveringOfTheLine, 0 , SIZE);
                int size = 0;
                for (Interval interval : coveringOfTheLine) {
                    size += (interval.second - interval.first) + 1;
                }
                // If the size of the covering is < SIZE + 1, it's missing a spot - that means
                // we're in the right y coordinate.
                if (size < SIZE + 1) {
                    System.out.println("Found y! y = " + y);
                    break;
                }
            }
            // Try every x coordinate - check against all beacons to see if it's in range of that beacon or not.
            // If it's in range of no beacons, then it's the right x coordinate.
            // This solution could be replaced with the way that we found y, but 4000000 x coordinates are fast enough
            // to brute force this problem (whereas 16,000,000,000,000 (x,y) pairs would take ~2000 minutes to run, which
            // is too slow for my liking), but I didn't feel like refactoring.
            for (int x = 0; x < SIZE + 1; ++x) {
                if (x % 1000000 == 0) {
                    System.out.println("Now processing for x = " + x);
                }
                boolean cannotBeReached = true;
                for (SensorBeaconPair sensorBeaconPair : sensorBeaconPairs) {
                    if (sensorBeaconPair.pointInRange(x, y)) {
                        cannotBeReached = false;
                        break;
                    }
                }
                if (cannotBeReached) {
                    System.out.println("Solution: (" + x + ", " + y + ")");
                    long tuningFrequency = 4000000l * ((long) x) + y;
                    System.out.println("Tuning Frequency: " + tuningFrequency);
                    return;
                }
            }
            System.out.println("No solution found");
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    /**
     * Figures out the interval set that defines the covering of line whichLine
     * (as in which spaces can and cannot be seen on the line y = whichLine by the
     * given set of sensors).
     */
    private static List<Interval> getListOfCoveringFromSensorBeaconPairsAndLine(
            List<SensorBeaconPair> sensorBeaconPairs,
            int whichLine) {
        List<Interval> coveringOfTheLine = new ArrayList<>();
        for (SensorBeaconPair sensorBeaconPair : sensorBeaconPairs) {
            Interval intersection = sensorBeaconPair.intersectionOnLine(whichLine);
            if (intersection != null) {
                coveringOfTheLine.add(intersection);
            }
        }
        return combineIntervals(coveringOfTheLine);
    }

    /** Combine the coverings into a list of non-overlapping coverings. */
    private static List<Interval> combineIntervals(List<Interval> intervalList) {
        intervalList.sort(new Comparator<Interval>() {
            @Override
            public int compare(Interval o1, Interval o2) {
                return o1.first - o2.first;
            }
        });
        int i = 0;
        while (i < intervalList.size() - 1) {
            if (intervalList.get(i).overlaps(intervalList.get(i + 1))) {
                Interval first = intervalList.get(i);
                Interval second = intervalList.get(i + 1);
                intervalList.remove(second);
                intervalList.set(i, first.combine(second));
            } else {
                i++;
            }
        }
        return intervalList;
    }

    /**
     * Truncates the given set of intervals so that all values lie between min & max.
     * There is a known bug here where if min or max are not in the original interval list but
     * extremes beyond those values are present, you'll get spurious intervals of [min,min] and/or
     * [max,max] which may mess up your calculations.
     * I'm not gonna fix that right now, but that's pretty trivial to fix: for intervals outside the
     * range, remove them rather than setting them to be in the range.
     */
    private static List<Interval> truncateIntervals(List<Interval> intervalList, int min, int max) {
        for (Interval interval : intervalList) {
            if (interval.first < min) {
                interval.first = min;
            }
            if (interval.second > max) {
                interval.second = max;
            }
            if (interval.first > max) {
                interval.first = max;
            }
            if (interval.second < min) {
                interval.second = min;
            }
        }
        return combineIntervals(intervalList);
    }

    /**
     * Represents an input line of the form:
     * Sensor at x=3988693, y=3986119: closest beacon is at x=3979063, y=3856315
     * and computes some intermediaries for use in later steps.
     */
    private static class SensorBeaconPair {
        int sensorX;
        int sensorY;
        int beaconX;
        int beaconY;
        int manhattanDist;

        SensorBeaconPair(String line) {
            String[] words = line.split(" ");
            sensorX = Integer.parseInt(words[2].substring(2, words[2].length() - 1));
            sensorY = Integer.parseInt(words[3].substring(2, words[3].length() - 1));
            beaconX = Integer.parseInt(words[8].substring(2, words[8].length() - 1));
            beaconY = Integer.parseInt(words[9].substring(2, words[9].length()));
            manhattanDist = Math.abs(sensorX - beaconX) + Math.abs(sensorY - beaconY);
        }

        // Determines if the given (x,y) point falls within the range determined by this
        // SensorBeaconPair, which is just if it's as close or closer than the Manhattan
        // distance of the beacon.
        boolean pointInRange(int x, int y) {
            return manhattanDist >= Math.abs(sensorX - x) + Math.abs(sensorY - y);
        }

        // Returns an IntPair that represents the x-range that falls within this sensor's reach
        // for the given y coordinate. If there is no overlap, returns null.
        Interval intersectionOnLine(int yCoordinate) {
            if (sensorY < yCoordinate) {
                if (sensorY + manhattanDist < yCoordinate) {
                    return null;
                }
            } else {
                if (sensorY - manhattanDist > yCoordinate) {
                    return null;
                }
            }
            int remainingDist = manhattanDist - Math.abs(sensorY - yCoordinate);
            return new Interval(sensorX - remainingDist, sensorX + remainingDist);
        }
    }

    // Representation of a Beacon - needed for creating the set of Beacons so that
    // size computation for #partOne() can exclude beacons on the given line.
    private static class Beacon {
        int beaconX;
        int beaconY;

        Beacon(int x, int y) {
            this.beaconX = x;
            this.beaconY = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Beacon beacon = (Beacon) o;
            return beaconX == beacon.beaconX && beaconY == beacon.beaconY;
        }

        @Override
        public int hashCode() {
            return Objects.hash(beaconX, beaconY);
        }
    }

    // Used to represent an interval as a bound pair of integers.
    private static class Interval {
        int first;
        int second;
        Interval(int first, int second) {
            this.first = Math.min(first, second);
            this.second = Math.max(first, second);
        }

        // Determines if this interval and other overlap.
        boolean overlaps(Interval other) {
            if (this.first < other.first &&
                this.second >= other.first) {
                return true;
            } else if (other.first < this.first &&
                other.second >= this.first) {
                return true;
            } else if (this.first == other.first) {
                return true;
            }
            return false;
        }

        // Returns a new interval that is the superset of this interval and that interval.
        Interval combine(Interval other) {
            if (!overlaps(other)) {
                throw new RuntimeException("Trying to combine non-overlapping intervals!");
            }
            return new Interval(Math.min(this.first, other.first), Math.max(this.second, other.second));
        }
    }
}
