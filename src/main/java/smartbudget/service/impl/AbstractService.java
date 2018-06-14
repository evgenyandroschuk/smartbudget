package smartbudget.service.impl;

import org.springframework.stereotype.Service;
import smartbudget.db.DbUtil;
import smartbudget.util.AppProperties;

import java.sql.Connection;

public abstract class AbstractService {

    protected Connection connection;

    public AbstractService(Connection connection) {
        this.connection = connection;
    }

}
