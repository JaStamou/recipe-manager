package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extractor {
    //REGEX ΓΙΑ ΕΞΑΓΩΓΗ ΥΛΙΚΩΝ, ΣΚΕΥΩΝ ΚΑΙ ΧΡΟΝΟΥ
    private static final String Ingredient_Pattern =
            "@([Α-Ωα-ωΆ-Ώά-ώA-Za-z]+(?: [Α-Ωα-ωΆ-Ώά-ώA-Za-z]+)*(?=\\{)|[Α-Ωα-ωΆ-Ώά-ώA-Za-z]+)\\{?(\\d+)?(?:%([Α-Ωα-ωΆ-Ώά-ώA-Za-z]+))?}?";
    private static final String Utensil_Pattern =
            "#([Α-Ωα-ωΆ-Ώά-ώA-Za-z]+(?: [Α-Ωα-ωΆ-Ώά-ώA-Zaζ]+)*(?=\\{\\})|[Α-Ωα-ωΆ-Ώά-ώA-Za-z]+)";
    private static final String Time_Pattern = "~\\{(\\d+)%([Α-Ωα-ωΆ-Ώά-ώA-Za-z]+)\\}";

    //ΜΕΘΟΔΟΣ ΠΟΥ ΕΞΑΓΕΙ ΤΑ ΔΕΔΟΜΕΝΑ ΑΠΟ ΤΙΣ ΓΡΑΜΜΕΣ ΤΟΥ ΑΡΧΕΙΟΥ .COOK
    public static ExtractedData extractData(List<String> lines) {
        List<Ingredient> ingredients = new ArrayList<>();
        List<Utensil> utensils = new ArrayList<>();
        List<Step> steps = new ArrayList<>();

        //ΑΠΟΘΗΚΕΥΕΙ ΤΗΝ ΠΕΡΙΓΡΑΦΗ ΤΟΥ ΚΑΘΕ ΒΗΜΑΤΟΣ
        StringBuilder currentStep = new StringBuilder();

        //ΑΠΟΘΗΚΕΥΕΙ ΤΟΝ ΣΥΝΟΛΙΚΟ ΧΡΟΝΟ ΤΩΝ ΒΗΜΑΤΩΝ
        int totalTime = 0;

        for (String line : lines) {

            //ΑΝ Η ΓΡΑΜΜΗ ΕΙΝΑΙ ΚΕΝΗ ΣΗΜΑΤΟΔΟΤEI ΤΗΝ ΛΗΞΗ ΤΟΥ STEP ΚΑΙ ΤΟ ΠΡΟΣΘΕΤΕΙ ΣΤΗΝ ΛΙΣΤΑ
            if (line.isEmpty()) {
                addStep(steps, currentStep.toString(), totalTime);
                currentStep.setLength(0);
                totalTime = 0;
                continue;
            }

            //ΠΡΟΣΘΗΚΗ ΤΗΣ ΓΡΑΜΜΗΣ ΣΤΗΝ ΠΕΡΙΓΡΑΦΗ ΤΟΥ ΒΗΜΑΤΟΣ
            currentStep.append(line).append(" ");

            //MATCHER ΓΙΑ ΕΞΑΓΩΓΗ ΧΡΟΝΟΥ
            Matcher timeMatcher = Pattern.compile(Time_Pattern).matcher(line);
            if (timeMatcher.find()) {
                totalTime += Integer.parseInt(timeMatcher.group(1));
            }



            //ΕΞΑΓΩΓΗ ΥΛΙΚΩΝ ΑΠΟ ΤΗΝ ΓΡΑΜΜΗ
            extractIngredients(line, ingredients);

            //ΕΞΑΓΩΓΗ ΤΩΝ ΣΚΕΥΩΝ ΑΠΟ ΤΗΝ ΓΡΑΜΜΗ
            extractUtensils(line, utensils);
        }

        // ΠΡΟΣΘΕΤΕΙ ΤΟ ΤΕΛΕΥΤΑΙΟ ΒΗΜΑ ΜΕΤΑ ΤΗΝ ΕΠΕΞΕΡΓΑΣΙΑ
        addStep(steps, currentStep.toString(), totalTime);

        //ΕΠΙΣΤΡΕΦΕΙ ΤΑ ΕΞΑΓΩΜΕΝΑ ΔΕΔΟΜΕΝΑ
        return new ExtractedData(ingredients, utensils, steps);
    }

    //ΜΕΘΟΔΟΣ ΓΙΑ MATCH ΔΕΔΟΜΕΝΩΝ ΥΛΙΚΩΝ ΚΑΙ ΕΞΑΓΩΓΗ
    private static void extractIngredients(String line, List<Ingredient> ingredients) {
        Matcher matcher = Pattern.compile(Ingredient_Pattern).matcher(line);
        while (matcher.find()) {
            String name = matcher.group(1);
            double quantity = matcher.group(2) != null ? Double.parseDouble(matcher.group(2)) : 0;
            String unit = matcher.group(3);
            //ΠΡΟΣΘΗΚΗ ΤΟΥ ΥΛΙΚΟΥ ΣΤΗ ΛΙΣΤΑ
            addIngredient(ingredients, name, quantity, unit);
        }
    }

    //ΜΕΘΟΔΟΣ ΓΙΑ MATCH ΔΕΔΟΜΕΝΩΝ ΣΚΕΥΩΝ ΚΑΙ ΕΞΑΓΩΓΗ
    private static void extractUtensils(String line, List<Utensil> utensils) {
        Matcher matcher = Pattern.compile(Utensil_Pattern).matcher(line);
        while (matcher.find()) {
            String name = matcher.group(1);
            //ΠΡΟΣΘΗΚΗ ΤΟΥ ΣΚΕΥΟΥΣ ΣΤΗ ΛΙΣΤΑ
            addUtensil(utensils, name);
        }
    }

    //ΜΕΘΟΔΟΣ ΠΟΥ ΠΡΟΣΘΕΤΕΙ ΝΕΑ ΥΛΙΚΑ ΣΤΗΝ ΛΙΣΤΑ, ΔΙΑΦΟΡΕΤΙΚΑ ΑΝ ΥΠΑΡΧΕΙ ΗΔΗ ΕΝΗΜΕΡΩΝΕΙ ΤΗΝ ΠΟΣΟΤΗΤΑ
    private static void addIngredient(List<Ingredient> ingredients, String name, double quantity, String unit) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getName().equals(name) && (ingredient.getUnit() == null || ingredient.getUnit().equals(unit))) {
                ingredient.addQuantity(quantity);
                return;
            }
        }
        ingredients.add(new Ingredient(name, quantity, unit));
    }

    //ΜΕΘΟΔΟΣ ΠΟΥ ΠΡΟΣΘΕΤΕΙ ΝΕΑ ΣΚΕΥΗ ΣΤΗΝ ΛΙΣΤΑ.
    private static void addUtensil(List<Utensil> utensils, String name) {
        if (utensils.stream().noneMatch(u -> u.getName().equals(name))) {
            utensils.add(new Utensil(name));
        }
    }

    //ΜΕΘΟΔΟΣ ΠΟΥ ΠΡΟΣΘΕΤΕΙ ΒΗΜΑΤΑ ΣΤΗΝ ΛΙΣΤΑ STEP
    private static void addStep(List<Step> steps, String description, int time) {
        if (!description.isBlank()) {
            steps.add(new Step(description.trim(), time, "minutes"));
        }
    }

    //ΚΛΑΣΣΗ ΓΙΑ ΕΞΑΓΩΜΕΝΑ ΔΕΔΟΜΕΝΑ
    public static class ExtractedData {
        private final List<Ingredient> ingredients;
        private final List<Utensil> utensils;
        private final List<Step> steps;

        public ExtractedData(List<Ingredient> ingredients, List<Utensil> utensils, List<Step> steps) {
            this.ingredients = ingredients;
            this.utensils = utensils;
            this.steps = steps;
        }

        public List<Ingredient> getIngredients() {
            return ingredients;
        }

        public List<Utensil> getUtensils() {
            return utensils;
        }

        public List<Step> getSteps() {
            return steps;
        }
    }
}
