package org.example;

public class Step {
    private final String description;
    private final int time;
    private final String timeUnit;

    public Step(String description, int time, String timeUnit) {
        this.description = description;
        this.time = time;
        this.timeUnit = timeUnit;
    }

    public int getTime() {
        return time;
    }

    //ΕΠΙΣΤΡΕΦΕΙΘ ΤΗΝ ΠΕΡΙΓΡΑΦΗ ΤΟΥ ΒΗΜΑΤΟΣ
    public String toString() {
        return description;
    }
}
