package org.example;

import gr.hua.dit.oop2.countdown.Countdown;
import gr.hua.dit.oop2.countdown.CountdownFactory;
import gr.hua.dit.oop2.countdown.Notifier;

public class RecipeTimer {
    private Countdown countdown;



    public void start() {
        System.out.println("Ξεκινάει η αντίστροφη μέτρηση για: " + countdown.getName());
        countdown.addNotifier(new Notifier() {
            @Override
            public void finished(Countdown c) {
                System.out.println("Η αντίστροφη μέτρηση ολοκληρώθηκε για: " + c.getName());
            }
        });
        countdown.start();
        monitorCountdown();
    }

    private void monitorCountdown() {
        // Παρακολουθεί την αντίστροφη μέτρηση μέχρι να ολοκληρωθεί
        while (countdown.secondsRemaining() > 0) {
            System.out.println("Υπολειπόμενος χρόνος: " + countdown.secondsRemaining() + " δευτερόλεπτα");
            try {
                Thread.sleep(1000); // Παύση για 1 δευτερόλεπτο
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        countdown.stop();
        System.out.println("Η αντίστροφη μέτρηση διακόπηκε.");
    }
}
