package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReaderUtility {

    //ΜΕΘΟΔΟΣ ΠΟΥ ΔΙΑΒΑΖΕΙ ΕΝΑ ΑΡΧΕΙΟ ΑΠΟ ΤΗΝ ΔΙΑΔΡΟΜΗ filePath ΚΑΙ ΕΠΙΣΤΡΕΦΕΙ ΤΙΣ ΓΡΑΜΜΕΣ ΣΕ ΛΙΣΤΑ
    public static List<String> readFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;  // Επιστρέφουμε τη λίστα με τις γραμμές του αρχείου
    }
}
