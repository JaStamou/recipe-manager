package org.example;

import java.util.Objects;

public class Ingredient {
    private final String name;
    private double quantity;
    private final String unit;

    //INGREDIENT CONSTRUCTOR
    public Ingredient(String name, double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    // ΜΕΘΟΔΟΣ ΠΟΥ ΠΡΟΣΘΕΤΕΙ ΠΟΣΟΤΗΤΑ ΣΕ ΥΛΙΚΑ
    public void addQuantity(double additionalQuantity) {
        this.quantity += additionalQuantity;
    }

    // ΕΠΙΣΤΡΟΦΗ ΤΟΥ ΥΛΙΚΟΥ, ΠΟΣΟΤΗΤΑΣ ΚΑΙ Μ.Μ.
    public String toString() {
        return name + (quantity > 0 ? " " + quantity + (unit != null ? " " + unit : "") : "");
    }

    //ΜΕΘΟΔΟΣ ΠΟΥ ΕΠΙΣΤΡΕΦΕΙ TRUE ΜΟΝΟ ΑΝ ΤΑ ΔΥΟ ΥΛΙΚΑ ΕΧΟΥΝ ΙΔΙΟ ΟΝΟΜΑ ΚΑΙ Μ.Μ.
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return name.equals(that.name) && Objects.equals(unit, that.unit);
    }

}
