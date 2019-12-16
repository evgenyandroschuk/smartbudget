package smartbudget.model.expenses;

import java.util.List;

public class YearlyReport {

    private List<YearlyReportData> reportDataList;
    private List<String> totals;

    public YearlyReport(List<YearlyReportData> reportDataList, List<String> totals) {
        this.reportDataList = reportDataList;
        this.totals = totals;
    }

    public List<YearlyReportData> getReportDataList() {
        return reportDataList;
    }

    public List<String> getTotals() {
        return totals;
    }

    @Override
    public String toString() {
        return "YearlyReport{" +
                "reportDataList=" + reportDataList +
                ", totals=" + totals +
                '}';
    }
}
