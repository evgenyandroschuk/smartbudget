package smartbudget.service;

import org.springframework.stereotype.Service;
import smartbudget.db.DbConnector;
import smartbudget.db.DbUtil;
import smartbudget.model.ExpensesType;
import smartbudget.util.AppProperties;
import smartbudget.util.ApplicationProperties;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

@Service
public class ApplicationProjectService {

    private AppProperties properties;

    public List<ExpensesType> getExpensesType()  {

        List<ExpensesType> types = new LinkedList<>();

        DbUtil dbUtil = new DbUtil(properties);
        try(ResultSet resultSet = dbUtil.getQueryResult("select * from t_operation_type")) {
            while (resultSet.next()) {
                ExpensesType et = new ExpensesType();
                et.setId(resultSet.getInt("id"));
                et.setDesc(resultSet.getString("description"));
                et.setDescRus(resultSet.getString("description_ru"));
                et.setIsIncome(resultSet.getBoolean("is_income"));
                types.add(et);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }

        return types;

    }

    public void setProperties(AppProperties properties) {
        this.properties = properties;
    }
}
