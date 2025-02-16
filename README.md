# Recipe Manager

## 🔎 Επισκόπηση
Το **Recipe Manager** είναι μια εφαρμογή βασισμένη στη γλώσσα **Java**, η οποία επιτρέπει στους χρήστες να διαχειρίζονται και να εκτελούν συνταγές μαγειρικής. Παρέχει λειτουργίες για **φόρτωση, προβολή και εκτέλεση συνταγών** μέσω ενός **γραφικού περιβάλλοντος χρήστη (GUI)** που υλοποιείται με **Swing (`JFrame`)**.

---

## 🛠️ Χαρακτηριστικά
### **1. Φόρτωση Συνταγών**
- Δυνατότητα φόρτωσης πολλαπλών αρχείων συνταγών **(`.cook`)** ταυτόχρονα.
- Εμφάνιση της λίστας των φορτωμένων συνταγών για περαιτέρω επεξεργασία.

### **2. Προβολή Συνταγών**
- Εμφάνιση των λεπτομερειών της συνταγής, συμπεριλαμβανομένων των βημάτων εκτέλεσης, των απαιτούμενων υλικών, των ποσοτήτων και των σκευών.

### **3. Λίστα Αγορών**
- Δημιουργία **συνολικής λίστας αγορών** βασισμένης στις επιλεγμένες συνταγές.

### **4. Εκτέλεση Συνταγών**
- Βήμα-βήμα καθοδήγηση για την εκτέλεση της συνταγής.
- Χρονομέτρηση για τα βήματα που απαιτούν χρόνο αναμονής ή μαγειρέματος.
- **Διαδραστικότητα**: Το πρόγραμμα περιμένει επιβεβαίωση από τον χρήστη πριν προχωρήσει στο επόμενο βήμα.

---

## 📌 Τεχνικές Πληροφορίες
- **Γλώσσα Προγραμματισμού**: Java
- **Γραφικό Περιβάλλον**: `JFrame` (Swing)
- **Build Tool**: Maven
- **Βιβλιοθήκες και Εξαρτήσεις**:
    - `countdown`: Διαχείριση αντίστροφης μέτρησης στα βήματα της συνταγής.
    - `JUnit`: Μονάδες ελέγχου (unit testing) για τη διασφάλιση της σωστής λειτουργίας.

---

## 🔧 Προαπαιτούμενα
Για την εκτέλεση της εφαρμογής, πρέπει να έχετε εγκατεστημένα τα ακόλουθα:

- **Java Development Kit (JDK)**: Έκδοση **21** ή νεότερη.
- **Maven**: Έκδοση **3.8.1** ή νεότερη.

---

## 📁 Δομή του Έργου
- **Κύριες Κλάσεις**:
    - `MainApplication`: Σημείο εισόδου της εφαρμογής. Αρχικοποιεί το GUI και διαχειρίζεται την αλληλεπίδραση με τον χρήστη.
    - `FileReaderUtility`: Διαβάζει τα αρχεία .cook.
    - `Extractor`: Εξάγει τα απαραίτητα σεσημασμένα δεδομένα με regex patterns
    - `Step`: Διαχειρίζεται τα βήματα των συνταγών.
    - `ShoppingList`: Δημιουργεί μία **λίστα αγορών** με δυνατότητα επιλογής πολλαπλών συνταγών
    - `Ingredient`: Διαχειρίζεται τα δεδομένα των υλικών.
    - `Utensil`: Διαχειρίζεται τα δεδομένα των σκευών μαγειρικής.

- **Διαχείριση Αρχείων**:
    - Οι συνταγές αποθηκεύονται σε αρχεία **`.cook`** και αναλύονται προκειμένου να εξαχθούν οι πληροφορίες τους, όπως τα **υλικά, οι ποσότητες, τα σκεύη και οι χρόνοι εκτέλεσης των βημάτων**.

---


### **1️⃣ Εκτέλεση μέσω Τερματικού**
Ανοίξτε το τερματικό και εκτελέστε τα παρακάτω:

```sh
# Μεταβείτε στον φάκελο του έργου
cd /path/to/RecipeManager

# Καθαρισμός και ανανέωση των εξαρτήσεων Maven
mvn clean install

# Εκτέλεση της εφαρμογής
java -jar target/RecipeManager-part2.jar
