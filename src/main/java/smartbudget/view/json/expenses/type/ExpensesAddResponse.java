package smartbudget.view.json.expenses.type;

import java.util.List;

public class ExpensesAddResponse {

    private String status;
    private List<ExpensesData> expensesList;

    public ExpensesAddResponse(String status, List<ExpensesData> expensesList) {
        this.status = status;
        this.expensesList = expensesList;
    }

    public String getStatus() {
        return status;
    }

    public List<ExpensesData> getExpensesList() {
        return expensesList;
    }
}
