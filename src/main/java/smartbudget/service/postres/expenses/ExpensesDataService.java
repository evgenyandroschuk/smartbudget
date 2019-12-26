package smartbudget.service.postres.expenses;

import smartbudget.model.expenses.CurrentStatistic;
import smartbudget.model.expenses.ExpensesReport;
import smartbudget.model.expenses.YearlyReport;

public interface ExpensesDataService {

    CurrentStatistic getCurrentStatistic(int userId);

    YearlyReport getReportsByYear(int userId, int year);

    ExpensesReport getReportByDescription(int userId, String description, String start, String end);

    ExpensesReport getReportByType(int user, int expensesType, String start, String end);
}
