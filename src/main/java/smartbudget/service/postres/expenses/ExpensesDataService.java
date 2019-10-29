package smartbudget.service.postres.expenses;

import smartbudget.model.expenses.CurrentStatistic;
import smartbudget.model.expenses.YearlyReportData;

import java.util.List;

public interface ExpensesDataService {

    CurrentStatistic getCurrentStatistic(int userId);

    List<YearlyReportData> getReportsByYear(int userId, int year);
}
