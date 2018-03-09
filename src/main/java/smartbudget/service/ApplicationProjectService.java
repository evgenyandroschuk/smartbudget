package smartbudget.service;

import org.springframework.stereotype.Service;
import smartbudget.db.DbConnector;
import smartbudget.db.DbUtil;
import smartbudget.model.Expenses;
import smartbudget.model.ExpensesType;
import smartbudget.util.AppProperties;
import smartbudget.view.json.expenses.type.ExpensesData;
import smartbudget.view.json.expenses.type.ExpensesRequest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class ApplicationProjectService {

    private AppProperties properties;

    private DbUtil dbUtil = new DbUtil();

    private List<ExpensesType> expensesTypes;

    private String dateRegEx = "^[0-9]{4}-[0-9]{1}[0-9]{1}-[0-3]{1}[0-9]{1}$";


    public void setProperties(AppProperties properties) {
        this.properties = properties;
    }

    private void checkDateFormat(String dateString) {
        if(!Pattern.matches(dateRegEx, dateString)) {
            throw new RuntimeException("date must be in YYYY-MM-DD format");
        }
    }

    public List<ExpensesType> getExpensesType()  {

        if(expensesTypes != null) {
            return expensesTypes;
        }
        List<ExpensesType> types = new LinkedList<>();

        dbUtil.setProperties(properties);
        try(ResultSet resultSet = dbUtil.getQueryResult("select * from t_operation_type")) {
            while (resultSet.next()) {
                ExpensesType et = new ExpensesType();
                et.setId(resultSet.getInt("id"));
                et.setDesc(resultSet.getString("description"));
                et.setDescRus(resultSet.getString("description_ru"));
                et.setIsIncome(resultSet.getBoolean("is_income"));
                types.add(et);
            }
            expensesTypes = types;
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
        return types;
    }

    public ExpensesType getExpensesTypeById(int id) {
        ExpensesType expensesType = null;
        for(ExpensesType et : getExpensesType()) {
            if(et.getId() == id) {
                expensesType = et;
            }
        }
        return expensesType;
    }


    public List<Expenses> getExpensesByQueryString(String query) {
        List<Expenses> expensesList = new LinkedList<>();

        dbUtil.setProperties(properties);
        try(ResultSet rs = dbUtil.getQueryResult(query)) {
            while (rs.next()) {
                Expenses expenses = new Expenses();
                expenses.setId(rs.getLong("id"));
                expenses.setDescription(rs.getString("description"));
                expenses.setMonth(rs.getInt("month_id"));
                expenses.setYear(rs.getInt("year_id"));
                int expTypeId = rs.getInt("operation_type_id");
                ExpensesType expensesType = getExpensesTypeById(expTypeId);
                expenses.setExpensesType(expensesType);
                expenses.setAmount(rs.getDouble("amount"));

                Date date = rs.getDate("update_date");
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                LocalDate localDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH+1) ,calendar.get(Calendar.DAY_OF_MONTH) );
                expenses.setDate(localDate);
                expensesList.add(expenses);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return expensesList;
    }

    public List<Expenses> getExpensesByMonthYear(int month, int year) {

        String query = "select * from expenses where month_id = " + month + " and year_id = " + year;
        return getExpensesByQueryString(query);
    }

    public List<Expenses> getExpensesByYear(int year) {
        String query = "select * from expenses where year_id = " + year;
        return getExpensesByQueryString(query);
    }

    public List<Expenses> getExpensesByTypeMonthYear(int type, int month, int year) {
        String query = "select * from expenses where month_id = " + month + " and year_id = " + year + " and operation_type_id = " + type;
        return getExpensesByQueryString(query);
    }

    public List<Expenses> getExpensesByTypeYear(int type, int year) {
        String query = "select * from expenses where year_id = " + year + " and operation_type_id = " + type;
        return getExpensesByQueryString(query);
    }

    public List<Expenses> getExpensesByTypeAndPeriod(int type, String startDate, String endDate) {

        checkDateFormat(startDate);
        checkDateFormat(endDate);

        String query = "select * from expenses where operation_type_id = " + type + " and (update_date >= '" + startDate
                + "' and update_date <= '" + endDate + "')";
        return getExpensesByQueryString(query);
    }

    public List<Expenses> getExpensesTypeByDescriptionPeriod(String description, String startDate, String endDate) {

        checkDateFormat(startDate);
        checkDateFormat(endDate);

        String desc = description.toLowerCase();
        String query = "select * from expenses where lower(description) like '%" + desc + "%' and (update_date >= '" + startDate
                + "' and update_date <= '" + endDate + "')";
        return getExpensesByQueryString(query);
    }

    public boolean addExpenses(ExpensesRequest request) {

        if(request.getExpensesData().isEmpty()) {
            return false;
        }

        List<ExpensesData> expensesDataList = request.getExpensesData();
        String updateDate = request.getDate();
        int month = request.getMonth();
        int year = request.getYear();

        try (
                Connection connection = new DbConnector(properties).getConnection();
                Statement statement = connection.createStatement();

        ) {
            expensesDataList.forEach((e) -> {

                String query = "insert into expenses ( id, description, month_id, year_id, operation_type_id, update_date, amount) "
                        + "values (get_id(1), '" + e.getDescription() + "', " + month + ", " + year
                        + ", " + e.getType() + ", '" + updateDate + "', " + e.getAmount() + ")";
                try {
                    statement.execute(query);
                } catch (SQLException e1) {
                    throw new RuntimeException("Exception from execute:" + e1);
                }

            });

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;

    }

}
