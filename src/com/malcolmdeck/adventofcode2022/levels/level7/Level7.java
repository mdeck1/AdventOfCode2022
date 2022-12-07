package com.malcolmdeck.adventofcode2022.levels.level7;

import com.malcolmdeck.adventofcode2022.util.FileHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Level7 {

    public static void partOne() throws Exception {
        File file = FileHelper.getFile("level7\\level7data.txt");
        try {
            Scanner scanner = new Scanner(file);
            Directory slash = new Directory("/");
            // Read the input file, construct the directory tree under slash
            parseTree(scanner, slash);
            // Use the directory tree to compute the value we want by having the tree itself do the
            // recursion for us.
            System.out.println("Sum of small directory sizes: " + slash.returnSizeIfDirectoryAndSmall());
        } catch (Exception e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    public static void partTwo() throws Exception {
        File file = FileHelper.getFile("level7\\level7data.txt");
        try {
            Scanner scanner = new Scanner(file);
            Directory slash = new Directory("/");
            // Read the input file, construct the directory tree under slash
            parseTree(scanner, slash);
            // Get and sort a list of all directories
            int totalSize = slash.getSize();
            ArrayList<Directory> directoryList = new ArrayList<>();
            traverseTree(directoryList, slash);
            directoryList.sort(new Comparator<Directory>() {
                @Override
                public int compare(Directory o1, Directory o2) {
                    return o1.getSize() - o2.getSize();
                }
            });
            // Find the target size, and find the smallest directory >= that target size to delete, print that.
            int targetSize = totalSize - 40000000; // Need directory tree to take up at most 40000000, delete excess
            for (int i = 0; i < directoryList.size(); ++i) {
                if (directoryList.get(i).getSize() > targetSize) {
                    System.out.println("Ideal directory to delete has size: " + directoryList.get(i).getSize());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("FileNotFound: " + e.getMessage());
        }
        return;
    }

    private static void parseTree(Scanner scanner, Directory slash) {
        Directory currDirectory = slash;
        String line = scanner.nextLine();
        // skip the first line; it causes bugs :(
        line = scanner.nextLine();
        // Read until the file is empty
        while (line != null) {
            String[] command = line.split(" ");
            // Read cd commands
            if (command[1].equals("cd")) {
                // Move up a directory
                if (command[2].equals("..")) {
                    currDirectory = currDirectory.getParent();
                } else { // Move down a directory, creating if needed
                    Directory newDirectory = currDirectory.getChildDir(command[2]);
                    if (newDirectory == null) {
                        newDirectory = new Directory(command[2]);
                        currDirectory.addNode(newDirectory);
                        newDirectory.setParent(currDirectory);
                    }
                    currDirectory = newDirectory;
                }
                // Get next line safely
                if (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                } else {
                    line = null;
                }
            } else if (command[1].equals("ls")) { // Parse all nodes in current directory
                line = scanner.nextLine();
                while (line != null && line.charAt(0) != '$') {
                    String[] nodeDescription = line.split(" ");
                    // Make a new subdir (if necessary)
                    if (nodeDescription[0].equals("dir")) {
                        Directory newDirectory = currDirectory.getChildDir(command[1]);
                        if (newDirectory == null) {
                            newDirectory = new Directory(nodeDescription[1]);
                            currDirectory.addNode(newDirectory);
                            newDirectory.setParent(currDirectory);
                        }
                    } else {
                        // Make a new raw file
                        FileClass fileClass = new FileClass(nodeDescription[1]);
                        fileClass.setSize(Integer.parseInt(nodeDescription[0]));
                        fileClass.setParent(currDirectory);
                        currDirectory.addNode(fileClass);
                    }
                    // Grab next line safely
                    if (scanner.hasNextLine()) {
                        line = scanner.nextLine();
                    } else {
                        line = null;
                    }
                }
            }
        }
    }

    /**
     * Adds all directories including and below the passed directory to the passed list.
     */
    private static void traverseTree(List<Directory> directoryList, Directory currDirectory) {
        directoryList.add(currDirectory);
        for (Node child : currDirectory.children) {
            if (child instanceof Directory) {
                traverseTree(directoryList, (Directory) child);
            }
        }
        return;
    }

    /**
     * Base class for both Files and Directories
     */
    private static abstract class Node {
        Directory parent;
        ArrayList<Node> children;
        String name;
        Integer size = null;

        Node(String name) {
            this.name = name;
            this.children = new ArrayList<>();
        }

        void setParent(Directory parent) {
            this.parent = parent;
        }

        Directory getParent() {
            return (Directory) parent;
        }

        abstract int getSize();
    }

    /**
     * Class to represent Directories in our tree
     */
    private static class Directory extends Node {
        Directory(String name) {
            super(name);
        }

        void addNode(Node node) {
            this.children.add(node);
        }

        Directory getChildDir(String name) {
            for (Node child : children) {
                if (child instanceof Directory && child.name.equals(name)) {
                    return (Directory) child;
                }
            }
            return null;
        }

        @Override
        int getSize() {
            if (size == null) {
                computeSize();
            }
            return size;
        }

        void computeSize() {
            int sum = 0;
            for (Node node : children) {
                sum += node.getSize();
            }
            size = sum;
        }

        // Used to recursively grab the sum of all directories of size at most 100000
        int returnSizeIfDirectoryAndSmall() {
            int sum = 0;
            if (getSize() < 100000) {
                sum += this.size;
            }
            for (Node node : children) {
                if (node instanceof Directory) {
                    sum += ((Directory) node).returnSizeIfDirectoryAndSmall();
                }
            }
            return sum;
        }
    }

    /**
     * Class to represent Files in our tree.
     *
     * FileClass used as name since File conflicts with other classname used in this file.
     */
    private static class FileClass extends Node {
        FileClass(String name) {
            super(name);
        }

        void setSize(int size) {
            this.size = size;
        }

        @Override
        int getSize() {
            return size;
        }
    }

}
