package smartbudget.model.expenses;

import java.util.List;

public class ExpensesReport {

    private Double amount;
    private List<ExpensesData> expensesDataList;
    private String startDate;
    private String endDate;

    public ExpensesReport(Double amount, List<ExpensesData> expensesDataList, String startDate, String endDate) {
        this.amount = amount;
        this.expensesDataList = expensesDataList;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Double getAmount() {
        return amount;
    }

    public List<ExpensesData> getExpensesDataList() {
        return expensesDataList;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "ExpensesReport{" +
                "amount=" + amount +
                ", expensesDataList=" + expensesDataList +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
