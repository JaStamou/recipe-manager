package org.example;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainApplication {
    //ΣΤΑΘΕΡΑ ΓΙΑ RELATIVE ΟΡΙΣΜΟ TOY FILEPATH ΚΑΤΑ ΤΗΝ ΕΚΤΕΛΕΣΗ
    public static final String RECIPES_PATH = "src/main/resources/Recipes/";

    public static void main(String[] args) {
        //ΕΛΕΓΧΟΣ ΓΙΑ ΠΑΡΟΥΣΙΑ ΟΡΙΣΜΑΤΩΝ
        if (args.length == 0) {
            //ΣΕ ΠΕΡΙΠΤΩΣΗ ΕΛΛΕΙΨΗΣ, ΠΑΡΟΥΣΙΑΖΕΤΑΙ ΣΦΑΛΜΑ
            System.err.println("Παρακαλώ εισάγετε ένα αρχείο συνταγής ή την επιλογή -list με αρχεία.");
            return;
        }

        try {
            //ΕΛΕΓΧΟΣ ΓΙΑ ΤΗΝ ΠΑΡΟΥΣΙΑ ΤΟΥ ΟΡΙΣΜΑΤΟΣ LIST
            if (args[0].equals("-list")) {
               //ΑΝ ΤΟ ΟΡΙΣΜΑ ΕΙΝΑΙ ΤΟ -LIST ΚΑΛΟΥΜΕ ΤΗΝ ΜΕΘΟΔΟ ΠΟΥ ΕΚΤΥΠΩΝΕΙ ΤΗΝ ΛΙΣΤΑ ΑΓΟΡΩΝ
                printShoppingList(args);
            } else {
                //ΔΙΑΦΟΡΕΤΙΚΑ ΚΑΛΟΥΜΕ ΤΗΝ ΜΕΘΟΔΟ ΠΟΥ ΕΚΤΥΠΩΝΕΙ ΤΗΝ ΣΥΝΤΑΓΗ
                printRecipe(args[0]);
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την εκτέλεση: " + e.getMessage());
        }
    }

    //ΜΕΘΟΔΟΣ ΠΟΥ ΕΚΥΠΩΝΕΙ ΤΑ ΖΗΤΟΥΜΕΝΑ ΤΗΣ ΛΕΙΤΟΥΡΓΙΑΣ ΣΥΝΤΑΓΗΣ
    private static void printRecipe(String fileName) throws IOException {

        //ΦΟΡΤΩΝΕΙ ΤΙΣ ΓΡΑΜΜΕΣ ΑΠΟ ΤΟ ΑΡΧΕΙΟ .COOK
        List<String> lines = loadRecipeFile(fileName);

        //ΕΞΑΓΕΙ ΤΑ ΔΕΔΟΜΕΝΑ
        Extractor.ExtractedData data = Extractor.extractData(lines);

        //ΕΚΤΥΠΩΝΕΙ ΤΑ ΥΛΙΚΑ
        System.out.println("Υλικά:");
        data.getIngredients().forEach(System.out::println);

        //ΕΚΤΥΠΩΝΕΙ ΤΑ ΣΚΕΥΗ
        System.out.println("\nΣκεύη:");
        data.getUtensils().forEach(System.out::println);

        //ΕΚΤΥΠΩΝΕΙ ΤΑ ΒΗΜΑΤΑ
        System.out.println("\nΒήματα:");
        int stepNumber = 1;
        for (Step step : data.getSteps()) {
            System.out.println(stepNumber++ + ". " + step);
        }

        //ΥΠΟΛΟΓΙΖΕΙ ΚΑΙ ΕΚΤΥΠΩΝΕΙ ΤΗ ΣΥΝΟΛΙΚΗ ΔΙΑΡΚΕΙΑ ΤΗΣ ΣΥΝΤΑΓΗΣ
        System.out.println("\nΣυνολική διάρκεια: " +
                data.getSteps().stream().mapToInt(Step::getTime).sum() + " λεπτά");
    }

    //ΜΕΘΟΔΟΣ ΠΟΥ ΕΚΤΥΠΩΝΕΙ ΤΗ ΛΙΣΤΑ ΑΓΟΡΩΝ
    private static void printShoppingList(String[] args) throws IOException {
        ShoppingList shoppingList = new ShoppingList();

        //ΠΡΟΣΘΗΚΗ ΤΩΝ ΥΛΙΚΩΝ ΑΠΟ ΚΑΘΕ ΑΡΧΕΙΟ .COOK
        for (int i = 1; i < args.length; i++) {
            shoppingList.addIngredientsFromRecipe(RECIPES_PATH + args[i]);
        }
        //ΕΚΤΥΠΩΣΗ ΤΗΣ ΛΙΣΤΑΣ
        System.out.println("Λίστα αγορών:\n");
        shoppingList.printShoppingList();
    }


    //ΦΟΡΤΩΝΕΙ ΤΟ ΠΕΡΙΕΧΟΜΕΝΟ ΕΝΟΣ ΑΡΧΕΙΟΥ ΣΥΝΤΑΓΗΣ ΚΑΙ ΕΠΙΣΤΡΕΦΕΙ ΛΙΣΤΑ ΜΕ ΤΙΣ ΓΡΑΜΜΕΣ
    private static List<String> loadRecipeFile(String fileName) throws IOException {

        //ΔΗΜΙΟΥΡΓΕΙ OBJ ΜΕ ΤΟ ABSOLUTE PATH ΤΟΥ ΑΡΧΕΙΟΥ
        File file = new File(RECIPES_PATH + fileName);
        if (!file.exists()) {
            throw new IOException("Το αρχείο " + fileName + " δεν βρέθηκε στον φάκελο Recipes.");
        }
        return FileReaderUtility.readFile(file.getAbsolutePath());
    }
}
