import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

import java.util.List;  // Important: import only java.util.List
import java.util.Map;
import java.util.HashMap;

public class ExpenseTrackerUI extends JFrame {
    private List<Expense> expenses = ExpenseDAO.loadExpenses();
    private DefaultTableModel model;
    private JTable table;

    public ExpenseTrackerUI() {
        setTitle("Expense Tracker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Panel
        JPanel topPanel = new JPanel(new FlowLayout());
        JTextField dateField = new JTextField(10);
        JTextField categoryField = new JTextField(10);
        JTextField amountField = new JTextField(8);
        JButton addButton = new JButton("Add Expense");

        topPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        topPanel.add(dateField);
        topPanel.add(new JLabel("Category:"));
        topPanel.add(categoryField);
        topPanel.add(new JLabel("Amount:"));
        topPanel.add(amountField);
        topPanel.add(addButton);

        // Center Panel
        String[] cols = {"Date", "Category", "Amount"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        refreshTable();

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton editButton = new JButton("Edit Selected");
        JButton deleteButton = new JButton("Delete Selected");
        JButton summaryButton = new JButton("Show Summary");

        bottomPanel.add(editButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(summaryButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Add Expense
        addButton.addActionListener(e -> {
            try {
                LocalDate date = LocalDate.parse(dateField.getText());
                String category = categoryField.getText();
                double amount = Double.parseDouble(amountField.getText());
                Expense exp = new Expense(date, category, amount);
                expenses.add(exp);
                ExpenseDAO.saveExpenses(expenses);
                refreshTable();
                dateField.setText(""); categoryField.setText(""); amountField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Delete Entry
        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                expenses.remove(row);
                ExpenseDAO.saveExpenses(expenses);
                refreshTable();
            }
        });

        // Edit Entry
        editButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Expense exp = expenses.get(row);
                try {
                    String newDate = JOptionPane.showInputDialog("New Date", exp.getDate());
                    String newCategory = JOptionPane.showInputDialog("New Category", exp.getCategory());
                    String newAmount = JOptionPane.showInputDialog("New Amount", exp.getAmount());

                    exp.setDate(LocalDate.parse(newDate));
                    exp.setCategory(newCategory);
                    exp.setAmount(Double.parseDouble(newAmount));

                    ExpenseDAO.saveExpenses(expenses);
                    refreshTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid data!");
                }
            }
        });

        // Summary
        summaryButton.addActionListener(e -> {
            double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
            Map<String, Double> byCategory = new HashMap<>();
            for (Expense exp : expenses) {
                byCategory.put(exp.getCategory(), byCategory.getOrDefault(exp.getCategory(), 0.0) + exp.getAmount());
            }

            StringBuilder summary = new StringBuilder("Total Spent: ₹" + total + "\n\nBy Category:\n");
            byCategory.forEach((cat, amt) -> summary.append(cat).append(": ₹").append(amt).append("\n"));

            JOptionPane.showMessageDialog(this, summary.toString(), "Summary", JOptionPane.INFORMATION_MESSAGE);
        });

        setVisible(true);
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Expense exp : expenses) {
            model.addRow(new Object[]{exp.getDate(), exp.getCategory(), "₹" + exp.getAmount()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpenseTrackerUI::new);
    }
}
