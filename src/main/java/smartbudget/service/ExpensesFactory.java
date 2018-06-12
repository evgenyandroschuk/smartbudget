package smartbudget.service;

import org.springframework.stereotype.Service;
import smartbudget.service.impl.mysql.ExpensesMySQLImpl;
import smartbudget.service.impl.mysql.ExpensesTypeMySQLImpl;
import smartbudget.util.AppProperties;

public class ExpensesFactory {

    private String name;
    private AppProperties properties;

    public ExpensesFactory(AppProperties properties, String name) {
        if (!name.equals("mysql")) {
            throw throwException(name);
        }
        this.properties = properties;
        this.name = name;
    }


    public ExpensesService getExpensesService() {
        if (name.equals("mysql")) {
            return new ExpensesMySQLImpl(properties);
        }
        throw throwException(name);

    }

    public ExpensesTypeService getExpensesTypeService() {
        if (name.equals("mysql")) {
            return new ExpensesTypeMySQLImpl(properties);
        }
        throw throwException(name);
    }

    private static IllegalArgumentException throwException (String name) {
        throw  new IllegalArgumentException("No such factory with name = " + name);
    }
}
