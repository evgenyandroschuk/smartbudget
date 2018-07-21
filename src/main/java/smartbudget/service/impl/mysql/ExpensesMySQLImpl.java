package smartbudget.service.impl.mysql;

import smartbudget.model.ExpensesData;
import smartbudget.service.ExpensesService;
import smartbudget.service.impl.AbstractService;

import java.sql.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;
import java.util.regex.Pattern;

public class ExpensesMySQLImpl extends AbstractService implements ExpensesService {

    private String dateRegEx = "^[0-9]{4}-[0-9]{1}[0-9]{1}-[0-3]{1}[0-9]{1}$";

    public ExpensesMySQLImpl(Connection connection) throws SQLException {
        super(connection);
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
        String amountString = Double.toString(amount);

        String updateDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String query =
                "insert into expenses ( id, description, month_id, year_id, operation_type_id, update_date, amount)"
                 + "values(get_id(1), '" + description + "', " + month + ", "+year+ ", "
                        + type + ", '" + updateDate + "', " + amountString + " )";
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
           throw new RuntimeException(e);
        }
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

        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        String query = "delete from expenses where id = " + id;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExpensesData findById(Long id) throws SQLException {
        String query = "select * from expenses where id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, id);
        return getExpensesByPreparedStatement(statement).get(0);
    }

    @Override
    public List<ExpensesData> findByMonthYear(int month, int year) throws SQLException {
        String query = "select * from expenses where month_id = ? and year_id = ? order by id desc";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, month);
        statement.setInt(2, year);
        return getExpensesByPreparedStatement(statement);
    }

    @Override
    public List<ExpensesData> findByYear(int year) throws SQLException {
        String query = "select * from expenses where year_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, year);
        return getExpensesByPreparedStatement(statement);
    }

    @Override
    public List<ExpensesData> findByDescription(String description, String start, String end) throws SQLException {
        checkDateFormat(start);
        checkDateFormat(end);
        String query = "select * from expenses where lower(description) like ?"
                + " and update_date > ?"
                + " and update_date < ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, "%" + description.toLowerCase() + "%");
        statement.setString(2, start);
        statement.setString(3, end);
        return getExpensesByPreparedStatement(statement);
    }

    @Override
    public List<ExpensesData> findByTypeMonthYear(int type, int month, int year) throws SQLException {
        String query =
                "select * from expenses where month_id = ? and year_id = ? and operation_type_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, month);
        statement.setInt(2, year);
        statement.setInt(3, type);
        return getExpensesByPreparedStatement(statement);
    }

    @Override
    public List<ExpensesData> findByTypeYear(int type, int year) throws SQLException {
        String query = "select * from expenses where year_id = ? and operation_type_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, year);
        statement.setInt(2, type);
        return getExpensesByPreparedStatement(statement);
    }

    private long getMaxIdByNumerator(int id) {
        String query = "select * from numerator where id = ?" ;
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                return rs.getLong("current_value") - 1; // current_value is value for nextId
            } else {
                throw new RuntimeException("No such numerator with id = " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<ExpensesData> getExpensesByPreparedStatement(PreparedStatement statement) {
        List<ExpensesData> result = new LinkedList<>();

        try ( ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
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
