package smartbudget.service;

import org.springframework.stereotype.Service;
import smartbudget.db.DbUtil;
import smartbudget.service.impl.mysql.*;
import smartbudget.service.services.DbServiceFactory;
import smartbudget.service.services.PropertyService;
import smartbudget.util.AppProperties;

import java.sql.Connection;
import java.sql.SQLException;

@Service
public class DbServiceFactoryImpl implements DbServiceFactory {

    private String name;
    private Connection connection;


    public DbServiceFactoryImpl(AppProperties appProperties) {
        this.name = appProperties.getProperty("app.impl");
        if (!name.equals("mysql")) {
            throw throwException(name);
        }
        try {
            connection = new DbUtil(appProperties).getConnect();
        } catch (SQLException e) {
            throw new RuntimeException("Error in constructor DbServiceFactoryImpl" + e.getMessage());
        }
    }

    public ExpensesService getExpensesService() {
        if (name.equals("mysql")) {
            try {
                return new ExpensesMySQLImpl(connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        throw throwException(name);
    }

    public ExpensesTypeService getExpensesTypeService() {
        if (name.equals("mysql")) {
            return new ExpensesTypeMySQLImpl(connection);
        }
        throw throwException(name);
    }

    public CommonService getCommonService() {
        if(name.equals("mysql")) {
                return new CommonMySQLImpl(connection);
        }
        throw throwException(name);
    }

    public VehicleService getVehicleService() {
        if (name.equals("mysql")) {
            return new VehicleMySQLImpl(connection) {
            };
        }
        throw throwException(name);
    }

    public PropertyService getPropertyService() {
        if (name.equals("mysql")) {
            return new PropertyServiceMySQLImpl(connection);
        }
        throw throwException(name);
    }

    private static IllegalArgumentException throwException (String name) {
        throw new IllegalArgumentException("No such factory with name = " + name);
    }
}
