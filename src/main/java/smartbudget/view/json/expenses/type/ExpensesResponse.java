package smartbudget.view.json.expenses.type;

import smartbudget.model.Expenses;

import java.util.List;

public class ExpensesResponse {

    private String description;

    private List<Expenses> expenses;

    public ExpensesResponse(String description, List<Expenses> expenses) {
        this.description = description;
        this.expenses = expenses;
    }

    public ExpensesResponse() {
        super();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Expenses> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expenses> expenses) {
        this.expenses = expenses;
    }
}
