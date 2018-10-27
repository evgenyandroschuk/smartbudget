package smartbudget.service;

import smartbudget.service.impl.mysql.CommonMySQLImpl;
import smartbudget.service.impl.mysql.ExpensesMySQLImpl;
import smartbudget.service.impl.mysql.ExpensesTypeMySQLImpl;
import smartbudget.service.impl.mysql.VehicleMySQLImpl;

import java.sql.Connection;
import java.sql.SQLException;

public class DbServiceFactory {

    private String name;
    Connection connection;

    public DbServiceFactory(String name, Connection connection) {
        if (!name.equals("mysql")) {
            throw throwException(name);
        }
        this.name = name;
        this.connection = connection;
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
        if(name.equals("mysql")) {
            return new VehicleMySQLImpl(connection) {
            };
        }
        throw throwException(name);
    }

    private static IllegalArgumentException throwException (String name) {
        throw new IllegalArgumentException("No such factory with name = " + name);
    }
}