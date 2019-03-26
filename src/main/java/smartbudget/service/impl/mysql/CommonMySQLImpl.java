package smartbudget.service.impl.mysql;

import smartbudget.service.CommonService;
import smartbudget.service.impl.AbstractService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CommonMySQLImpl extends AbstractService implements CommonService {

    public CommonMySQLImpl(Connection connection) {
        super(connection);
    }

    @Override
    public void createOrReplaceUserParams(int userId, int paramId, BigDecimal value) {
        throw new NotImplementedException();
    }

    @Override
    public void createReplaceUserParams(int userId, int paramId, double value) {
        String query = "select * from t_user_system_params where userid = ? and system_param_id = ?";
        String execute;
        try (PreparedStatement statement = connection.prepareStatement(query)
            ) {
            statement.setInt(1, userId);
            statement.setInt(2, paramId);
            ResultSet rs = statement.executeQuery();
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
            statement = connection.prepareStatement(execute);
            statement.setDouble(1, value);
            statement.setInt(2, userId);
            statement.setInt(3, paramId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double getUserParamValue(int userId, int paramId) {
        String query = "select * from  t_user_system_params where userid = ? and system_param_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)){
             statement.setInt(1, userId);
             statement.setInt(2, paramId);
             ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getDouble("system_value");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException(String.format("No such system_param with userId = %d and param_id = %d ", userId, paramId));
    }

    @Override
    public BigDecimal getParamValue(int userId, int paramId) {
        throw new NotImplementedException();
    }

    public String getUserParamUpdateDateString(int userId, int paramId) {
        String query = "select * from  t_user_system_params where userid = ? and system_param_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)){
             statement.setInt(1, userId);
             statement.setInt(2, paramId);
             ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("update_date");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException(String.format("No such system_param with userId = %d and param_id = %d ", userId, paramId));
    }

    public long getMaxIdByNumerator(int id) {
        String query = "select * from numerator where id = ?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
                 statement.setInt(1, id);
                 ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getLong("current_value") - 1; // current_value is value for nextId
            } else {
                throw new RuntimeException("No such numerator with id = " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
    public void updateCurrency(int currencyId, BigDecimal price) {
        throw new NotImplementedException();
    }

}
