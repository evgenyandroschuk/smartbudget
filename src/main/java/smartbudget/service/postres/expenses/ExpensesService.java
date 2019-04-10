package smartbudget.service.postres.expenses;

import smartbudget.model.expenses.ExpensesData;
import smartbudget.model.expenses.ExpensesType;

import java.time.LocalDate;
import java.util.List;

public interface ExpensesService {

    List<ExpensesType> getExpensesTypes(int userId);

    List<ExpensesData> getExpensesByYear(int userId, int year);

    List<ExpensesData> getExpensesByYearMonth(int userId, int year, int month);

    List<ExpensesData> getExpensesByPeriod(int userId, LocalDate startDate, LocalDate endDate);
}
