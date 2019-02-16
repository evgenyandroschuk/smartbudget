package smartbudget.service.impl.mysql;

import smartbudget.model.ExpensesData;
import smartbudget.service.ExpensesService;
import smartbudget.service.impl.AbstractService;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;
import java.util.regex.Pattern;

public class ExpensesMySQLImpl extends AbstractService implements ExpensesService {

    private String dateRegEx = "^[0-9]{4}-[0-9]{1}[0-9]{1}-[0-3]{1}[0-9]{1}$";

    public ExpensesMySQLImpl(Connection connection) {
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
        String updateDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String query =
                "insert into expenses (id, description, month_id, year_id, operation_type_id, update_date, amount)\n" +
                        "values(get_id(1), ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, description);
            preparedStatement.setInt(2, month);
            preparedStatement.setInt(3, year);
            preparedStatement.setInt(4, type);
            preparedStatement.setString(5, updateDate);
            preparedStatement.setDouble(6, amount);
            preparedStatement.execute();
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
        String updateDate = expensesData.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String query =
                "update expenses \n" +
                        "set description = ?,\n" +
                        "month_id = ?,\n" +
                        "year_id = ?,\n" +
                        "operation_type_id = ?,\n" +
                        "update_date = ?,\n" +
                        "amount = ?\n" +
                        "where id = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, description);
            preparedStatement.setInt(2, month);
            preparedStatement.setInt(3, year);
            preparedStatement.setInt(4, type);
            preparedStatement.setString(5, updateDate);
            preparedStatement.setDouble(6, amount);
            preparedStatement.setLong(7, id);
            if (!preparedStatement.execute()) {
                throw new RuntimeException("Expenses does not updated!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        String query = "delete from expenses where id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
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

    @Override
    public Map<String, String> getStatisticByYear(int year) {
        String query = "select sum(amount) as amount from expenses, t_operation_type \n" +
            "where expenses.operation_type_id = t_operation_type.id\n" +
            "and is_income = 0 and year_id = ?";
        Map<String, String> resultMap = new HashMap<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, year);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.first()) {
                int i = 0;
                while(i < rs.getMetaData().getColumnCount()) {
                    i++;
                    resultMap.put(rs.getMetaData().getColumnName(i), rs.getString(i));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultMap;
    }

    @Override
    public Map<String, String> getStatisticByYearMonth(int year, int month) {
        String query = "select sum(amount) as amount from expenses, t_operation_type \n" +
                "where expenses.operation_type_id = t_operation_type.id\n" +
                "and is_income = 0 and year_id = ? and month_id = ?";
        Map<String, String> resultMap = new HashMap<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, month);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.first()) {
                int i = 0;
                while(i < rs.getMetaData().getColumnCount()) {
                    i++;
                    resultMap.put(rs.getMetaData().getColumnName(i), rs.getString(i));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultMap;
    }

    @Override
    public double getLastPeriodExpenses(int startId) {
        double result = 0;
        String query = "select sum(amount) expenses_amount from expenses e\n" +
                "join t_operation_type t on t.id = e.operation_type_id \n" +
                "where e.id > ? and t.is_income = 0";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, startId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                result = rs.getDouble("expenses_amount");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public double getIncomeAmount(int startId) {
        double result = 0;
        String query = "select sum(amount) income_amount from expenses e " +
                "join t_operation_type t on t.id = e.operation_type_id\n" +
                " where e.id > ?  and t.is_income = 1";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, startId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                result = rs.getDouble("income_amount");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public Map<String, Double> getCurrencies() {
        String query = "select * from currency";
        Map<String, Double> currencyMap = new HashMap<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String currencyName = rs.getString("description");
                double price = rs.getDouble("price");
                currencyMap.put(currencyName, price);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currencyMap;
    }

    @Override
    public double getFundAmount(int fundId, int currencyId) {
        String query = "select sum(amount) amount from fund where id > ? and currency_id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, fundId);
            preparedStatement.setInt(2, currencyId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() ? resultSet.getDouble("amount") : 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Map<String, String>> getYearlyExpenses(int year) {
        String query = "select expenses.id, month_id, year_id, is_income, t_operation_type.description, amount " +
                "from expenses, t_operation_type \n" +
                "where expenses.operation_type_id = t_operation_type.id\n" +
                "and year_id = ?";
        List<Map<String, String>> resultList = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, year);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                Map<String, String> map = new HashMap<>();
                map.put("id", rs.getString("id"));
                map.put("month_id", rs.getString("month_id"));
                map.put("year_id", rs.getString("year_id"));
                map.put("description", rs.getString("description"));
                map.put("amount", rs.getString("amount"));
                resultList.add(map);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }

    @Override
    public List<Map<String, String>> getFunds() {
        String query = "select id, update_date, currency_id, amount, description, sale_price from fund order by id";
        List<Map<String, String>> resultList = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("id", rs.getString("id"));
                    map.put("update_date", rs.getString("update_date"));
                    map.put("currency_id", rs.getString("currency_id"));
                    map.put("amount", rs.getString("amount"));
                    map.put("description", rs.getString("description"));
                    map.put("sale_price", rs.getString("sale_price"));
                    resultList.add(map);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        return resultList;
    }

    @Override
    public void saveFund(int currency, String description, double price, double amount) {
        String query = "insert into fund (id, update_date, currency_id, amount, description, sale_price)\n" +
                "values(get_id(5), sysdate(), ?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, currency);
            preparedStatement.setDouble(2, amount);
            preparedStatement.setString(3, description);
            preparedStatement.setDouble(4, price);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteFund(long id) {
        String query = "delete from fund where id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
