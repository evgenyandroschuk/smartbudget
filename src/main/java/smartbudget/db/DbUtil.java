package smartbudget.db;

import smartbudget.util.AppProperties;
import smartbudget.util.ApplicationProperties;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbUtil {

    AppProperties properties;

    public DbUtil(AppProperties properties) {
        this.properties = properties;
    }

    public ResultSet getQueryResult(String query) throws SQLException {
        Connection connection = new DbConnector(properties).getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet;

    }

    public void executeQuery(String query) {

        try (Connection connection = new DbConnector(properties).getConnection();
                Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }

    }
}
