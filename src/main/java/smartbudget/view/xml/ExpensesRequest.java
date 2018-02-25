package smartbudget.view.xml;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by evgenyandroshchuk on 23.12.17.
 */
@XmlRootElement(name = "ExpensesItems")
@XmlType(namespace = "http://localhost:7004/expensesrequest")
public class ExpensesRequest implements Serializable {


    private ExpensesList expensesList;

    private int month;

    private int year;

    private String updateDate;

    @XmlElement(name = "expenses")
    public ExpensesList getExpensesList() {
        return expensesList;
    }

    public void setExpensesList(ExpensesList expensesList) {
        this.expensesList = expensesList;
    }

    @XmlElement(name = "month", required = true)
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @XmlElement(name = "year", required = true)
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @XmlElement(name = "updateDate", required = true)
    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

}
