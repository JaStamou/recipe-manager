# Recipe Manager

## Overview
Recipe Manager is a Java-based desktop application designed to help users manage and execute cooking recipes. It provides functionalities for loading, viewing, and executing recipes through an intuitive graphical user interface (GUI) built using `JFrame`.

---

## Features
1. **Load Recipes**:
   - Load multiple recipe files (`.cook`) at once.
   - Displays the list of loaded recipes for further actions.

2. **View Recipes**:
   - Display the detailed steps, required ingredients, quantities, and utensils for each recipe.

3. **Shopping List**:
   - Generate a combined shopping list when planning to cook multiple recipes.

4. **Execute Recipes**:
   - Step-by-step guide for cooking recipes with timers for steps that require waiting or cooking durations.

---

## Technical Details
- **Language**: Java
- **GUI Framework**: `JFrame` (Swing)
- **Build Tool**: Maven
- **Dependencies**:
  - `countdown`: For handling step timers.
  - `JUnit`: For unit testing.

---

## Project Structure
- **Main Classes**:
  - `MainApplication`: Entry point for the application. Initializes the GUI and handles user interactions.
  - `Recipe`: Represents a single recipe, including steps, ingredients, and utensils.
  - `Step`: Represents a single step in a recipe.
  - `ShoppingList`: Combines ingredients from multiple recipes into a consolidated list.

- **File Parsing**:
  - Recipe files follow the `.cook` format and are parsed to extract structured information like ingredients, quantities, utensils, and step durations.
