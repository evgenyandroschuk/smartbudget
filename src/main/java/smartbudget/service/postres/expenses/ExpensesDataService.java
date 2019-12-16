package smartbudget.service.postres.expenses;

import smartbudget.model.expenses.CurrentStatistic;
import smartbudget.model.expenses.YearlyReport;

public interface ExpensesDataService {

    CurrentStatistic getCurrentStatistic(int userId);

    YearlyReport getReportsByYear(int userId, int year);
}
