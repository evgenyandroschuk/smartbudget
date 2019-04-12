package smartbudget.service.impl;

import org.springframework.stereotype.Service;
import smartbudget.db.DbUtil;
import smartbudget.util.AppProperties;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;
import java.sql.Connection;

public abstract class AbstractService {

    protected Connection connection;

    public AbstractService(Connection connection) {
        this.connection = connection;
    }

    public void updateCurrency(int userId, int currencyId, BigDecimal price) {
        throw new NotImplementedException();
    }
}
