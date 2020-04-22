package smartbudget.client.service.postgres;

import com.google.common.collect.ImmutableMap;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import smartbudget.model.funds.Currency;
import smartbudget.service.postres.fund.CurrencyDao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class CurrencyTest {

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @InjectMocks
    private CurrencyDao currencyDao;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void resetMocks() {
        reset(namedParameterJdbcTemplate);
    }

    @Test
    public void testGetCurrency() {
        String query = "select * from t_currency where id = :id";
        Map<String, Object> params = ImmutableMap.of("id", 1);
        Currency dollar = new Currency(
            1, "Dollar",
            LocalDate.of(2020, 4, 20),
            BigDecimal.valueOf(74.01).setScale(2)
        );
        when(namedParameterJdbcTemplate.query(
            eq(query),
            eq(params),
            ((ResultSetExtractor<Optional<Currency>>) any(ResultSetExtractor.class))
        )).thenReturn(Optional.of(dollar));

        Currency expected = currencyDao.getCurrencyById(1);
        Assert.assertEquals("Dollar", expected.getDescription());
        Mockito.verify(namedParameterJdbcTemplate).query(
            eq(query),
            eq(params),
            ((ResultSetExtractor<Optional<Currency>>) any(ResultSetExtractor.class)));
    }

    @Test
    public void testUpdateCurrency() {
        Currency currency = new Currency(
            1, "Dollar",
            LocalDate.of(2020, 4, 20),
            BigDecimal.valueOf(74.01).setScale(2)
        );
        String update = "update t_currency \n" +
            "set description = :description, update_date = now(), price = :price \n" +
            "where id = :id";
        Map<String, Object> params = ImmutableMap.of(
            "id", currency.getId(),
            "description", currency.getDescription(),
            "price", currency.getPrice()
        );
        currencyDao.setCurrency(currency);
        when(namedParameterJdbcTemplate.execute(
            eq(update), eq(params), (PreparedStatementCallback<Boolean>) any(PreparedStatementCallback.class))
        ).thenReturn(true);
    }

}
