package smartbudget.client.service.postgres;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import smartbudget.model.funds.Currency;
import smartbudget.model.funds.FundData;
import smartbudget.service.postres.fund.FundDao;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FundTest {

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @InjectMocks
    private FundDao fundDao;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void resetMocks() {
        reset(namedParameterJdbcTemplate);
    }


    @Test(dataProvider = "getFundsProvider")
    public void testGetFunds(String description, LocalDate start, LocalDate end, int count) {
        String query = "select * from funds where user_id =:userId order by update_date desc";
        Map<String, Object> params = ImmutableMap.of("userId", 1);

        ImmutableList<FundData> fundList = ImmutableList.of(
            new FundData(
                1L, 1, DOLLAR_CURRENCY,
                BigDecimal.valueOf(100).setScale(2),
                BigDecimal.valueOf(77.41).setScale(2),
                "To Alpha",
                LocalDate.of(2020, 4, 22)
            )
        );
        when(namedParameterJdbcTemplate.query(
            eq(query),
            eq(params),
            ((ResultSetExtractor<List<FundData>>) any(ResultSetExtractor.class))
        )).thenReturn(fundList);

        List<FundData> result = fundDao.getFundData(1, start, end);

        Assert.assertTrue(result.size() == count);
    }

    @DataProvider
    private static Object[][] getFundsProvider() {
        return new Object[][]{
            {
                "Fund between startDate and endDate",
                LocalDate.of(2020, 4, 10),
                LocalDate.of(2020, 4, 22),
                1
            },
            {
                "Fund after endDate",
                LocalDate.of(2020, 4, 22),
                LocalDate.of(2020, 4, 30),
                0
            },
            {
                "Fund before startDate",
                LocalDate.of(2020, 3, 10),
                LocalDate.of(2020, 3, 22),
                0
            }
        };
    }

    @Test
    public void testInsertFund() {
        LocalDate updateDate = LocalDate.of(2020,4,20);
        FundData fundData = new FundData(
            null,
            1,
            EURO_CURRENCY,
            BigDecimal.valueOf(110),
            BigDecimal.valueOf(83.28),
            "euro",
            updateDate
        );
        String insert = "insert into funds(id, user_id, currency_id, amount, price, description, update_date)\n" +
            "values (nextval('funds_seq'), :userId, :currencyId, :amount, :price, :description, :updateDate)";
        Map<String, Object> params = new HashMap<>();
        params.put("userId", fundData.getUserId());
        params.put("currencyId", fundData.getCurrency().getId());
        params.put("amount", fundData.getAmount());
        params.put("description", fundData.getDescription());
        params.put("price", fundData.getPrice());
        params.put("updateDate", Date.valueOf(updateDate));
        when(namedParameterJdbcTemplate.execute(
            eq(insert), eq(params), (PreparedStatementCallback<Boolean>) any(PreparedStatementCallback.class))
        ).thenReturn(true);
        fundDao.addFund(fundData);
        verify(namedParameterJdbcTemplate)
            .execute(
                eq(insert), eq(params), (PreparedStatementCallback<Boolean>) any(PreparedStatementCallback.class)
            );
    }

    @Test
    public void testDeleteFund() {
        Map<String, Object> params = ImmutableMap.of("id", 1L);
        when(namedParameterJdbcTemplate.execute(
            eq(DELETE_QUERY), eq(params), (PreparedStatementCallback<Boolean>) any(PreparedStatementCallback.class))
        ).thenReturn(true);
        fundDao.deleteFund(1L);
        verify(namedParameterJdbcTemplate)
            .execute(
                eq(DELETE_QUERY), eq(params), (PreparedStatementCallback<Boolean>) any(PreparedStatementCallback.class)
            );
    }

    @Test(
        expectedExceptions = IllegalArgumentException.class,
        expectedExceptionsMessageRegExp = "ID cannot be null!"
    )
    public void testErrorDeleteFund() {
        fundDao.deleteFund(null);
    }

    private static final String DELETE_QUERY = "delete from funds where id = :id";


    private static final Currency DOLLAR_CURRENCY = new Currency(
        1, "Dollar",
        LocalDate.of(2020, 4, 20),
        BigDecimal.valueOf(74.01).setScale(2)
    );

    private static final Currency EURO_CURRENCY = new Currency(
        2, "EURO",
        LocalDate.of(2020, 4, 20),
        BigDecimal.valueOf(82).setScale(2)
    );

}
