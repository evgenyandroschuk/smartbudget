package smartbudget.service;

import smartbudget.service.impl.mysql.CommonMySQLImpl;
import smartbudget.service.impl.mysql.ExpensesMySQLImpl;
import smartbudget.service.impl.mysql.ExpensesTypeMySQLImpl;
import smartbudget.util.AppProperties;

import java.sql.Connection;
import java.sql.SQLException;

public class ExpensesFactory {

    private String name;
    Connection connection;

    public ExpensesFactory(String name, Connection connection) {
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
                e.printStackTrace();
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

    private static IllegalArgumentException throwException (String name) {
        throw  new IllegalArgumentException("No such factory with name = " + name);
    }
}
