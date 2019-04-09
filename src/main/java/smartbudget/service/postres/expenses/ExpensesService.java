package smartbudget.service.postres.expenses;

import smartbudget.model.expenses.ExpensesType;

import java.util.List;

public interface ExpensesService {

    List<ExpensesType> getExpensesTypes();

}
