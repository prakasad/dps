import Exceptions.InputDataErrException;
import helper.Couchbase;
import javassist.tools.rmi.ObjectNotFoundException;
import model.PreProcessFiles;
import play.Logger;
import services.SearchService;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.Set;

public class ConsoleRunner {

    public static void run() {

        // Initialize couchbase

        final Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Select Search Query Option");
            System.out.println("1. W1 => W2 \n2.W1 => (R)W2 \n3.* => (R)W1 \n4.W1<=W2=>W3 \n 5.Exit.");
            final Integer input = Integer.parseInt(scanner.nextLine());
            String w1 = null;
            String w2 = null;
            String w3 = null;
            String rel = null;
            SearchService searchService = SearchService.getInstance();

            if (input == 5 ) {
                System.out.print("Exiting the code.");
                break;
            }

            try {
            switch (input) {
                case 1:
                    System.out.print("Enter word W1: ");
                    w1 = scanner.nextLine();
                    System.out.print("\n Enter word W2: ");
                    w2 = scanner.nextLine();
                    System.out.println(String.format("Performing search for %s => %s", w1, w2));
                    printSentences(searchService.W2childW1Search(w1, w2));
                    break;
                case 2:
                    System.out.print("Enter word W1: ");
                    w1 = scanner.nextLine();
                    System.out.print("Enter Reln: ");
                    rel = scanner.nextLine();
                    System.out.print("\n Enter word W2: ");
                    w2 = scanner.nextLine();
                    System.out.println(String.format("Performing search for %s => (%s)%s", w1, rel, w2));
                    printSentences(searchService.W2relationW1Search(w1, w2, rel));
                    break;
                case 3:
                    System.out.print("Enter word W1: ");
                    w1 = scanner.nextLine();
                    System.out.print("Enter Reln: ");
                    rel = scanner.nextLine();
                    System.out.println(String.format("Performing search for * => (%s)%s", rel, w1));
                    printSentences(searchService.W1relationAnyParent(w1, rel));
                    break;
                case 4:
                    System.out.print("Enter word W1: ");
                    w1 = scanner.nextLine();
                    System.out.print("Enter word W2: ");
                    w2 = scanner.nextLine();
                    System.out.print("\n Enter word W3: ");
                    w3 = scanner.nextLine();
                    System.out.println(String.format("Performing search for %s <= %s => %s", w1, w2, w3));
                    printSentences(searchService.W1andW3childrenW2Search(w1, w2, w3));
                    break;
                default :
                    System.out.println("Please select correct input");
                    break;
            }
            } catch (ObjectNotFoundException | InputDataErrException e) {
                Logger.error(String.format("" + e.getStackTrace()));
                throw new RuntimeException("Error with search process.");
            }
        }
    }

    public static void preProcessFile() {
        PreProcessFiles preProcessFiles = new PreProcessFiles("sentences.txt");
        //Couchbase couchbase = new Couchbase();
        try {
            preProcessFiles.readFileAndExtractSentences();
        } catch (NoSuchAlgorithmException | InputDataErrException | ObjectNotFoundException | IOException e) {
            Logger.error(String.format("%s", e.getStackTrace()));
        }
    }

    private static void  printSentences (Set<String> sentences) {
        if (sentences.size() > 0) {
            System.out.println("Seach Results for the given query is :");
        } else {
            System.out.println("No seach results found for the given query.");
        }
        for (String s : sentences) {
            System.out.println(s);
        }

    }

    public static void main(String[] args) {
        Couchbase couchbase = new Couchbase();

        // After initial run this flag can be set to false , as the database is pre populated.
        Boolean preLoad = true;
        if (args.length > 0) {
            preLoad = Boolean.getBoolean(args[0]);
        } else {
            System.out.println("This assumes that text files are pre processed and present in couchbase.");
        }

        if (preLoad) {
            preProcessFile();
        }

        run();
    }


}