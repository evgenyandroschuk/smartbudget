package smartbudget.view.xml;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by evgenyandroshchuk on 05.01.2018.
 */
public class ExpensesList {

    private List<ExpensesInput> expenses;

    @XmlElement(name = "item")
    public List<ExpensesInput> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpensesInput> expenses) {
        this.expenses = expenses;
    }
}
