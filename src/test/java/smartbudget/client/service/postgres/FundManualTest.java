package smartbudget.client.service.postgres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;
import smartbudget.model.funds.Currency;
import smartbudget.service.postres.DbConfig;
import smartbudget.service.postres.PostgreSqlConfig;
import smartbudget.service.postres.fund.CurrencyDao;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest(classes = {
    DbConfig.class,
    PostgreSqlConfig.class,
    CurrencyDao.class

})
@Test(groups = "manual")
public class FundManualTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private CurrencyDao currencyDao;

    @Test
    public void testGetCurrency() {
        Currency dollar = currencyDao.getCurrencyById(1);
        Assert.assertEquals("Dollar", dollar.getDescription());
    }

    @Test
    public void testUpdateCurrency() {
        Currency dollar = new Currency(
            1, "Dollar",
            LocalDate.of(2020, 4, 20),
            BigDecimal.valueOf(77.41).setScale(2)
        );
        currencyDao.setCurrency(dollar);
    }
}
