package smartbudget.client.service.postgres.expenses;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.ObjectUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import smartbudget.model.expenses.ExpensesData;
import smartbudget.model.expenses.ExpensesType;
import smartbudget.service.postres.expenses.ExpensesRepositoryImpl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class ExpensesRepositoryTest {

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @InjectMocks
    private ExpensesRepositoryImpl expensesService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void resetMocks() {
        expensesService = null;
    }

    @Test
    public void testGetExpensesTypes() {
        String query = "select id, user_id, expenses_type_id, description, is_income " +
            "from t_expenses_type where user_id = :userId";

        Map<String, Object> params = ImmutableMap.of("userId", USER_ID);

        List<ExpensesType> expensesTypes = ImmutableList.of(
            new ExpensesType(1,1,1, "Others", false),
            new ExpensesType(1,2,1, "Health", false)
        );
        when(namedParameterJdbcTemplate.query(
            eq(query),
            eq(params),
            (ResultSetExtractor<List<ExpensesType>>) any(ResultSetExtractor.class))
        ).thenReturn(expensesTypes);

        List<ExpensesType> result = expensesService.getExpensesTypes(USER_ID);
        Assert.assertEquals(result.get(1).getDescription(), "Health");
    }

    @Test
    public void testGetExpensesByYear() {
        String query = "select id, user_id, month, year, expenses_type_id, description, amount, update_date\n" +
            "from expenses_data where user_id = :userId and year = :year";
        Map<String, Object> params = ImmutableMap.of("userId", USER_ID, "year", YEAR);

        when(namedParameterJdbcTemplate.query(
            eq(query),
            eq(params),
            (ResultSetExtractor<List<ExpensesData>>) any(ResultSetExtractor.class)
        )).thenReturn(getDefaultExpensesDataList());

        List<ExpensesData> result = expensesService.getExpensesByYear(USER_ID, YEAR);
        verify(namedParameterJdbcTemplate).query(
            eq(query),
            eq(params),
            (ResultSetExtractor<List<ExpensesData>>) any(ResultSetExtractor.class));
        Assert.assertEquals(result.get(1).getDescription(), "TestDescription 02");
    }

    @Test
    public void testGetExpensesByYearMonth() {
        String query = "select id, user_id, month, year, expenses_type_id, description, amount, update_date\n" +
            "from expenses_data where user_id = :userId and year = :year and month = :month";
        Map<String, Object> params = ImmutableMap.of("userId", USER_ID, "year", YEAR, "month", MONTH);

        when(namedParameterJdbcTemplate.query(
            eq(query),
            eq(params),
            (ResultSetExtractor<List<ExpensesData>>) any(ResultSetExtractor.class)
        )).thenReturn(getDefaultExpensesDataList());

        List<ExpensesData> result = expensesService.getExpensesByYearMonth(USER_ID, YEAR, MONTH);

        verify(namedParameterJdbcTemplate).query(
            eq(query),
            eq(params),
            (ResultSetExtractor<List<ExpensesData>>) any(ResultSetExtractor.class));
        Assert.assertEquals(result.get(1).getDescription(), "TestDescription 02");
    }

    @Test
    public void testGetExpensesByPeriod() {
        LocalDate startDate = LocalDate.of(YEAR, MONTH, 1);
        LocalDate endDate = LocalDate.of(YEAR, MONTH, 30);
        String query = "select id, user_id, month, year, expenses_type_id, description, amount, update_date\n" +
            "from expenses_data where user_id = :userId and update_date >= :startDate and update_date < :endDate";
        Map<String, Object> params = ImmutableMap.of(
            "userId", USER_ID, "startDate", Date.valueOf(startDate), "endDate", Date.valueOf(endDate)
        );

        when(namedParameterJdbcTemplate.query(
            eq(query),
            eq(params),
            (ResultSetExtractor<List<ExpensesData>>) any(ResultSetExtractor.class)
        )).thenReturn(getDefaultExpensesDataList());

        List<ExpensesData> result = expensesService.getExpensesByPeriod(USER_ID, startDate, endDate);

        Assert.assertEquals(result.get(1).getDescription(), "TestDescription 02");
        verify(namedParameterJdbcTemplate).query(
            eq(query),
            eq(params),
            (ResultSetExtractor<List<ExpensesData>>) any(ResultSetExtractor.class)
        );
    }

    @Test
    public void testGetExpensesSinceId() {
        String query = "select id, user_id, month, year, expenses_type_id, description, amount, update_date\n" +
            "from expenses_data where user_id = :userId and id > :startId";
        Map<String, Object> params = ImmutableMap.of("userId", USER_ID, "startId", 1);

        when(namedParameterJdbcTemplate.query(
            eq(query),
            eq(params),
            (ResultSetExtractor<List<ExpensesData>>) any(ResultSetExtractor.class)
        )).thenReturn(getDefaultExpensesDataList());

        List<ExpensesData> result = expensesService.getExpensesSinceId(USER_ID, 1);

        Assert.assertEquals(result.get(1).getDescription(), "TestDescription 02");
        verify(namedParameterJdbcTemplate).query(
            eq(query),
            eq(params),
            (ResultSetExtractor<List<ExpensesData>>) any(ResultSetExtractor.class)
        );
    }

    @Test
    public void testFundState() {
        String query = "select currency_id, sum(amount) amount " +
            "from funds where id > :startId and user_id = :userId group by currency_id";
        Map<String, Object> params = ImmutableMap.of("startId", 0, "userId",  USER_ID);

        Map<Integer, BigDecimal> fundMap = ImmutableMap.of(
            1, BigDecimal.valueOf(100.00),
            2, BigDecimal.valueOf(130.10)
        );

        when(namedParameterJdbcTemplate.query(
            eq(query), eq(params),
            (ResultSetExtractor<Map<Integer, BigDecimal>>) any(ResultSetExtractor.class))
        ).thenReturn(fundMap);

        Map<Integer, BigDecimal> result = expensesService.getFundState(USER_ID, 0);

        Assert.assertEquals(result, fundMap);
        verify(namedParameterJdbcTemplate).query(
            eq(query), eq(params),
            (ResultSetExtractor<Map<Integer, BigDecimal>>) any(ResultSetExtractor.class));
    }

    @Test
    public void testGetCurrencyPrice() {
        String query = "select id, price from t_currency";
        Map<Integer, BigDecimal> currencies =   ImmutableMap.of(
            1, BigDecimal.valueOf(65.02),
            2, BigDecimal.valueOf(73.05),
            3, BigDecimal.valueOf(1)
        );

        when(namedParameterJdbcTemplate.query(
            eq(query),
            (ResultSetExtractor<Map<Integer, BigDecimal>>) any(ResultSetExtractor.class)
            )).thenReturn(currencies);

        Map<Integer, BigDecimal> result = expensesService.getCurrencyPrice();

        Assert.assertEquals(result.get(1), currencies.get(1));
        verify(namedParameterJdbcTemplate).query(
            eq(query),
            (ResultSetExtractor<Map<Integer, BigDecimal>>) any(ResultSetExtractor.class)
        );
    }

    @Test
    public void testGetExpensesDataById() {
        long id = 20;
        String query = "select id, user_id, month, year, expenses_type_id, description, amount, update_date\n" +
            "from expenses_data where user_id = :userId and id = :id";
        Map<String, Object> params = ImmutableMap.of("userId", USER_ID, "id", id);
        when(namedParameterJdbcTemplate.query(
            eq(query),
            eq(params),
            (ResultSetExtractor<List<ExpensesData>>) any(ResultSetExtractor.class)
        )).thenReturn(getDefaultExpensesDataList().stream().filter(t -> t.getId().equals(1L)).collect(Collectors.toList()));

        List<ExpensesData> result = expensesService.getExpensesDataById(USER_ID, id);

        Assert.assertEquals(result.size(), 1);

        verify(namedParameterJdbcTemplate).query(
            eq(query),
            eq(params),
            (ResultSetExtractor<List<ExpensesData>>) any(ResultSetExtractor.class)
        );

    }

    @Test
    public void testSaveExpenses() {
        ExpensesData data = getDefaultExpensesDataList().get(0);
        String query = "insert  into expenses_data(id, user_id, month, year, expenses_type_id, description, amount, update_date )\n" +
            "values(nextval('expenses_seq'), :userId, :month, :year, :type, :description, :amount, now())";

        Map<String, Object> params = new HashMap<>();
        params.put("userId", data.getUserId());
        params.put("month", data.getMonth());
        params.put("year", data.getYear());
        params.put("type", data.getExpensesType().getId());
        params.put("description", data.getDescription());
        params.put("amount", data.getAmount());

        when(namedParameterJdbcTemplate.execute(
            eq(query), eq(params), (PreparedStatementCallback<Boolean>) any(PreparedStatementCallback.class))
        ).thenReturn(true);

        expensesService.saveExpenses(data);

        verify(namedParameterJdbcTemplate).execute(eq(query), eq(params), (PreparedStatementCallback<Boolean>) any(PreparedStatementCallback.class));
    }

    @Test
    public void testLastExpensesId() {
        String query = "select max(id) from expenses_data where user_id = :userId";
        Map<String, Object> params = ImmutableMap.of("userId", USER_ID);
        when(namedParameterJdbcTemplate.query(
            eq(query), eq(params), (ResultSetExtractor<Long>) any(ResultSetExtractor.class))
        ).thenReturn(1200L);

        long lastId = expensesService.getLastExpensesId(USER_ID);
        Assert.assertEquals(lastId, 1200L);

        verify(namedParameterJdbcTemplate).query(
            eq(query), eq(params), (ResultSetExtractor<Long>) any(ResultSetExtractor.class));
    }

    private List<ExpensesData> getDefaultExpensesDataList() {
        ExpensesType expensesType = new ExpensesType(1, USER_ID,1, "Others", false);
        return ImmutableList.of(
            new ExpensesData(1L, USER_ID, MONTH, YEAR, expensesType, "TestDescription 01",
                BigDecimal.valueOf(100.00), LocalDate.of(YEAR, MONTH, 1)),
            new ExpensesData(2L, USER_ID, MONTH, YEAR, expensesType, "TestDescription 02",
                BigDecimal.valueOf(110.00), LocalDate.of(YEAR, MONTH, 1))
        );
    }

    private static final int USER_ID = 1;
    private static final int YEAR = 2019;
    private static final int MONTH = 1;

}
