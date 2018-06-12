package smartbudget.service.impl.mysql;

import smartbudget.db.DbUtil;
import smartbudget.model.ExpensesData;
import smartbudget.service.ExpensesService;
import smartbudget.service.impl.AbstractService;
import smartbudget.util.AppProperties;
import smartbudget.util.Numerator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class ExpensesMySQLImpl extends AbstractService implements ExpensesService {


    private DbUtil dbUtil;

    private String dateRegEx = "^[0-9]{4}-[0-9]{1}[0-9]{1}-[0-3]{1}[0-9]{1}$";

    public ExpensesMySQLImpl(AppProperties properties) {
        super(properties);
        dbUtil = new DbUtil(properties);
    }

    private void checkDateFormat(String dateString) {
        if(!Pattern.matches(dateRegEx, dateString)) {
            throw new RuntimeException("date must be in YYYY-MM-DD format");
        }
    }

    @Override
    public void create(ExpensesData expensesData) {
        String description = expensesData.getDescription();
        int month = expensesData.getMonth();
        int year = expensesData.getYear();
        int type = expensesData.getType();
        double amount = expensesData.getAmount();
        String amountString = NumberFormat.getInstance().format(amount);

        String updateDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String query =
                "insert into expenses ( id, description, month_id, year_id, operation_type_id, update_date, amount)"
                 + "values(get_id(1), '" + description + "', " + month + ", "+year+ ", "
                        + type + ", '" + updateDate + "', " + amountString + " )";
        dbUtil.executeQuery(query);
    }

    @Override
    public void create(List<ExpensesData> expensesDataList) {
        expensesDataList.forEach(t -> create(t));
    }

    @Override
    public void update(Long id, ExpensesData expensesData) {

        String description = expensesData.getDescription();
        int month = expensesData.getMonth();
        int year = expensesData.getYear();
        int type = expensesData.getType();
        double amount = expensesData.getAmount();
        String amountString = NumberFormat.getInstance().format(amount);

        String query =
                "update expenses set description = '" + description + "', operation_type_id = " + type
                + ", month_id = " + month + ", year_id = " + year + ", amount = " + amountString
                + " where id = " + id;

        dbUtil.executeQuery(query);

    }

    @Override
    public void delete(Long id) {
        String query = "delete from expenses where id = " + id;
        dbUtil.executeQuery(query);
    }

    @Override
    public ExpensesData findById(Long id) {
        String query = "select * from expenses where id = " + id;
        return getExpensesDataByQuery(query).get(0);
    }

    @Override
    public List<ExpensesData> findByMonthYear(int month, int year) {
        String query = "select * from expenses where month_id = " + month + " and year_id = " + year;
        return getExpensesDataByQuery(query);
    }

    @Override
    public List<ExpensesData> findByYear(int year) {
        String query = "select * from expenses where year_id = " + year;
        return getExpensesDataByQuery(query);
    }

    @Override
    public List<ExpensesData> findByDescription(String description, String start, String end) {
        checkDateFormat(start);
        checkDateFormat(end);
        String query = "select * from expenses where lower(description) like '%" + description.toLowerCase() + "%'"
                + " and update_date > '" + start + "'"
                + " and update_date < '" + end + "'";
        return getExpensesDataByQuery(query);
    }

    @Override
    public List<ExpensesData> findByTypeMonthYear(int type, int month, int year) {
        String query =
                "select * from expenses where month_id = " + month + " and year_id = " + year + " and operation_type_id = " + type;
        return getExpensesDataByQuery(query);
    }

    @Override
    public List<ExpensesData> findByTypeYear(int type, int year) {
        String query = "select * from expenses where year_id = " + year + " and operation_type_id = " + type;
        return getExpensesDataByQuery(query);
    }

    private long getMaxIdByNumerator(int id) {
        String query = "select * from numerator where id = " + id;
        try(ResultSet rs = dbUtil.getQueryResult(query)) {
            if (rs.next()) {
                return rs.getLong("current_value") - 1; // current_value is value for nextId
            } else {
                throw new RuntimeException("No such numerator with id = " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<ExpensesData> getExpensesDataByQuery(String query) {

        List<ExpensesData> result = new LinkedList<>();

        try(ResultSet rs = dbUtil.getQueryResult(query)) {
            while (rs.next()){
                long id = rs.getLong("id");
                int month_id = rs.getInt("month_id");
                int year_id = rs.getInt("year_id");
                int expensesType = rs.getInt("operation_type_id");
                String description = rs.getString("description");
                double amount = rs.getDouble("amount");

                Date date = rs.getDate("update_date");
                Calendar calendar = new GregorianCalendar();
                calendar.setTimeInMillis(date.getTime());
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                LocalDate localDate = LocalDate.of(year, month, day);

                result.add(ExpensesData.of(id, month_id, year_id, expensesType, description, amount, localDate));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return result;
    }
}
