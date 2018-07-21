package smartbudget.service;

import smartbudget.model.ExpensesData;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface ExpensesService {

    void create(ExpensesData expensesData);

    void create(List<ExpensesData> expensesDataList);

    void update(Long id, ExpensesData expensesData);

    void delete(Long id );

    ExpensesData findById(Long id) throws SQLException;

    List<ExpensesData> findByMonthYear(int month, int year) throws SQLException;

    List<ExpensesData> findByYear(int year) throws SQLException;

    List<ExpensesData> findByDescription(String description, String start, String end) throws SQLException;

    List<ExpensesData> findByTypeMonthYear(int type, int month, int year) throws SQLException;

    List<ExpensesData> findByTypeYear(int type, int year) throws SQLException;


}
