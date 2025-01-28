package org.example;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainApplication {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Δημιουργία του βασικού JFrame
            JFrame frame = new JFrame("Recipe Manager");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            // Δημιουργία κύριου πάνελ
            JPanel mainPanel = new JPanel(new BorderLayout());
            frame.add(mainPanel);

            // Ετικέτα για οδηγίες
            JLabel instructionLabel = new JLabel("Επιλέξτε αρχεία συνταγών για φόρτωση:");
            instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
            mainPanel.add(instructionLabel, BorderLayout.NORTH);

            // Κουμπί για φόρτωση αρχείων
            JButton loadFilesButton = new JButton("Φόρτωση Συνταγών");
            mainPanel.add(loadFilesButton, BorderLayout.CENTER);

            // Περιοχή για εμφάνιση αποτελεσμάτων
            JTextArea resultArea = new JTextArea();
            resultArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(resultArea);
            mainPanel.add(scrollPane, BorderLayout.NORTH);

            // Λίστα για αποθήκευση αρχείων
            List<File> loadedFiles = new ArrayList<>();

            // Λειτουργικότητα κουμπιού για φόρτωση αρχείων
            loadFilesButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                fileChooser.setFileFilter(new FileNameExtensionFilter("Recipe Files (.cook)", "cook"));

                int returnValue = fileChooser.showOpenDialog(frame);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File[] files = fileChooser.getSelectedFiles();
                    for (File file : files) {
                        loadedFiles.add(file);
                        resultArea.append("Φορτώθηκε αρχείο: " + file.getName() + "\n");
                    }
                } else {
                    resultArea.append("Δεν επιλέχθηκαν αρχεία.\n");
                }
            });

            // Εμφάνιση παραθύρου
            frame.setVisible(true);
        });
    }
}
