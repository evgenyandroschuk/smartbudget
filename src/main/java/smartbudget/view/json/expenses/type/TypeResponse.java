package smartbudget.view.json.expenses.type;

import smartbudget.model.ExpensesType;
import java.util.List;

public class TypeResponse {

    private String description;

    private List<ExpensesType> expensesTypes;

    public TypeResponse(String description, List<ExpensesType> expensesTypes) {
        this.description = description;
        this.expensesTypes = expensesTypes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ExpensesType> getExpensesTypes() {
        return expensesTypes;
    }

    public void setExpensesTypes(List<ExpensesType> expensesTypes) {
        this.expensesTypes = expensesTypes;
    }
}
