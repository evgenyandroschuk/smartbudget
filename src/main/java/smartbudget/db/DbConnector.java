package smartbudget.db;

import smartbudget.util.AppProperties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {

    AppProperties properties;

    public DbConnector(AppProperties properties) {
        this.properties = properties;
    }

    public Connection getConnection() throws SQLException {

        String url = properties.getProperty("app.db.url");
        String database = properties.getProperty("app.db.base");
        String userName = properties.getProperty("app.db.user");
        String password = properties.getProperty("app.db.pass");
        return DriverManager.getConnection(url + database, userName, password);
    }


}
