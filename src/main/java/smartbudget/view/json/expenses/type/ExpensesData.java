package smartbudget.view.json.expenses.type;

public class ExpensesData {

    private String description;
    private int type;
    private double amount;

    public ExpensesData(String description, int type, double amount) {
        this.description = description;
        this.type = type;
        this.amount = amount;
    }

    public ExpensesData() {
        super();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
