package smartbudget.service.postres.expenses;

import com.google.common.collect.ImmutableMap;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import smartbudget.model.expenses.ExpensesData;
import smartbudget.model.expenses.ExpensesType;
import smartbudget.service.postres.AbstractDao;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
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

    @Override
    public List<ExpensesData> getExpensesByYear(int userId, int year) {
        String query = "select id, user_id, month, year, expenses_type_id, description, amount, update_date\n" +
            "from expenses_data where user_id = :userId and year = :year";
        Map<String, Object> params = ImmutableMap.of("userId", userId, "year", year);
        return namedParameterJdbcTemplate.query(query, params, getExpensesResultSetExtractor());
    }

    @Override
    public List<ExpensesData> getExpensesByYearMonth(int userId, int year, int month) {
        String query = "select id, user_id, month, year, expenses_type_id, description, amount, update_date\n" +
            "from expenses_data where user_id = :userId and year = :year and month = :month";
        Map<String, Object> params = ImmutableMap.of("userId", userId, "year", year, "month", month);
        return namedParameterJdbcTemplate.query(query, params, getExpensesResultSetExtractor());
    }

    @Override
    public List<ExpensesData> getExpensesByPeriod(int userId, LocalDate startDate, LocalDate endDate) {
        String query = "select id, user_id, month, year, expenses_type_id, description, amount, update_date\n" +
            "from expenses_data where user_id = :userId and update_date >= :startDate and update_date < :endDate";
        Map<String, Object> params = ImmutableMap.of(
            "userId", userId, "startDate", Date.valueOf(startDate), "endDate", Date.valueOf(endDate)
        );
        return namedParameterJdbcTemplate.query(query, params, getExpensesResultSetExtractor());
    }

    @Override
    public Map<Integer, BigDecimal> getFundState(int userId, int startId) {
        String query = "select currency_id, sum(amount) amount " +
            "from funds where id > :startId and user_id = :userId group by currency_id";
        Map<String, Object> params = ImmutableMap.of("startId", startId, "userId",  userId);

        return namedParameterJdbcTemplate.query(
            query, params,
            rs -> {
                Map<Integer, BigDecimal> fundMap = new HashMap<>();
                while(rs.next()) {
                    fundMap.put(rs.getInt("currency_id"), rs.getBigDecimal("amount"));
                }
                return fundMap;
            }
        );
    }

    private ResultSetExtractor<List<ExpensesData>> getExpensesResultSetExtractor() {
        return rs -> {
            List<ExpensesData> expensesDataList = new LinkedList<>();
            while(rs.next()) {
                ExpensesData data = new ExpensesData(
                    rs.getLong("id"),
                    rs.getInt("year"),
                    rs.getInt("month"),
                    rs.getInt("year"),
                    rs.getInt("expenses_type_id"),
                    rs.getString("description"),
                    rs.getBigDecimal("amount"),
                    rs.getDate("update_date").toLocalDate()
                );
                expensesDataList.add(data);
            }
            return expensesDataList;
        };
    }
}
