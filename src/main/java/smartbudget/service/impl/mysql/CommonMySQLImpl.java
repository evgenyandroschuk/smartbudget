package smartbudget.service.impl.mysql;

import smartbudget.service.CommonService;
import smartbudget.service.impl.AbstractService;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CommonMySQLImpl extends AbstractService implements CommonService {

    public CommonMySQLImpl(Connection connection) {
        super(connection);
    }

    @Override
    public void createReplaceUserParams(int userId, int paramId, double value) {
        String query = "select * from t_user_system_params where userid = ? and system_param_id = ?";
        String execute;
        try (PreparedStatement statement = connection.prepareStatement(query)
            ) {
            statement.setInt(1, userId);
            statement.setInt(2, paramId);
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                execute = "update t_user_system_params set system_value = ?, update_date = sysdate() where userid = ? and system_param_id = ?";
            } else {
                execute = "insert into t_user_system_params (id, system_value, userid, system_param_id, update_date) " +
                        "values(get_id(7), ?, ?, ?, sysdate())";
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(query);
            statement.setDouble(1, value);
            statement.setInt(2, userId);
            statement.setInt(3, paramId);
            statement.execute(execute);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double getUserParamValue(int userId, int paramId) {
        String query = "select * from  t_user_system_params where userid = " + userId +" and system_param_id = " + paramId;
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            if (rs.next()) {
                return rs.getDouble("system_value");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException(String.format("No such system_param with userId = %d and param_id = %d ", userId, paramId));
    }

    public String getUserParamUpdateDate(int userId, int paramId) {
        String query = "select * from  t_user_system_params where userid = " + userId +" and system_param_id = " + paramId;
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            if (rs.next()) {
                return rs.getString("update_date");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException(String.format("No such system_param with userId = %d and param_id = %d ", userId, paramId));
    }

    public long getMaxIdByNumerator(int id) {
        String query = "select * from numerator where id = " + id;
        try(Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query)) {
            if (rs.next()) {
                return rs.getLong("current_value") - 1; // current_value is value for nextId
            } else {
                throw new RuntimeException("No such numerator with id = " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Map<String, String>> getQueryRequest(String query) {
        List<Map<String, String>> resultList = new ArrayList<>();
        try(Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query)) {
            while(rs.next()) {
                Map<String, String> map = new HashMap<>();
                int i = 0;
                while(i < rs.getMetaData().getColumnCount()) {
                    i++;
                    map.put(rs.getMetaData().getColumnName(i), rs.getString(i));
                }
                resultList.add(map);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }

    public void updateCurrencyCost(int currencyId, double price) {
        String query = "update currency set price = ? where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query))  {
            statement.setDouble(1, price);
            statement.setInt(2, currencyId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute(String query) {
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}