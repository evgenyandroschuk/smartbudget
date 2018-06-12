package smartbudget.service.impl;

import org.springframework.stereotype.Service;
import smartbudget.db.DbUtil;
import smartbudget.util.AppProperties;

public abstract class AbstractService {

    protected DbUtil dbUtil;

    public AbstractService(AppProperties properties) {
        dbUtil = new DbUtil(properties);
    }

}
