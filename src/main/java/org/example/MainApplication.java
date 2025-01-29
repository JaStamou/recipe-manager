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
        SwingUtilities.invokeLater(() -> {
                    // Δημιουργία παραθύρου
                    JFrame frame = new JFrame("Recipe Manager");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setSize(900, 600);
                    frame.setLocationRelativeTo(null);

                    // Δημιουργία κύριου πάνελ
                    JPanel mainPanel = new JPanel(new BorderLayout());
                    frame.add(mainPanel);

                    // Πάνελ κουμπιών
                    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    mainPanel.add(buttonPanel, BorderLayout.NORTH);

                    // Λίστα συνταγών
                    DefaultListModel<File> recipeListModel = new DefaultListModel<>();
                    JList<File> recipeList = new JList<>(recipeListModel);
                    recipeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    JScrollPane recipeScrollPane = new JScrollPane(recipeList);
                    recipeScrollPane.setPreferredSize(new Dimension(250, 0));
                    mainPanel.add(recipeScrollPane, BorderLayout.WEST);

                    // Περιοχή εμφάνισης
                    JTextArea resultArea = new JTextArea();
                    resultArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(resultArea);
                    mainPanel.add(scrollPane, BorderLayout.CENTER);

                    // Κουμπιά
                    JButton loadButton = new JButton("Φόρτωση Συνταγών");
                    JButton viewButton = new JButton("Εμφάνιση Συνταγής");
                    JButton shoppingListButton = new JButton("Λίστα Αγορών");
                    JButton executeButton = new JButton("Εκτέλεση Συνταγής");
                    viewButton.setEnabled(false);
                    shoppingListButton.setEnabled(false);
                    executeButton.setEnabled(false);

                    // Προσθήκη κουμπιών
                    buttonPanel.add(loadButton);
                    buttonPanel.add(viewButton);
                    buttonPanel.add(shoppingListButton);
                    buttonPanel.add(executeButton);

                    // Λειτουργία φόρτωσης συνταγών
                    loadButton.addActionListener(e -> {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setMultiSelectionEnabled(true);
                        fileChooser.setFileFilter(new FileNameExtensionFilter("Recipe Files (.cook)", "cook"));
                        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                            for (File file : fileChooser.getSelectedFiles()) {
                                recipeListModel.addElement(file);
                            }
                        }
                    });

                    // Ενεργοποίηση κουμπιών κατά την επιλογή συνταγής
                    recipeList.addListSelectionListener(e -> {
                        boolean isSelected = !recipeList.isSelectionEmpty();
                        viewButton.setEnabled(isSelected);
                        shoppingListButton.setEnabled(isSelected);
                        executeButton.setEnabled(isSelected);
                    });

                    // Λειτουργία εμφάνισης συνταγής
                    viewButton.addActionListener(e -> {
                        File selectedFile = recipeList.getSelectedValue();
                        if (selectedFile != null) {
                            try {
                                List<String> lines = Files.readAllLines(selectedFile.toPath());
                                Extractor.ExtractedData data = Extractor.extractData(lines);

                                int totalTime = data.getSteps().stream()
                                        .mapToInt(Step::getTime)
                                        .sum();

                                resultArea.setText("Συνταγή: " + selectedFile.getName() + "\n\n");
                                resultArea.append("Υλικά:\n");
                                for (Ingredient ingredient : data.getIngredients()) {
                                    resultArea.append("- " + ingredient + "\n");
                                }
                                resultArea.append("\nΣκεύη:\n");
                                for (Utensil utensil : data.getUtensils()) {
                                    resultArea.append("- " + utensil + "\n");
                                }
                                resultArea.append("\nΒήματα:\n");
                                for (int i = 0; i < data.getSteps().size(); i++) {
                                    resultArea.append((i + 1) + ". " + data.getSteps().get(i) + "\n\n");
                                }
                                resultArea.append("Συνολικός Χρόνος: " + totalTime + " λεπτά\n\n");
                            } catch (IOException ex) {
                                resultArea.setText("Σφάλμα κατά την ανάγνωση της συνταγής.");
                            }
                        }
                    });

                    // Λειτουργία παραγωγής λίστας αγορών
                    shoppingListButton.addActionListener(e -> {
                        int[] selectedIndices = recipeList.getSelectedIndices();
                        if (selectedIndices.length > 0) {
                            try {
                                ShoppingList shoppingList = new ShoppingList();
                                for (int index : selectedIndices) {
                                    File file = recipeListModel.get(index);
                                    shoppingList.addIngredientsFromRecipe(file.getAbsolutePath());
                                }

                                resultArea.setText("Λίστα Αγορών:\n\n");
                                for (Ingredient ingredient : shoppingList.getIngredients()) {
                                    resultArea.append("- " + ingredient + "\n");
                                }
                            } catch (IOException ex) {
                                resultArea.setText("Σφάλμα κατά τη δημιουργία λίστας αγορών.");
                            }
                        } else {
                        }
                    });

            // Λειτουργία εκτέλεσης συνταγής
            executeButton.addActionListener(e -> {
                File selectedFile = recipeList.getSelectedValue();
                if (selectedFile != null) {
                    try {
                        List<String> lines = Files.readAllLines(selectedFile.toPath());
                        Extractor.ExtractedData data = Extractor.extractData(lines);

                        new Thread(() -> {
                            SwingUtilities.invokeLater(() -> resultArea.setText("Ξεκινάμε τη συνταγή.\n\n"));

                            int i=0;
                            for (Step step : data.getSteps()) {

                                int stepCounter = i;
                                SwingUtilities.invokeLater(() -> resultArea.append("Βήμα " + stepCounter + ": " + step + "\n"));
                                i++;


                                if (step.getTime() > 0) {
                                    Countdown countdown = CountdownFactory.countdown(step.toString(), step.getTime());

                                    countdown.addNotifier(new Notifier() {
                                        @Override
                                        public void finished(Countdown c) {
                                            SwingUtilities.invokeLater(() -> {
                                                resultArea.append("Η αντίστροφη μέτρηση ολοκληρώθηκε: " + c.getName() + "\n");
                                            });
                                        }
                                    });

                                    countdown.start();

                                    // Αναμονή για την απάντηση του χρήστη
                                    while (true) {
                                        // Αναμονή για την ολοκλήρωση ή παύση της αντίστροφης μέτρησης
                                        if (countdown.secondsRemaining() == 0) {
                                            countdown.stop(); // Διακοπή της αντίστροφης μέτρησης
                                            break;
                                        }

                                        try {
                                            Thread.sleep(1000); // Ενημέρωση κάθε 1 δευτερόλεπτο
                                            SwingUtilities.invokeLater(() -> resultArea.append(
                                                    "Χρόνος που απομένει: " + countdown.secondsRemaining() + " δευτερόλεπτα\n"));
                                        } catch (InterruptedException ignored) {
                                        }
                                    }

                                    // Περιμένουμε απάντηση από τον χρήστη
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
                            SwingUtilities.invokeLater(() -> resultArea.append("Η συνταγή ολοκληρώθηκε!\n"));
                        }).start();

                    } catch (IOException ex) {
                        resultArea.setText("Σφάλμα κατά την ανάγνωση της συνταγής.");
                    }
                }
            });

           // Εμφάνιση παραθύρου
            frame.setVisible(true);
        });
    }
}
