package smartbudget.service;

import smartbudget.model.ExpensesData;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    Map<String, String> getStatisticByYear(int year);

    Map<String, String> getStatisticByYearMonth(int year, int month);

    double getLastPeriodExpenses(int startId);

    double getIncomeAmount(int startId);

    Map<String, Double> getCurrencies();

    double getFundAmount(int fundId, int currencyId);

    List<Map<String, String>> getYearlyExpenses(int year);

    List<Map<String,String>> getFunds();


}
