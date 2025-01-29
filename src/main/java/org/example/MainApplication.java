package org.example;

import gr.hua.dit.oop2.countdown.Countdown;
import gr.hua.dit.oop2.countdown.CountdownFactory;
import gr.hua.dit.oop2.countdown.Notifier;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class MainApplication {

    public static void main(String[] args) {
        //ΕΝΑΡΞΗ ΤΟΥ SWING THREAD ΓΙΑ ΤΗΝ ΕΚΤΕΛΕΣΗ ΤΗΣ ΓΡΑΦΙΚΗΣ ΔΙΕΠΑΦΗΣ
        SwingUtilities.invokeLater(() -> {

            //ΔΗΜΙΟΥΡΓΙΑ ΚΥΡΙΟΥ ΠΑΡΑΘΥΡΟΥ
            JFrame frame = new JFrame("Recipe Manager");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);

            //ΔΗΜΙΟΥΡΓΙΑ MAIN PANEL
            JPanel mainPanel = new JPanel(new BorderLayout());
            frame.add(mainPanel);

            //ΔΗΜΙΟΥΡΓΙΑ PANEL ΜΕ ΚΟΥΜΠΙΑ
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            mainPanel.add(buttonPanel, BorderLayout.NORTH);

            //ΔΗΜΙΟΥΡΓΙΑ LIST PANE
            DefaultListModel<File> recipeListModel = new DefaultListModel<>();
            JList<File> recipeList = new JList<>(recipeListModel);
            //ΔΥΝΑΤΟΤΗΤΑ ΠΟΛΛΑΠΛΗΣ ΕΠΙΛΟΓΗς
            recipeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            JScrollPane recipeScrollPane = new JScrollPane(recipeList);
            recipeScrollPane.setPreferredSize(new Dimension(250, 0));
            mainPanel.add(recipeScrollPane, BorderLayout.WEST);

            //ΠΕΔΙΟ ΤΕΧΤ OUTPUT
            JTextArea resultArea = new JTextArea();
            resultArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(resultArea);
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            //ΔΗΜΙΟΥΡΓΙΑ ΚΟΥΜΠΙΩΝ
            JButton loadButton = new JButton("Φόρτωση Συνταγών");
            JButton viewButton = new JButton("Εμφάνιση Συνταγής");
            JButton shoppingListButton = new JButton("Λίστα Αγορών");
            JButton executeButton = new JButton("Εκτέλεση Συνταγής");
            viewButton.setEnabled(false);
            shoppingListButton.setEnabled(false);
            executeButton.setEnabled(false);

            //ΠΡΟΣΘΗΚΗ ΚΟΥΜΠΙΩΝ ΣΤΟ BUTTON PANEL
            buttonPanel.add(loadButton);
            buttonPanel.add(viewButton);
            buttonPanel.add(shoppingListButton);
            buttonPanel.add(executeButton);

            //ΛΕΙΤΟΥΡΓΙΑ ΦΟΡΤΩΣΗΣ ΑΡΧΕΙΩΝ ΑΠΟ ΤΟΠΙΚΟ DIR
            loadButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                fileChooser.setFileFilter(new FileNameExtensionFilter("Recipe Files (.cook)", "cook"));
                if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    for (File file : fileChooser.getSelectedFiles()) {
                        recipeListModel.addElement(file); // ΠΡΟΣΘΗΚΗ ΑΡΧΕΙΩΝ ΣΤΗ ΛΙΣΤΑ
                    }
                }
            });

            //ΕΝΕΡΓΟΠΟΙΗΣΗ ΤΩΝ ΚΟΥΜΠΙΩΝ ΜΟΝΟ ΟΤΑΝ ΕΠΙΛΕΓΕΤΑΙ ΣΥΝΤΑΓΗ
            recipeList.addListSelectionListener(e -> {
                boolean isSelected = !recipeList.isSelectionEmpty();
                viewButton.setEnabled(isSelected);
                shoppingListButton.setEnabled(isSelected);
                executeButton.setEnabled(isSelected);
            });

            //ΛΕΙΤΟΥΡΓΙΑ ΠΡΟΒΟΛΗΣ ΣΥΝΤΑΓΗΣ
            viewButton.addActionListener(e -> {
                File selectedFile = recipeList.getSelectedValue();
                if (selectedFile != null) {
                    try {
                        List<String> lines = Files.readAllLines(selectedFile.toPath());
                        Extractor.ExtractedData data = Extractor.extractData(lines);

                        //ΠΡΟΒΟΛΗ ΔΕΔΟΜΕΝΩΝ ΣΥΝΤΑΓΗΣ
                        resultArea.setText("Συνταγή: " + selectedFile.getName() + "\n\n");
                        resultArea.append("Υλικά:\n");
                        data.getIngredients().forEach(ingredient -> resultArea.append("- " + ingredient + "\n"));
                        resultArea.append("\nΣκεύη:\n");
                        data.getUtensils().forEach(utensil -> resultArea.append("- " + utensil + "\n"));
                        resultArea.append("\nΒήματα:\n");
                        for (int i = 0; i < data.getSteps().size(); i++) {
                            resultArea.append((i + 1) + ". " + data.getSteps().get(i) + "\n\n");
                        }
                    } catch (IOException ex) {
                        resultArea.setText("Σφάλμα κατά την ανάγνωση της συνταγής.");
                    }
                }
            });

            //ΛΕΙΤΟΥΡΓΙΑ ΕΚΤΥΠΩΣΗΣ ΛΙΣΤΑΣ ΑΓΟΡΩΝ
            shoppingListButton.addActionListener(e -> {
                int[] selectedIndices = recipeList.getSelectedIndices();
                if (selectedIndices.length > 0) {
                    try {
                        ShoppingList shoppingList = new ShoppingList();
                        for (int index : selectedIndices) {
                            File file = recipeListModel.get(index);
                            shoppingList.addIngredientsFromRecipe(file.getAbsolutePath());
                        }

                        //ΕΜΦΑΝΙΣΗ SHOPPING LIST
                        resultArea.setText("Λίστα Αγορών:\n\n");
                        shoppingList.getIngredients().forEach(ingredient -> resultArea.append("- " + ingredient + "\n"));
                    } catch (IOException ex) {
                        resultArea.setText("Σφάλμα κατά τη δημιουργία λίστας αγορών.");
                    }
                }
            });

            //ΛΕΙΤΟΥΡΓΙΑ ΕΚΤΕΛΕΣΗΣ ΣΥΝΤΑΓΗΣ
            executeButton.addActionListener(e -> {
                File selectedFile = recipeList.getSelectedValue();
                if (selectedFile != null) {
                    try {
                        List<String> lines = Files.readAllLines(selectedFile.toPath());
                        Extractor.ExtractedData data = Extractor.extractData(lines);

                        //ΕΚΤΕΛΕΣΗ ΣΥΝΤΑΓΗΣ ΣΕ ΞΕΧΩΡΙΣΤΟ THREAD
                        new Thread(() -> {
                            SwingUtilities.invokeLater(() -> resultArea.setText("Ξεκινάμε τη συνταγή...\n\n"));

                            int stepCounter = 1;
                            for (Step step : data.getSteps()) {
                                int currentStep = stepCounter;
                                SwingUtilities.invokeLater(() -> resultArea.append("\nΒήμα " + currentStep + ": " + step + "\n"));

                                //ΔΙΑΧΕΙΡΙΣΗ ΜΗ ΧΡΟΝΟΜΕΤΡΗΜΕΝΩΝ ΒΗΜΑΤΩΝ
                                if (step.getTime() == 0) try {
                                    Thread.sleep(500);
                                } catch (InterruptedException ex) {
                                    throw new RuntimeException(ex);
                                }

                                stepCounter++;

                                //ΔΙΑΧΕΙΡΙΣΗ ΧΡΟΝΟΜΕΤΡΗΜΕΝΩΝ ΒΗΜΑΤΩΝ
                                if (step.getTime() > 0) {
                                    Countdown countdown = CountdownFactory.countdown(step.toString(), step.getTime());

                                    //NOTIFIER ΓΙΑ ΕΙΔΟΠΟΙΗΣΗ ΤΕΛΟΥΣ ΑΝΤ. ΜΕΤΡΗΣΗΣ
                                    countdown.addNotifier(new Notifier() {
                                        @Override
                                        public void finished(Countdown c) {
                                            SwingUtilities.invokeLater(() -> {
                                                resultArea.append("Η αντίστροφη μέτρηση ολοκληρώθηκε: " + c.getName() + "\n");
                                            });
                                        }
                                    });

                                    countdown.start();

                                    //LOOP ΓΙΑ ΑΝΤ. ΜΕΤΡΗΣΗ ΜΕ STOP ΣΤΗ ΛΗΞΗ ΤΗΣ
                                    while (true) {
                                        if (countdown.secondsRemaining() == 0) {
                                            countdown.stop();
                                            break;
                                        }

                                        try {
                                            SwingUtilities.invokeLater(() -> resultArea.append(
                                                    "Χρόνος που απομένει: " + countdown.secondsRemaining() + " δευτερόλεπτα\n"));
                                            // ΕΝΗΜΕΡΩΣΗ ΚΑΘΕ 1 ΔΕΥΤΕΡΟΛΕΠΤΟ
                                            Thread.sleep(1000);
                                        } catch (InterruptedException ignored) {
                                        }
                                    }

                                    //PROMPT ΧΡΗΣΤΗ
                                    boolean userResponse = false;
                                    while (!userResponse) {
                                        int response = JOptionPane.showConfirmDialog(null,
                                                "Τέλος αντίστροφης μέτρησης. Είστε έτοιμοι να προχωρήσετε στο επόμενο βήμα;",
                                                "Επιβεβαίωση", JOptionPane.YES_NO_OPTION);

                                        if (response == JOptionPane.YES_OPTION) {
                                            userResponse = true;
                                        } else {
                                            SwingUtilities.invokeLater(() -> resultArea.append("Παρακαλώ επιβεβαιώστε ότι είστε έτοιμοι να προχωρήσετε.\n"));
                                        }
                                    }
                                }

                            }
                            //ΟΛΟΚΛΗΡΩΣΗ ΔΙΑΔΙΚΑΣΙΑΣ
                            SwingUtilities.invokeLater(() -> resultArea.append("Η συνταγή ολοκληρώθηκε!\n"));
                        }).start();

                    } catch (IOException ex) {
                        resultArea.setText("Σφάλμα κατά την ανάγνωση της συνταγής.");
                    }
                }
            });

            //ΕΜΦΑΝΙΣΗ GUI
            frame.setVisible(true);
        });
    }
}
