package smartbudget.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import smartbudget.Application;
import smartbudget.util.ApplicationProperties;
import smartbudget.util.ConfigProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class DbConnector {

    ApplicationProperties properties;

    public DbConnector(ApplicationProperties properties) {
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
