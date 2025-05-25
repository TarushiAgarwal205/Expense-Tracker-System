import java.io.Serializable;
import java.time.LocalDate;

public class Expense implements Serializable {
    private LocalDate date;
    private String category;
    private double amount;

    public Expense(LocalDate date, String category, double amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    public LocalDate getDate() { return date; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }

    public void setDate(LocalDate date) { this.date = date; }
    public void setCategory(String category) { this.category = category; }
    public void setAmount(double amount) { this.amount = amount; }

    @Override
    public String toString() {
        return date + " | " + category + " | ₹" + amount;
    }
}
