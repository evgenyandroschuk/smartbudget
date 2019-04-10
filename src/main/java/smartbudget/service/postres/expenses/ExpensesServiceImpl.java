package smartbudget.service.postres.expenses;

import com.google.common.collect.ImmutableMap;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import smartbudget.model.expenses.ExpensesType;
import smartbudget.service.postres.AbstractDao;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ExpensesServiceImpl extends AbstractDao implements ExpensesService {

    public ExpensesServiceImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
    }

    @Override
    public List<ExpensesType> getExpensesTypes(int userId) {
        String query = "select id, user_id, expenses_type_id, description, is_income " +
            "from t_expenses_type where user_id = :userId";
        Map<String, Object> params = ImmutableMap.of("userId", userId);
        return namedParameterJdbcTemplate.query(query, params, rs -> {
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
