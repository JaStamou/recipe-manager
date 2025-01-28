package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Countdown {
    private int timeRemaining; // Ο χρόνος που απομένει, σε δευτερόλεπτα
    private Timer timer;       // Timer για την αντίστροφη μέτρηση
    private CountdownListener listener; // Ακροατής για τα γεγονότα της αντίστροφης μέτρησης

    // Δημιουργία της αντίστροφης μέτρησης
    public Countdown(int timeInSeconds, CountdownListener listener) {
        this.timeRemaining = timeInSeconds;
        this.listener = listener;
        initializeTimer();
    }

    // Αρχικοποίηση του Timer
    private void initializeTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeRemaining > 0) {
                    timeRemaining--;
                    listener.onTick(timeRemaining); // Κάθε tick ενημερώνεται ο listener
                } else {
                    timer.stop();
                    listener.onFinish(); // Όταν τελειώσει η αντίστροφη μέτρηση
                }
            }
        });
    }

    // Εκκίνηση της αντίστροφης μέτρησης
    public void start() {
        if (timer != null) {
            timer.start();
            listener.onStart(timeRemaining); // Ενημέρωση ότι ξεκίνησε
        }
    }

    // Διακοπή της αντίστροφης μέτρησης
    public void stop() {
        if (timer != null) {
            timer.stop();
            listener.onStop(); // Ενημέρωση ότι σταμάτησε
        }
    }

    // Επιστροφή του χρόνου που απομένει
    public int getTimeRemaining() {
        return timeRemaining;
    }

    // Επαναφορά της αντίστροφης μέτρησης
    public void reset(int newTimeInSeconds) {
        stop();
        this.timeRemaining = newTimeInSeconds;
        initializeTimer();
    }

    // Ακροατής για την αντίστροφη μέτρηση
    public interface CountdownListener {
        void onStart(int totalTime); // Όταν ξεκινά η αντίστροφη μέτρηση
        void onTick(int secondsRemaining); // Κάθε tick της αντίστροφης μέτρησης
        void onFinish(); // Όταν ολοκληρώνεται η αντίστροφη μέτρηση
        void onStop(); // Όταν σταματά η αντίστροφη μέτρηση
    }
}
