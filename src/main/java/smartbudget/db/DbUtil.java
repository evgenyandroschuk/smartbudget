package smartbudget.db;

import smartbudget.util.ApplicationProperties;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbUtil {

    ApplicationProperties properties;

    public DbUtil(ApplicationProperties properties) {
        this.properties = properties;
    }

    public ResultSet getQueryResult(String query) throws SQLException {
        Connection connection = new DbConnector(properties).getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet;

    }




}
