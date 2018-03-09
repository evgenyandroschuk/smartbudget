package smartbudget.view.json.expenses.type;

import java.util.List;

public class ExpensesRequest {

    private String date;

    private int month;
    private int year;

    private List<ExpensesData> expensesData;

    public ExpensesRequest() {
        super();

    }

    public ExpensesRequest(String date, int month, int year, List<ExpensesData> expensesData) {
        this.date = date;
        this.month = month;
        this.year = year;
        this.expensesData = expensesData;
    }

    public String getDate() {
        return date;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public List<ExpensesData> getExpensesData() {
        return expensesData;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setExpensesData(List<ExpensesData> expensesData) {
        this.expensesData = expensesData;
    }
}
