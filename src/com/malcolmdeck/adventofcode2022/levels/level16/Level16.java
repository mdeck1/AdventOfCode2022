package com.malcolmdeck.adventofcode2022.levels.level16;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Level16 {

    private static final boolean DEBUG = true;

    private static Map<String, Integer> partialPathCache = new HashMap<>();
    private static Map<String, PathsAndMaxFlow> partialPathCacheForPair = new HashMap<>();

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level16\\level16data.txt");
        try {
            // Parse in input, storing the adjacency information
            Scanner scanner = new Scanner(file);
            Map<String, Integer> valveToFlowRate = new HashMap<>();
            Map<String, List<String>> valveToAdjacentValves = new HashMap<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                String name = parts[1];
                Integer flowRate =
                        Integer.parseInt(parts[4].substring(5, parts[4].length() - 1));
                List<String> neighbors = new ArrayList<>();
                for (int i = 9; i < parts.length; ++i) {
                    if (i < parts.length - 1) {
                        neighbors.add(parts[i].substring(0, parts[i].length() - 1));
                    } else {
                        neighbors.add(parts[i]);
                    }
                }
                valveToFlowRate.put(name, flowRate);
                valveToAdjacentValves.put(name, neighbors);
            }
            int maxPressureReleased = getMaxPressureReleaseable(valveToFlowRate, valveToAdjacentValves);
            System.out.println("Max Pressure Releaseable: " + maxPressureReleased);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level16\\level16data.txt");
        try {
            // Read in the file, store valve adjacency, flow rates, and valves with non-zero flow rates.
            Scanner scanner = new Scanner(file);
            Map<String, Integer> valveToFlowRate = new HashMap<>();
            Map<String, List<String>> valveToAdacentValves = new HashMap<>();
            List<String> importantValves = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                String name = parts[1];
                Integer flowRate =
                        Integer.parseInt(parts[4].substring(5, parts[4].length() - 1));
                List<String> neighbors = new ArrayList<>();
                for (int i = 9; i < parts.length; ++i) {
                    if (i < parts.length - 1) {
                        neighbors.add(parts[i].substring(0, parts[i].length() - 1));
                    } else {
                        neighbors.add(parts[i]);
                    }
                }
                valveToFlowRate.put(name, flowRate);
                valveToAdacentValves.put(name, neighbors);
                if (flowRate > 0) {
                    importantValves.add(name);
                }
            }
            // Compute the distances from every valve with non-zero flow to every other valve with non-zero flow, plus
            // the starting valve.
            if (!importantValves.contains("AA")) {
                importantValves.add("AA");
            }
            Map<String, Integer> distanceBetweenImportantValves =
                    new HashMap<>();
            importantValves.sort(Comparator.comparing(String::toString));
            for (int i = 0; i < importantValves.size() - 1; ++i) {
                for (int j = i + 1; j < importantValves.size(); ++j) {
                    int shortestPathLength =
                            findShortestPath(importantValves.get(i), importantValves.get(j), valveToAdacentValves);
                    distanceBetweenImportantValves.put(
                            importantValves.get(i) + importantValves.get(j),
                            shortestPathLength);
                    distanceBetweenImportantValves.put(
                            importantValves.get(j) + importantValves.get(i),
                            shortestPathLength);

                }
            }
            PathsAndMaxFlow maxPressureReleased = maxPressureFromImportantPoints(
                    valveToFlowRate,
                    importantValves,
                    distanceBetweenImportantValves);
            System.out.println("Max Pressure Releaseable: " + maxPressureReleased.maxFlow);
            System.out.println("PersonPath: " + "AA" + maxPressureReleased.personPath);
            System.out.println("ElephantPath: " + "AA" + maxPressureReleased.elephantPath);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    // Uses DFS to compute the shortest path distance between start and end
    private static int findShortestPath(String start, String end, Map<String, List<String>> valveToAdjacentValves) {
        List<String> alreadyTraversed = new ArrayList<>();
        alreadyTraversed.add(start);
        return findShortestPathRecursive(start, end, valveToAdjacentValves, alreadyTraversed);
    }

    private static int findShortestPathRecursive(String start, String end, Map<String, List<String>> valveToAdjacentValves, List<String> alreadyTraversed) {
        if (start.equals(end)) {
            return 0;
        }
        int minDist = Integer.MAX_VALUE;
        for (String next : valveToAdjacentValves.get(start)) {
            if (!alreadyTraversed.contains(next)) {
                List<String> newTraversed = new ArrayList<>(alreadyTraversed);
                newTraversed.add(next);
                int remainingLength = findShortestPathRecursive(next, end, valveToAdjacentValves, newTraversed);
                if (remainingLength < Integer.MAX_VALUE) {
                    int thisDist = 1 + remainingLength;
                    if (thisDist < minDist) {
                        minDist = thisDist;
                    }
                }
            }
        }
        return minDist;
    }

    private static int getMaxPressureReleaseable(
            Map<String, Integer> valveToFlowRate,
            Map<String, List<String>> valveToAdacentValves) {
        return getMaxPressureForRemainingPartialPath(
                "AA",
                valveToFlowRate,
                valveToAdacentValves,
                new ArrayList<>(),
                30);
    }

    // Computes the maxPressure for the remaining time given.
    // Uses the following techniques:
    // DFS
    // Caching
    private static int getMaxPressureForRemainingPartialPath(
            String currentLocation,
            Map<String, Integer> valveToFlowRate,
            Map<String, List<String>> valveToAdjacentValves,
            List<String> locationsAlreadyOpen,
            int minutesRemaining) {
        // Termination condition
        if (minutesRemaining < 1) {
            return 0;
        }
        // Cache check
        String cacheName = getCacheName(currentLocation, locationsAlreadyOpen, minutesRemaining);
        if (partialPathCache.get(cacheName) != null) {
            return partialPathCache.get(cacheName);
        }
        // DFS
        int thisFlowRate = valveToFlowRate.get(currentLocation);
        int maxFlow = 0;
        for (String neighbor : valveToAdjacentValves.get(currentLocation)) {
            // Ignore the current location
            int withOutThisValveOpen =
                    getMaxPressureForRemainingPartialPath(
                            neighbor,
                            valveToFlowRate,
                            valveToAdjacentValves,
                            locationsAlreadyOpen,
                            minutesRemaining - 1);
            maxFlow = maxFlow > withOutThisValveOpen ? maxFlow : withOutThisValveOpen;

            if (thisFlowRate > 0 && !locationsAlreadyOpen.contains(currentLocation)) {
                List<String> newOpenList = new ArrayList<>(locationsAlreadyOpen);
                newOpenList.add(currentLocation);
                int withThisValveOpen =
                        thisFlowRate * (minutesRemaining - 1) +
                                getMaxPressureForRemainingPartialPath(
                                        neighbor,
                                        valveToFlowRate,
                                        valveToAdjacentValves,
                                        newOpenList,
                                        minutesRemaining - 2);

                maxFlow = maxFlow > withThisValveOpen ? maxFlow : withThisValveOpen;
            }
        }
        // Cache update
        partialPathCache.put(cacheName, maxFlow);
        return maxFlow;
    }

    private static PathsAndMaxFlow maxPressureFromImportantPoints(
            Map<String, Integer> valveToFlowRate,
            List<String> importantValves,
            Map<String, Integer> distanceBetweenImportantValves) {
        importantValves.remove("AA");
        PathsAndMaxFlow result = getMaxPressureForRemainingPartialPathForPairWithImportantPointsList(
                "AA",
                "AA",
                valveToFlowRate,
                new ArrayList<>(),
                importantValves,
                distanceBetweenImportantValves,
                26,
                26);
        return result;
    }

    // Computes the max flow findable for the given remaining places to check.
    // Uses the following techniques:
    // - DFS, but excluding all nodes that don't matter (HUGE time saver, from 52! possibilities
    //     down to 14!
    // - Cache
    private static PathsAndMaxFlow getMaxPressureForRemainingPartialPathForPairWithImportantPointsList(
            String currentLocation,
            String currentElephantLocation,
            Map<String, Integer> valveToFlowRate,
            List<String> locationsAlreadyOpen,
            List<String> importantValvesRemaining,
            Map<String, Integer> distanceBetweenImportantValves,
            int personMinutesRemaining,
            int elephantMinutesRemaining) {
        PathsAndMaxFlow pathsAndMaxFlow = new PathsAndMaxFlow("", "", 0);
        // Termination condition
        if (importantValvesRemaining.size() < 1
            || (personMinutesRemaining < 1 && elephantMinutesRemaining < 1)) {
            return pathsAndMaxFlow;
        }
        // Cache check
        String cacheName =
                getCacheNameForPair(
                        currentLocation,
                        currentElephantLocation,
                        locationsAlreadyOpen,
                        personMinutesRemaining,
                        elephantMinutesRemaining);
        if (partialPathCacheForPair.get(cacheName) != null) {
            return partialPathCacheForPair.get(cacheName);
        }
        // DFS on important points
        // Iterate through all of the valves for me
        for (String next : importantValvesRemaining) {
            int timeRemainingAtNextLocation = personMinutesRemaining - distanceBetweenImportantValves.get(currentLocation + next) - 1;
            if (timeRemainingAtNextLocation > 0) {
                List<String> newLocationsOpen = new ArrayList<>(locationsAlreadyOpen);
                newLocationsOpen.add(next);
                List<String> newValvesRemaining = new ArrayList<>(importantValvesRemaining);
                newValvesRemaining.remove(next);
                PathsAndMaxFlow thisFlow =
                        getMaxPressureForRemainingPartialPathForPairWithImportantPointsList(
                                next,
                                currentElephantLocation,
                                valveToFlowRate,
                                newLocationsOpen,
                                newValvesRemaining,
                                distanceBetweenImportantValves,
                                timeRemainingAtNextLocation,
                                elephantMinutesRemaining);
                thisFlow = thisFlow.copy();
                thisFlow.maxFlow += timeRemainingAtNextLocation * valveToFlowRate.get(next);
                thisFlow.personPath = next + thisFlow.personPath;
                if (thisFlow.maxFlow > pathsAndMaxFlow.maxFlow) {
                    if (DEBUG && personMinutesRemaining == 26 && elephantMinutesRemaining == 26) {
                        System.out.println("Candidate maxFlow computed: "  + thisFlow.maxFlow);
                        System.out.println("Candidate personPath: "  + thisFlow.personPath);
                        System.out.println("Candidate elephantPath: "  + thisFlow.elephantPath);
                    }
                    pathsAndMaxFlow = thisFlow;
                }
            }
        }
        // Iterate through all of the valves for the elephant
        for (String next : importantValvesRemaining) {
            int timeRemainingAtNextLocation = elephantMinutesRemaining - distanceBetweenImportantValves.get(currentElephantLocation + next) - 1;
            if (timeRemainingAtNextLocation > 0) {
                List<String> newLocationsOpen = new ArrayList<>(locationsAlreadyOpen);
                newLocationsOpen.add(next);
                List<String> newValvesRemaining = new ArrayList<>(importantValvesRemaining);
                newValvesRemaining.remove(next);
                PathsAndMaxFlow thisFlow = getMaxPressureForRemainingPartialPathForPairWithImportantPointsList(
                                currentLocation,
                                next,
                                valveToFlowRate,
                                newLocationsOpen,
                                newValvesRemaining,
                                distanceBetweenImportantValves,
                                personMinutesRemaining,
                            timeRemainingAtNextLocation);
                thisFlow = thisFlow.copy();
                thisFlow.maxFlow += timeRemainingAtNextLocation * valveToFlowRate.get(next);
                thisFlow.elephantPath = next + thisFlow.elephantPath;
                if (thisFlow.maxFlow > pathsAndMaxFlow.maxFlow) {
                    if (DEBUG && personMinutesRemaining == 26 && elephantMinutesRemaining == 26) {
                        System.out.println("Candidate maxFlow computed: "  + thisFlow.maxFlow);
                        System.out.println("Candidate personPath: "  + thisFlow.personPath);
                        System.out.println("Candidate elephantPath: "  + thisFlow.elephantPath);
                    }
                    pathsAndMaxFlow = thisFlow;
                }
            }
        }
        // Cache update
        partialPathCacheForPair.put(cacheName, pathsAndMaxFlow);
        return pathsAndMaxFlow;
    }

    private static class PathsAndMaxFlow {
        String personPath;
        String elephantPath;
        int maxFlow;

        PathsAndMaxFlow(String personPath, String elephantPath, int maxFlow) {
            this.personPath = personPath;
            this.elephantPath = elephantPath;
            this.maxFlow = maxFlow;
        }

        PathsAndMaxFlow copy() {
            return new PathsAndMaxFlow(personPath, elephantPath, maxFlow);
        }
    }


    private static String getCacheName(
            String location,
            List<String> alreadyOpen,
            Integer minutesRemaining) {
        StringBuilder builder = new StringBuilder()
                .append(location);
        builder.append("/");
        for (String open : alreadyOpen) {
            builder.append(open);
        }
        builder.append("/");
        builder.append(minutesRemaining);

        return builder.toString();
    }

    private static String getCacheNameForPair(
            String location,
            String elephantLocation,
            List<String> alreadyOpen,
            int minutesRemaining,
            int elephantMinutesRemaining) {
        StringBuilder builder = new StringBuilder()
                .append(location)
                .append("/")
                .append(elephantLocation)
                .append("/");
        alreadyOpen.sort(Comparator.comparing(String::toString));
        for (String open : alreadyOpen) {
            builder.append(open);
        }
        builder.append("/");
        builder.append(minutesRemaining);
        builder.append("/");
        builder.append(elephantMinutesRemaining);
        return builder.toString();
    }
}
