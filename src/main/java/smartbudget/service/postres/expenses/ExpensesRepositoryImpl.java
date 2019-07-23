package smartbudget.service.postres.expenses;

import com.google.common.collect.ImmutableMap;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import smartbudget.model.expenses.ExpensesData;
import smartbudget.model.expenses.ExpensesType;
import smartbudget.service.postres.AbstractDao;
import smartbudget.service.postres.DataNotFoundException;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ExpensesRepositoryImpl extends AbstractDao implements ExpensesRepository {

    public ExpensesRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
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
        return namedParameterJdbcTemplate.query(query, params, getExpensesResultSetExtractor(userId));
    }

    @Override
    public List<ExpensesData> getExpensesByYearMonth(int userId, int year, int month) {
        String query = "select id, user_id, month, year, expenses_type_id, description, amount, update_date\n" +
            "from expenses_data where user_id = :userId and year = :year and month = :month";
        Map<String, Object> params = ImmutableMap.of("userId", userId, "year", year, "month", month);
        return namedParameterJdbcTemplate.query(query, params, getExpensesResultSetExtractor(userId));
    }

    @Override
    public List<ExpensesData> getExpensesByPeriod(int userId, LocalDate startDate, LocalDate endDate) {
        String query = "select id, user_id, month, year, expenses_type_id, description, amount, update_date\n" +
            "from expenses_data where user_id = :userId and update_date >= :startDate and update_date < :endDate";
        Map<String, Object> params = ImmutableMap.of(
            "userId", userId, "startDate", Date.valueOf(startDate), "endDate", Date.valueOf(endDate)
        );
        return namedParameterJdbcTemplate.query(query, params, getExpensesResultSetExtractor(userId));
    }

    @Override
    public List<ExpensesData> getExpensesSinceId(int userId, int startId) {
        String query = "select id, user_id, month, year, expenses_type_id, description, amount, update_date\n" +
            "from expenses_data where user_id = :userId and id > :startId";
        Map<String, Object> params = ImmutableMap.of("userId", userId, "startId", startId);
        return namedParameterJdbcTemplate.query(query, params, getExpensesResultSetExtractor(userId));
    }

    @Override
    public List<ExpensesData> getExpensesDataById(int userId, long id) {
        String query = "select id, user_id, month, year, expenses_type_id, description, amount, update_date\n" +
            "from expenses_data where user_id = :userId and id = :id";
        Map<String, Object> params = ImmutableMap.of("userId", userId, "id", id);
        return namedParameterJdbcTemplate.query(query, params, getExpensesResultSetExtractor(userId));
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

    @Override
    public Map<Integer, BigDecimal> getCurrencyPrice() {
        String query = "select id, price from t_currency";
        return namedParameterJdbcTemplate.query(query, rs -> {
            Map<Integer, BigDecimal> result = new HashMap<>();
            while (rs.next()) {
                result.put(rs.getInt("id"), rs.getBigDecimal("price"));
            }
            return result;
        });
    }

    @Override
    public long getLastExpensesId(int userId) {
        String query = "select max(id) from expenses_data where user_id = :userId";
        Map<String, Object> params = ImmutableMap.of("userId", userId);
        return namedParameterJdbcTemplate.query(query, params, rs -> rs.next() ? rs.getLong("id") : 0L);
    }

    private ResultSetExtractor<List<ExpensesData>> getExpensesResultSetExtractor(int userId) {
        List<ExpensesType> expensesTypes = getExpensesTypes(userId);
        return rs -> {
            List<ExpensesData> expensesDataList = new LinkedList<>();
            while(rs.next()) {
                int typeId = rs.getInt("expenses_type_id");
                ExpensesType expensesType = expensesTypes.stream().filter(t -> t.getId() == typeId).findFirst()
                    .orElseThrow( () -> new DataNotFoundException("expensesType not found by id = " + typeId));
                ExpensesData data = new ExpensesData(
                    rs.getLong("id"),
                    rs.getInt("year"),
                    rs.getInt("month"),
                    rs.getInt("year"),
                    expensesType,
                    rs.getString("description"),
                    rs.getBigDecimal("amount"),
                    rs.getDate("update_date").toLocalDate()
                );
                expensesDataList.add(data);
            }
            return expensesDataList;
        };
    }

    public void saveExpenses(ExpensesData data) {
        String query = "insert  into expenses_data(id, user_id, month, year, expenses_type_id, description, amount, update_date )\n" +
            "values(nextval('expenses_seq'), :userId, :month, :year, :type, :description, :amount, now())";

        Map<String, Object> params = new HashMap<>();
        params.put("userId", data.getUserId());
        params.put("month", data.getMonth());
        params.put("year", data.getYear());
        params.put("type", data.getExpensesType().getId());
        params.put("description", data.getDescription());
        params.put("amount", data.getAmount());

        namedParameterJdbcTemplate.execute(query, params, PreparedStatement::execute);
    }
}
