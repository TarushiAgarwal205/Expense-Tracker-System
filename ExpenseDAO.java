import java.io.*;
import java.util.*;

public class ExpenseDAO {
    private static final String FILE_NAME = "expenses.dat";

    public static List<Expense> loadExpenses() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Expense>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static void saveExpenses(List<Expense> expenses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(expenses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
