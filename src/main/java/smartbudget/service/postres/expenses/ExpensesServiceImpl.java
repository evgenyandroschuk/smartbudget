package smartbudget.service.postres.expenses;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import smartbudget.model.expenses.ExpensesType;
import smartbudget.service.postres.AbstractDao;

import java.util.LinkedList;
import java.util.List;

public class ExpensesServiceImpl extends AbstractDao implements ExpensesService {

    public ExpensesServiceImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
    }

    @Override
    public List<ExpensesType> getExpensesTypes() {
        String query = "select id, user_id, expenses_type_id, description, is_income from t_expenses_type";
        return namedParameterJdbcTemplate.query(query, rs -> {
            List<ExpensesType> expensesTypes = new LinkedList<>();
            while (rs.next()) {
                ExpensesType type = new ExpensesType(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("expenses_type_id"),
                    rs.getString("description"),
                    rs.getBoolean("is_income")
                );
                expensesTypes.add(type);
            }
            return expensesTypes;
        });
    }
}
