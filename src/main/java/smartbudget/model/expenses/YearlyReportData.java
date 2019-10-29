package smartbudget.model.expenses;

import com.google.common.base.Objects;

import java.util.List;

public class YearlyReportData {

    private int month;
    private List<String> amounts;

    public YearlyReportData(int month, List<String> amount) {
        this.month = month;
        this.amounts = amount;
    }

    public int getMonth() {
        return month;
    }

    public List<String> getAmounts() {
        return amounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YearlyReportData that = (YearlyReportData) o;
        return month == that.month &&
                Objects.equal(amounts, that.amounts);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(month, amounts);
    }

    @Override
    public String toString() {
        return "YearlyReportData{" +
                "month=" + month +
                ", amounts=" + amounts +
                '}';
    }
}
