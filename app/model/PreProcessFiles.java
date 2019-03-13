package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PreProcessFiles {

    String filePath;

    public PreProcessFiles(String filePath) {
        this.filePath = filePath;
    }


    public void readFileAndExtractSentences() {
        File file = new File(filePath);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Given file path is incorrect " +  e);
        }

        while (sc.hasNextLine()) {
            // TODO : process stuff here.
            System.out.println(sc.nextLine());
            System.out.println("----");
        }
    }

}


