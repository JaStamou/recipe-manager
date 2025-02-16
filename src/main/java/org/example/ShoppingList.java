package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShoppingList {
    private List<Ingredient> ingredients = new ArrayList<>();

    // ΜΕΘΟΔΟΣ ΠΟΥ ΕΚΤΕΛΕΙ ΤΗΝ ΠΡΟΣΘΗΚΗ Η ΕΝΗΜΕΡΩΣΗ ΥΛΙΚΟΥ
    public void addIngredientsFromRecipe(String recipeFile) throws IOException {
        List<String> lines = FileReaderUtility.readFile(recipeFile);
        Extractor.ExtractedData data = Extractor.extractData(lines);

        for (Ingredient ingredient : data.getIngredients()) {
            addOrUpdateIngredient(ingredient);
        }
    }

    // ΜΕΘΟΔΟΣ ΠΟΥ ΕΛΕΓΧΕΙ ΑΝ ΤΟ ΥΛΙΚΟ ΘΑ ΕΝΗΜΕΡΩΘΕΙ Η ΘΑ ΠΡΟΣΤΕΘΕΙ
    private void addOrUpdateIngredient(Ingredient newIngredient) {
        for (Ingredient existing : ingredients) {
            if (existing.equals(newIngredient)) {
                existing.addQuantity(newIngredient.getQuantity());
                return;
            }
        }
        ingredients.add(newIngredient);
    }

    // ΜΕΘΟΔΟΣ ΓΙΑ ΤΗΝ ΕΚΤΥΠΩΣΗ ΤΗΣ ΛΙΣΤΑΣ
    public void printShoppingList() {
        for (Ingredient ingredient : ingredients) {
            System.out.println(ingredient);
        }
    }

    // ΝΕΑ ΜΕΘΟΔΟΣ ΓΙΑ ΕΠΙΣΤΡΟΦΗ ΤΗΣ ΛΙΣΤΑΣ ΥΛΙΚΩΝ
    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}
