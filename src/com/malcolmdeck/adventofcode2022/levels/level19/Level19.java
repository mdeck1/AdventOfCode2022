package com.malcolmdeck.adventofcode2022.levels.level19;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * TODO(mdeck): Refactor this file to take in a max simulation time, make a second input file
 * (or read from the input file differently in part two). Also, if you can make it not require 8GB of RAM that would be
 * nice.
 */
public class Level19 {

    private static final boolean DEBUG = true;
    private static final boolean FORCE_GC = true;

    // Part One
    // private static final int SIMULATION_END_TIME = 24;
    // Part Two
    private static final int SIMULATION_END_TIME = 32;

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level19\\level19data.txt");
        try {
            Scanner scanner = new Scanner(file);
            List<Blueprint> blueprintList = new ArrayList<>(30);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                blueprintList.add(
                        new Blueprint(
                                Integer.parseInt(parts[1].substring(0, parts[1].length() - 1)),
                                Integer.parseInt(parts[6]),
                                Integer.parseInt(parts[12]),
                                Integer.parseInt(parts[18]),
                                Integer.parseInt(parts[21]),
                                Integer.parseInt(parts[27]),
                                Integer.parseInt(parts[30])));
            }
            int sumQualityLevel = 0;
            for (int i = 0; i < blueprintList.size(); ++i) {
                System.out.println("Starting on blueprint: " + (i + 1));
                Blueprint blueprint = blueprintList.get(i);
                int maxGeodes = maxGeodesFromBlueprint(blueprint);
                sumQualityLevel += blueprint.blueprintId * maxGeodes;
                System.gc();
                Thread.sleep(100);
                if (DEBUG) {
                    System.out.println("Computed a blueprint. New sumQualityLevel: " + sumQualityLevel);
                    System.out.println();
                }
            }
            System.out.println("SumQualityLevel: " + sumQualityLevel);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level19\\level19data.txt");
        try {
            Scanner scanner = new Scanner(file);
            List<Blueprint> blueprintList = new ArrayList<>(30);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                blueprintList.add(
                        new Blueprint(
                                Integer.parseInt(parts[1].substring(0, parts[1].length() - 1)),
                                Integer.parseInt(parts[6]),
                                Integer.parseInt(parts[12]),
                                Integer.parseInt(parts[18]),
                                Integer.parseInt(parts[21]),
                                Integer.parseInt(parts[27]),
                                Integer.parseInt(parts[30])));
            }
            int multiplyGeodes = 1;
            for (int i = 0; i < blueprintList.size(); ++i) {
                System.out.println("Starting on blueprint: " + (i + 1));
                Blueprint blueprint = blueprintList.get(i);
                int maxGeodes = maxGeodesFromBlueprint(blueprint);
                multiplyGeodes *= maxGeodes;
                System.gc();
                Thread.sleep(100);
                if (DEBUG) {
                    System.out.println("Computed a blueprint. New multiplyGeodes: " + multiplyGeodes);
                    System.out.println();
                }
            }
            System.out.println("Geodes multiplied: " + multiplyGeodes);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    private static int maxGeodesFromBlueprint(Blueprint blueprint) throws Exception {
        Queue<Simulation> queue = new ArrayDeque<>(140000000);
        queue.add(new Simulation());
        int maxGeodes = 0;
//        List<String> bestHistory = Collections.emptyList();
        int count = 0;
        while (!queue.isEmpty()) {
            Simulation currentState = queue.poll();
            if (count % 10000000 == 0 && DEBUG) {
                System.out.println("Current iteration count: " + count);
                System.out.println("Current max geodes: " + maxGeodes);
                System.out.println("Current queue size: " + queue.size());
                System.out.println("Current state's time: " + currentState.time);
                System.out.println("Current state representation: " + currentState.getCacheName());
                System.out.println("");
                if (FORCE_GC) {
                    System.gc();
                    Thread.sleep(10);
                }
            }
            count++;

            //Try running out the clock
            Simulation currentStateDoNothing = currentState.copy();
            currentStateDoNothing.simulateUntilEnd(blueprint);

            if (currentStateDoNothing.time == SIMULATION_END_TIME &&
                    maxGeodes < currentStateDoNothing.geodes) {
                    maxGeodes = currentStateDoNothing.geodes;
//                    bestHistory = currentStateDoNothing.history;
            }

            if (currentState.time >= SIMULATION_END_TIME || maxGeodesGettableHeuristic(currentState) < maxGeodes) {
                continue;
            }

            Simulation getNextOreRobot = currentState.copy();
            getNextOreRobot.simulateUntilPurchaseOfOreRobotOrEnd(blueprint);
            if (getNextOreRobot.time == SIMULATION_END_TIME) {
                if (maxGeodes < getNextOreRobot.geodes) {
                    maxGeodes = getNextOreRobot.geodes;
//                    bestHistory = getNextOreRobot.history;
                }
            } else if (getNextOreRobot.time < SIMULATION_END_TIME) {
                queue.add(getNextOreRobot);
            }

            Simulation getNextClayRobot = currentState.copy();
            getNextClayRobot.simulateUntilPurchaseOfClayRobotOrEnd(blueprint);
            if (getNextClayRobot.time == SIMULATION_END_TIME) {
                if (maxGeodes < getNextClayRobot.geodes) {
                    maxGeodes = getNextClayRobot.geodes;
//                    bestHistory = getNextClayRobot.history;
                }
            } else if (getNextClayRobot.time < SIMULATION_END_TIME) {
                queue.add(getNextClayRobot);
            }

            Simulation getNextObsidianRobot = currentState.copy();
            getNextObsidianRobot.simulateUntilPurchaseOfObsidianRobotOrEnd(blueprint);
            if (getNextObsidianRobot.time == SIMULATION_END_TIME) {
                if (maxGeodes < getNextObsidianRobot.geodes) {
                    maxGeodes = getNextObsidianRobot.geodes;
//                    bestHistory = getNextObsidianRobot.history;
                }
            } else if (getNextObsidianRobot.time < SIMULATION_END_TIME) {
                queue.add(getNextObsidianRobot);
            }

            Simulation getNextGeodeRobot = currentState.copy();
            getNextGeodeRobot.simulateUntilPurchaseOfGeodeRobotOrEnd(blueprint);
            if (getNextGeodeRobot.time == SIMULATION_END_TIME) {
                if (maxGeodes < getNextGeodeRobot.geodes) {
                    maxGeodes = getNextGeodeRobot.geodes;
//                    bestHistory = getNextGeodeRobot.history;
                }
            } else if (getNextGeodeRobot.time < SIMULATION_END_TIME) {
                queue.add(getNextGeodeRobot);
            }
        }
//        System.out.println("History of best simulation:");
//        for (String line : bestHistory) {
//            System.out.println(line);
//        }

        return maxGeodes;
    }

    private static int maxGeodesGettableHeuristic(Simulation simulation) {
        int remainingSteps = SIMULATION_END_TIME - simulation.time;
        int sum = simulation.geodes;
        for (int i = 0; i < remainingSteps; ++i) {
            sum += (simulation.geodeRobots + i);
        }
        return sum;
    }


//    private static int maxGeodesFromBlueprintRecursive(Blueprint blueprint) {
//        return maxGeodesFromThisPointForward(blueprint, new Simulation(), new HashMap<>());
//    }
//
//    private static int maxGeodesFromThisPointForward(Blueprint blueprint, Simulation simulation, Map<String, Integer> cache) {
//        if (cache.containsKey(simulation.getCacheName())) {
//            return cache.get(simulation.getCacheName());
//        }
//        if (simulation.time == 24) {
//            return simulation.geodes;
//        }
//
//        int maxGeodes = 0;
//
//        // TODO: deal with multiple purchases in a single minute (use a list)
//        if (simulation.canPurchaseOreRobot(blueprint)) {
//            Simulation currentButBuysOreRobot = simulation.copy();
//            currentButBuysOreRobot.increment();
//            currentButBuysOreRobot.purchaseOreRobot(blueprint);
//            int thisMaxGeodes = maxGeodesFromThisPointForward(blueprint, currentButBuysOreRobot, cache);
//            if (maxGeodes < thisMaxGeodes) {
//                maxGeodes = thisMaxGeodes;
//            }
//        }
//        if (simulation.canPurchaseClayRobot(blueprint)) {
//            Simulation currentButBuysClayRobot = simulation.copy();
//            currentButBuysClayRobot.increment();
//            currentButBuysClayRobot.purchaseClayRobot(blueprint);
//            int thisMaxGeodes = maxGeodesFromThisPointForward(blueprint, currentButBuysClayRobot, cache);
//            if (maxGeodes < thisMaxGeodes) {
//                maxGeodes = thisMaxGeodes;
//            }
//        }
//        if (simulation.canPurchaseObsidianRobot(blueprint)) {
//            Simulation currentButBuysObsidianRobot = simulation.copy();
//            currentButBuysObsidianRobot.increment();
//            currentButBuysObsidianRobot.purchaseObsidianRobot(blueprint);
//            int thisMaxGeodes = maxGeodesFromThisPointForward(blueprint, currentButBuysObsidianRobot, cache);
//            if (maxGeodes < thisMaxGeodes) {
//                maxGeodes = thisMaxGeodes;
//            }
//        }
//        if (simulation.canPurchaseGeodeRobot(blueprint)) {
//            Simulation currentButBuysGeodeRobot = simulation.copy();
//            currentButBuysGeodeRobot.increment();
//            currentButBuysGeodeRobot.purchaseGeodeRobot(blueprint);
//            int buyOreRobotMaxGeodes = maxGeodesFromThisPointForward(blueprint, currentButBuysGeodeRobot, cache);
//            if (maxGeodes < buyOreRobotMaxGeodes) {
//                maxGeodes = buyOreRobotMaxGeodes;
//            }
//        }
//        Simulation currentStateDoNothing = simulation.copy();
//        currentStateDoNothing.increment();
//        int doNothingMaxGeodes = maxGeodesFromThisPointForward(blueprint, currentStateDoNothing, cache);
//        if (maxGeodes < doNothingMaxGeodes) {
//            maxGeodes = doNothingMaxGeodes;
//        }
//        cache.put(simulation.getCacheName(), maxGeodes);
//        return maxGeodes;
//    }

    private static class Blueprint {

        int blueprintId;

        int oreRobotOreCost;
        int clayRobotOreCost;
        int obsidianRobotOreCost;
        int obsidianRobotClayCost;
        int geodeRobotOreCost;
        int geodeRobotObsidianCost;

        Blueprint(int blueprintId,
                int oreRobotOreCost,
                int clayRobotOreCost,
                int obsidianRobotOreCost,
                int obsidianRobotClayCost,
                int geodeRobotOreCost,
                int geodeRobotObsidianCost) {
            this.blueprintId = blueprintId;
            this.oreRobotOreCost = oreRobotOreCost;
            this.clayRobotOreCost = clayRobotOreCost;
            this.obsidianRobotOreCost = obsidianRobotOreCost;
            this.obsidianRobotClayCost = obsidianRobotClayCost;
            this.geodeRobotOreCost = geodeRobotOreCost;
            this.geodeRobotObsidianCost = geodeRobotObsidianCost;
        }

        boolean enoughForAnotherOreRobot(int ore) {
            return ore >= oreRobotOreCost;
        }

        boolean enoughForAnotherClayRobot(int ore) {
            return ore >= clayRobotOreCost;
        }

        boolean enoughForAnotherObsidianRobot(int ore, int clay) {
            return ore >= obsidianRobotOreCost && clay >= obsidianRobotClayCost;
        }

        boolean enoughForAnotherGeodeRobot(int ore, int obsidian) {
            return ore >= geodeRobotOreCost && obsidian >= geodeRobotObsidianCost;
        }
    }

    private static class Simulation {

        int time;

        int ore;
        int clay;
        int obsidian;
        int geodes;

        int oreRobots;
        int clayRobots;
        int obsidianRobots;
        int geodeRobots;

//        List<String> history;

        Simulation() {
            this.time = 0;

            this.ore = 0;
            this.clay = 0;
            this.obsidian = 0;
            this.geodes = 0;

            this.oreRobots = 1;
            this.clayRobots = 0;
            this.obsidianRobots = 0;
            this.geodeRobots = 0;

//            history = new ArrayList<>(SIMULATION_END_TIME);
        }

        void simulateUntilEnd(Blueprint blueprint) {
            while (time < SIMULATION_END_TIME) {
                increment();
            }
        }

        void simulateUntilPurchaseOfOreRobotOrEnd(Blueprint blueprint) {
            while (time < SIMULATION_END_TIME && !canPurchaseOreRobot(blueprint)) {
                increment();
            }
            incrementWithoutHistory();
            purchaseOreRobot(blueprint);
            updateHistory();
        }

        void simulateUntilPurchaseOfClayRobotOrEnd(Blueprint blueprint) {
            while (time < SIMULATION_END_TIME && !canPurchaseClayRobot(blueprint)) {
                increment();
            }
            incrementWithoutHistory();
            purchaseClayRobot(blueprint);
            updateHistory();
        }

        void simulateUntilPurchaseOfObsidianRobotOrEnd(Blueprint blueprint) {
            while (time < SIMULATION_END_TIME && !canPurchaseObsidianRobot(blueprint)) {
                increment();
            }
            incrementWithoutHistory();
            purchaseObsidianRobot(blueprint);
            updateHistory();
        }

        void simulateUntilPurchaseOfGeodeRobotOrEnd(Blueprint blueprint) {
            while (time < SIMULATION_END_TIME && !canPurchaseGeodeRobot(blueprint)) {
                increment();
            }
            incrementWithoutHistory();
            purchaseGeodeRobot(blueprint);
            updateHistory();
        }

        void increment() {
            time++;
            ore += oreRobots;
            clay += clayRobots;
            obsidian += obsidianRobots;
            geodes += geodeRobots;
//            history.add(getCacheName());
        }

        void incrementWithoutHistory() {
            time++;
            ore += oreRobots;
            clay += clayRobots;
            obsidian += obsidianRobots;
            geodes += geodeRobots;
        }

        void updateHistory() {
//            history.add(getCacheName());
        }


        boolean canPurchaseOreRobot(Blueprint blueprint) {
            return blueprint.enoughForAnotherOreRobot(this.ore);
        }

        boolean canPurchaseClayRobot(Blueprint blueprint) {
            return blueprint.enoughForAnotherClayRobot(this.ore);
        }

        boolean canPurchaseObsidianRobot(Blueprint blueprint) {
            return blueprint.enoughForAnotherObsidianRobot(this.ore, this.clay);
        }

        boolean canPurchaseGeodeRobot(Blueprint blueprint) {
            return blueprint.enoughForAnotherGeodeRobot(this.ore, this.obsidian);
        }

        void purchaseOreRobot(Blueprint blueprint) {
            ore -= blueprint.oreRobotOreCost;
            oreRobots++;
        }

        void purchaseClayRobot(Blueprint blueprint) {
            ore -= blueprint.clayRobotOreCost;
            clayRobots++;
        }

        void purchaseObsidianRobot(Blueprint blueprint) {
            ore -= blueprint.obsidianRobotOreCost;
            clay -= blueprint.obsidianRobotClayCost;
            obsidianRobots++;
        }

        void purchaseGeodeRobot(Blueprint blueprint) {
            ore -= blueprint.geodeRobotOreCost;
            obsidian -= blueprint.geodeRobotObsidianCost;
            geodeRobots++;
        }

        String getCacheName() {
            return new StringBuilder()
                    .append(time)
                    .append(":")
                    .append(ore)
                    .append(".")
                    .append(clay)
                    .append(".")
                    .append(obsidian)
                    .append(".")
                    .append(geodes)
                    .append("///")
                    .append(oreRobots)
                    .append(".")
                    .append(clayRobots)
                    .append(".")
                    .append(obsidianRobots)
                    .append(".")
                    .append(geodeRobots)
                    .toString();
        }

        Simulation copy() {
            Simulation newSimulation = new Simulation();
            newSimulation.time = time;
            newSimulation.ore = ore;
            newSimulation.oreRobots = oreRobots;
            newSimulation.clay = clay;
            newSimulation.clayRobots = clayRobots;
            newSimulation.obsidian = obsidian;
            newSimulation.obsidianRobots = obsidianRobots;
            newSimulation.geodes = geodes;
            newSimulation.geodeRobots = geodeRobots;
//            newSimulation.history = new ArrayList<>(history);
            return newSimulation;
        }
    }
}
