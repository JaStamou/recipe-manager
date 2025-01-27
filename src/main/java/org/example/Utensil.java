package org.example;

import java.util.Objects;

public class Utensil {
    private final String name;

    public Utensil(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    //ΕΠΙΣΤΡΟΦΗ ΤΟΥ ΟΝΟΜΑΤΟΣ ΤΟΥ ΣΚΕΥΟΥΣ
    public String toString() {
        return name;
    }

    //ΜΕΘΟΔΟΣ ΠΟΥ ΕΠΙΣΤΡΕΦΕΙ TRUE ΜΟΝΟ ΑΝ ΔΥΟ ΣΚΕΥΗ ΕΧΟΥΝ ΙΔΙΟ ΟΝΟΜΑ
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utensil utensil = (Utensil) o;
        return name.equals(utensil.name);
    }
}
