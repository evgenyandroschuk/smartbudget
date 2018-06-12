package smartbudget.service;

import smartbudget.model.ExpensesData;

import java.time.LocalDate;
import java.util.List;

public interface ExpensesService {

    void create(ExpensesData expensesData);

    void create(List<ExpensesData> expensesDataList);

    void update(Long id, ExpensesData expensesData);

    void delete(Long id );

    ExpensesData findById(Long id);

    List<ExpensesData> findByMonthYear(int month, int year);

    List<ExpensesData> findByYear(int year);

    List<ExpensesData> findByDescription(String description, String start, String end);

    List<ExpensesData> findByTypeMonthYear(int type, int month, int year);

    List<ExpensesData> findByTypeYear(int type, int year);


}
