package com.malcolmdeck.adventofcode2022.levels.Level11;

import java.math.BigInteger;
import java.util.*;

public class Level11 {

    private static final boolean DEBUG = true;

    public static void partOne() throws Exception {
        try {
            List<Monkey> monkeyList = listFromRealData(true);

            // Do 20 rounds
            for (int i = 0; i < 20; ++i) {
                if (DEBUG) {
                    System.out.println("About to do iteration: " + i);
                    printAllMonkeys(monkeyList);
                }
                for (int j = 0; j < monkeyList.size(); ++j) {
                    monkeyList.get(j).processEachItem();
                }
            }
            if (DEBUG) {
                printAllMonkeys(monkeyList);
            }
            // Find top 2 scores to compute final score
            long[] inspectionCounts = new long[monkeyList.size()];
            for (int i = 0; i < inspectionCounts.length; ++i) {
                inspectionCounts[i] = monkeyList.get(i).inspectionCount;
            }
            Arrays.sort(inspectionCounts);

            System.out.println("Monkey Business score: " +
                    (inspectionCounts[inspectionCounts.length - 2] *
                            inspectionCounts[inspectionCounts.length - 1]));
        } catch (Exception e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    public static void partTwo() throws Exception {
        try {
            List<Monkey> monkeyList = listFromRealData(false);

            // Do 10000 rounds
            for (int i = 0; i < 10000; ++i) {
                if (DEBUG && i % 10000 == 0) {
                    System.out.println("About to do iteration: " + i);
                    printAllMonkeys(monkeyList);
                }
                for (int j = 0; j < monkeyList.size(); ++j) {
                    monkeyList.get(j).processEachItem();
                }
            }
            if (DEBUG) {
                printAllMonkeys(monkeyList);
            }

            // Find busiest 2 monkeys to compute final score
            long[] inspectionCounts = new long[monkeyList.size()];
            for (int i = 0; i < inspectionCounts.length; ++i) {
                inspectionCounts[i] = monkeyList.get(i).inspectionCount;
            }
            Arrays.sort(inspectionCounts);
            System.out.println("Monkey Business score: " +
                    (inspectionCounts[inspectionCounts.length - 2] *
                            inspectionCounts[inspectionCounts.length - 1]));
        } catch (Exception e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    private static void printAllMonkeys(List<Monkey> monkeyList) {
        for (int i = 0; i < monkeyList.size(); ++i) {
            System.out.println("Monkey " + i + ": " + monkeyList.get(i).toString());
        }
    }

    private static List<Monkey> listFromRealData(
            boolean worryLevelDecreases) {
        long modulus = 9699690l;
        List<Monkey> monkeyList = new ArrayList<>(8);
        Monkey monkey0 = new Monkey(new Operation() {
            @Override
            public BigInteger doOperation(BigInteger old) {
                return old.multiply(BigInteger.valueOf(5));
            }
        }, new Condition() {
            @Override
            public boolean passesTest(BigInteger value) {
                return value.mod(BigInteger.valueOf(11)).equals(BigInteger.ZERO);
            }
        },
                worryLevelDecreases,
                modulus);
        Monkey monkey1 = new Monkey(new Operation() {
            @Override
            public BigInteger doOperation(BigInteger old) {
                return old.multiply(old);
            }
        }, new Condition() {
            @Override
            public boolean passesTest(BigInteger value) {
                return value.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO);
            }
        },
                worryLevelDecreases,
                modulus);
        Monkey monkey2 = new Monkey(new Operation() {
            @Override
            public BigInteger doOperation(BigInteger old) {
                return old.multiply(BigInteger.valueOf(7));
            }
        }, new Condition() {
            @Override
            public boolean passesTest(BigInteger value) {
                return value.mod(BigInteger.valueOf(5)).equals(BigInteger.ZERO);
            }
        },
                worryLevelDecreases,
                modulus);
        Monkey monkey3 = new Monkey(new Operation() {
            @Override
            public BigInteger doOperation(BigInteger old) {
                return old.add(BigInteger.valueOf(1));
            }
        }, new Condition() {
            @Override
            public boolean passesTest(BigInteger value) {
                return value.mod(BigInteger.valueOf(17)).equals(BigInteger.ZERO);
            }
        },
                worryLevelDecreases,
                modulus);
        Monkey monkey4 = new Monkey(new Operation() {
            @Override
            public BigInteger doOperation(BigInteger old) {
                return old.add(BigInteger.valueOf(3));
            }
        }, new Condition() {
            @Override
            public boolean passesTest(BigInteger value) {
                return value.mod(BigInteger.valueOf(19)).equals(BigInteger.ZERO);
            }
        },
                worryLevelDecreases,
                modulus);
        Monkey monkey5 = new Monkey(new Operation() {
            @Override
            public BigInteger doOperation(BigInteger old) {
                return old.add(BigInteger.valueOf(5));
            }
        }, new Condition() {
            @Override
            public boolean passesTest(BigInteger value) {
                return value.mod(BigInteger.valueOf(7)).equals(BigInteger.ZERO);
            }
        },
                worryLevelDecreases,
                modulus);
        Monkey monkey6 = new Monkey(new Operation() {
            @Override
            public BigInteger doOperation(BigInteger old) {
                return old.add(BigInteger.valueOf(8));
            }
        }, new Condition() {
            @Override
            public boolean passesTest(BigInteger value) {
                return value.mod(BigInteger.valueOf(3)).equals(BigInteger.ZERO);
            }
        },
                worryLevelDecreases,
                modulus);
        Monkey monkey7 = new Monkey(new Operation() {
            @Override
            public BigInteger doOperation(BigInteger old) {
                return old.add(BigInteger.valueOf(2));
            }
        }, new Condition() {
            @Override
            public boolean passesTest(BigInteger value) {
                return value.mod(BigInteger.valueOf(13)).equals(BigInteger.ZERO);
            }
        },
                worryLevelDecreases,
                modulus);
        monkeyList.add(monkey0);
        monkeyList.add(monkey1);
        monkeyList.add(monkey2);
        monkeyList.add(monkey3);
        monkeyList.add(monkey4);
        monkeyList.add(monkey5);
        monkeyList.add(monkey6);
        monkeyList.add(monkey7);
        monkey0.giveItem(92l);
        monkey0.giveItem(73l);
        monkey0.giveItem(86l);
        monkey0.giveItem(83l);
        monkey0.giveItem(65l);
        monkey0.giveItem(51l);
        monkey0.giveItem(55l);
        monkey0.giveItem(93l);
        monkey1.giveItem(99l);
        monkey1.giveItem(67l);
        monkey1.giveItem(62l);
        monkey1.giveItem(61l);
        monkey1.giveItem(59l);
        monkey1.giveItem(98l);
        monkey2.giveItem(81l);
        monkey2.giveItem(89l);
        monkey2.giveItem(56l);
        monkey2.giveItem(61);
        monkey2.giveItem(99);
        monkey3.giveItem(97);
        monkey3.giveItem(74);
        monkey3.giveItem(68);
        monkey4.giveItem(78);
        monkey4.giveItem(73);
        monkey5.giveItem(50);
        monkey6.giveItem(95);
        monkey6.giveItem(88);
        monkey6.giveItem(53);
        monkey6.giveItem(75);
        monkey7.giveItem(50);
        monkey7.giveItem(77);
        monkey7.giveItem(98);
        monkey7.giveItem(85);
        monkey7.giveItem(94);
        monkey7.giveItem(56);
        monkey7.giveItem(89);
        monkey0.setTrueMonkey(monkey3);
        monkey0.setFalseMonkey(monkey4);
        monkey1.setTrueMonkey(monkey6);
        monkey1.setFalseMonkey(monkey7);
        monkey2.setTrueMonkey(monkey1);
        monkey2.setFalseMonkey(monkey5);
        monkey3.setTrueMonkey(monkey2);
        monkey3.setFalseMonkey(monkey5);
        monkey4.setTrueMonkey(monkey2);
        monkey4.setFalseMonkey(monkey3);
        monkey5.setTrueMonkey(monkey1);
        monkey5.setFalseMonkey(monkey6);
        monkey6.setTrueMonkey(monkey0);
        monkey6.setFalseMonkey(monkey7);
        monkey7.setTrueMonkey(monkey4);
        monkey7.setFalseMonkey(monkey0);
        return monkeyList;
    }

    private static List<Monkey> listFromTestData(boolean worryLevelDecreases) {
        long modulus = 96577l;
        List<Monkey> monkeyList = new ArrayList<>(4);
        Monkey monkey0 = new Monkey(new Operation() {
            @Override
            public BigInteger doOperation(BigInteger old) {
                return old.multiply(BigInteger.valueOf(19));
            }
        }, new Condition() {
            @Override
            public boolean passesTest(BigInteger value) {
                return value.mod(BigInteger.valueOf(23)).equals(BigInteger.ZERO);
            }
        },
                worryLevelDecreases,
                modulus);
        Monkey monkey1 = new Monkey(new Operation() {
            @Override
            public BigInteger doOperation(BigInteger old) {
                return old.add(BigInteger.valueOf(6));
            }
        }, new Condition() {
            @Override
            public boolean passesTest(BigInteger value) {
                return value.mod(BigInteger.valueOf(19)).equals(BigInteger.ZERO);
            }
        },
                worryLevelDecreases,
                modulus);
        Monkey monkey2 = new Monkey(new Operation() {
            @Override
            public BigInteger doOperation(BigInteger old) {
                return old.multiply(old);
            }
        }, new Condition() {
            @Override
            public boolean passesTest(BigInteger value) {
                return value.mod(BigInteger.valueOf(13)).equals(BigInteger.ZERO);
            }
        },
                worryLevelDecreases,
                modulus);
        Monkey monkey3 = new Monkey(new Operation() {
            @Override
            public BigInteger doOperation(BigInteger old) {
                return old.add(BigInteger.valueOf(3));
            }
        }, new Condition() {
            @Override
            public boolean passesTest(BigInteger value) {
                return value.mod(BigInteger.valueOf(17)).equals(BigInteger.ZERO);
            }
        },
                worryLevelDecreases,
                modulus);
        
        monkeyList.add(monkey0);
        monkeyList.add(monkey1);
        monkeyList.add(monkey2);
        monkeyList.add(monkey3);
        monkey0.giveItem(79);
        monkey0.giveItem(98);
        monkey1.giveItem(54);
        monkey1.giveItem(65);
        monkey1.giveItem(75);
        monkey1.giveItem(74);
        monkey2.giveItem(79);
        monkey2.giveItem(60);
        monkey2.giveItem(97);
        monkey3.giveItem(74);
        monkey0.setTrueMonkey(monkey2);
        monkey0.setFalseMonkey(monkey3);
        monkey1.setTrueMonkey(monkey2);
        monkey1.setFalseMonkey(monkey0);
        monkey2.setTrueMonkey(monkey1);
        monkey2.setFalseMonkey(monkey3);
        monkey3.setTrueMonkey(monkey0);
        monkey3.setFalseMonkey(monkey1);
        return monkeyList;
    }

    private static class Monkey {
        Deque<BigInteger> items;
        Operation operation;
        Condition condition;
        Monkey trueMonkey;
        Monkey falseMonkey;
        boolean worryLevelDecreases;
        // We cna modulo all numbers by this value since all we're doing is
        // multiplying and adding, which are safe to do.
        // Compute this by multiplying all monkeys' condition values together.
        BigInteger modulus;
        long inspectionCount;

        Monkey(Operation operation,
               Condition condition,
               boolean worryLevelDecreases,
               long modulus) {
            this.operation = operation;
            this.condition = condition;
            items = new ArrayDeque<>();
            inspectionCount = 0;
            this.worryLevelDecreases = worryLevelDecreases;
            this.modulus = BigInteger.valueOf(modulus);
        }

        void setTrueMonkey(Monkey monkey) {
            this.trueMonkey = monkey;
        }

        void setFalseMonkey(Monkey monkey) {
            this.falseMonkey = monkey;
        }

        void giveItem(long item) {
            items.addLast(BigInteger.valueOf(item));
        }

        void giveItem(BigInteger item) {
            items.addLast(item);
        }

        void processEachItem() {
            for (BigInteger item : items) {
                item = operation.doOperation(item);
                if (worryLevelDecreases) {
                    item = item.divide(BigInteger.valueOf(3));
                }
                item = item.mod(modulus);
                if (condition.passesTest(item)) {
                    trueMonkey.giveItem(item);
                } else {
                    falseMonkey.giveItem(item);
                }
                inspectionCount++;
            }
            items.clear();
        }

        @Override
        public String toString() {
            return "Items: {" + this.items.toString() + "} \n" +
                    "InspectionCount: " + this.inspectionCount;
        }
    }

    private interface Operation {
        BigInteger doOperation(BigInteger old);
    }

    private interface Condition {
        boolean passesTest(BigInteger value);
    }

}
