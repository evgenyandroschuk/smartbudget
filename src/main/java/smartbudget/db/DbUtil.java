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

    public Connection getConnect() throws SQLException {
        return  new DbConnector(properties).getConnection();
    }
}
