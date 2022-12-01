package com.malcolmdeck.adventofcode2022.util;

import java.io.File;
import java.io.FileNotFoundException;

public class FileHelper {

    public static File getFile(String fileName) throws FileNotFoundException {
        // Desktop
        return new File("C:\\Users\\malco\\IdeaProjects\\AdventOfCode2022\\src\\com\\malcolmdeck\\adventofcode2021\\levels\\" + fileName);
        // Laptop
//        return new File("C:\\Users\\Malcolm_razer\\IdeaProjects\\AdventOfCode2021\\src\\com\\malcolmdeck\\adventofcode2021\\levels\\" + fileName);
    }

}
