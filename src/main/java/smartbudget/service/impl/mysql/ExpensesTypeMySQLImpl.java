package smartbudget.service.impl.mysql;

import smartbudget.model.ExpensesTypeData;
import smartbudget.service.ExpensesTypeService;
import smartbudget.service.impl.AbstractService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class ExpensesTypeMySQLImpl extends AbstractService implements ExpensesTypeService {
    public ExpensesTypeMySQLImpl(Connection connection) {
        super(connection);
    }

    @Override
    public void save(ExpensesTypeData expensesTypeData) {

        int id = expensesTypeData.getId();
        String description = expensesTypeData.getDesc();
        String descriptionRus = expensesTypeData.getDescRus();
        int isIncome = expensesTypeData.isIncome() ? 1 : 0;
        boolean isActive = expensesTypeData.isActive();

        String query =
                "insert into t_operation_type (id, description, description_ru, is_income, is_active ) values(" +
                        id + ", '" + description + "', '" + descriptionRus + "', " + isIncome + ", " + isActive + ")";
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(int id, ExpensesTypeData expensesTypeData) {
        String desc = expensesTypeData.getDesc();
        String descRu = expensesTypeData.getDescRus();
        int isIncome = expensesTypeData.isIncome() ? 1 : 0;
        boolean isActive = expensesTypeData.isActive();

        String query =
                "update t_operation_type set description = '" + desc + "', description_ru = '" + descRu + "', " +
                        "is_income = " + isIncome + ", is_active = " + isActive + " where id = " + id;

        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExpensesTypeData getById(int id) {
        String query = "select * from t_operation_type where id = " + id;
        List<ExpensesTypeData> dataList = getExpensesTypeByQuery(query);
        if (dataList.isEmpty()) {
            throw new RuntimeException("No expenses type with id = " + id);
        }
        return dataList.get(0);
    }

    @Override
    public List<ExpensesTypeData> getAll() {
        String query = "select * from t_operation_type";
        return getExpensesTypeByQuery(query);
    }

    @Override
    public List<ExpensesTypeData> getActiveType() {
        String query = "select * from t_operation_type where is_active = true";
        return getExpensesTypeByQuery(query);
    }

    private List<ExpensesTypeData> getExpensesTypeByQuery(String query) {

        List<ExpensesTypeData> result = new LinkedList<>();

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)){
            while (rs.next()) {
                int id = rs.getInt("id");
                String description = rs.getString("description");
                String descriptionRus = rs.getString("description_ru");
                boolean isIncome = rs.getInt("is_income") == 0 ? false : true;
                boolean isActive = rs.getBoolean("is_active");
                ExpensesTypeData data = ExpensesTypeData.of(id, description, descriptionRus, isIncome, isActive);
                result.add(data);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return result;
    }
}
