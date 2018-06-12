package smartbudget.service;

import smartbudget.model.ExpensesTypeData;

import java.util.List;

public interface ExpensesTypeService {

    void save(ExpensesTypeData expensesTypeData);

    void update(int id, ExpensesTypeData expensesTypeData);

    ExpensesTypeData getById(int id);

    List<ExpensesTypeData> getAll();

    List<ExpensesTypeData> getActiveType();

}
