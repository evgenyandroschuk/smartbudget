package smartbudget.service.postres.expenses;

import smartbudget.model.expenses.CurrentStatistic;

public interface ExpensesDataService {

    CurrentStatistic getCurrentStatistic(int userId);

}
