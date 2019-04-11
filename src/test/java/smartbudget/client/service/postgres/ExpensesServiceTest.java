package smartbudget.client.service.postgres;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import smartbudget.model.expenses.ExpensesData;
import smartbudget.model.expenses.ExpensesType;
import smartbudget.service.postres.expenses.ExpensesServiceImpl;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class ExpensesServiceTest {

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @InjectMocks
    private ExpensesServiceImpl expensesService;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void resetMocks() {
        reset(namedParameterJdbcTemplate);
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

    private List<ExpensesData> getDefaultExpensesDataList() {
        return ImmutableList.of(
            new ExpensesData(1L, USER_ID, MONTH, YEAR, 1, "TestDescription 01",
                BigDecimal.valueOf(100.00), LocalDate.of(YEAR, MONTH, 1)),
            new ExpensesData(2L, USER_ID, MONTH, YEAR, 1, "TestDescription 02",
                BigDecimal.valueOf(110.00), LocalDate.of(YEAR, MONTH, 1))
        );
    }

    private static final int USER_ID = 1;
    private static final int YEAR = 2019;
    private static final int MONTH = 1;

}
