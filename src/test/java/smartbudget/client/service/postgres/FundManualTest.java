package smartbudget.client.service.postgres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;
import smartbudget.model.funds.Currency;
import smartbudget.model.funds.FundData;
import smartbudget.service.postres.DbConfig;
import smartbudget.service.postres.PostgreSqlConfig;
import smartbudget.service.postres.fund.CurrencyDao;
import smartbudget.service.postres.fund.FundDao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest(classes = {
    DbConfig.class,
    PostgreSqlConfig.class,
    CurrencyDao.class,
    FundDao.class

})
@Test(groups = "manual")
public class FundManualTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private CurrencyDao currencyDao;

    @Autowired
    private FundDao fundDao;

    @Test
    public void testGetCurrency() {
        Currency dollar = currencyDao.getCurrencyById(1);
        Assert.assertEquals("Dollar", dollar.getDescription());
    }

    @Test
    public void testUpdateCurrency() {
        Currency dollar = new Currency(
            DEFAULT_USER, "Dollar",
            LocalDate.of(2020, 4, 20),
            BigDecimal.valueOf(77.41).setScale(2)
        );
        currencyDao.setCurrency(dollar);
    }

    @Test
    public void testGetFund() {
        List<FundData> fundData = fundDao.getFundData(DEFAULT_USER,
            LocalDate.of(2020, 4, 19),
            LocalDate.of(2020, 4, 23));
        Assert.assertFalse(fundData.isEmpty());
        System.out.println(fundData);
    }

    @Test
    public void testSetAndDeleteFund() {
        String testDescription = "Test set euro";

        System.out.println("Check that test fund doesn't exist...");
        boolean exists = fundDao
            .getFundData(DEFAULT_USER, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1))
            .stream().anyMatch(t -> t.getDescription().equals(testDescription));
        Assert.assertFalse(exists);

        FundData fundData = new FundData(
            DEFAULT_USER,
            currencyDao.getCurrencyById(2),
            BigDecimal.valueOf(110),
            BigDecimal.valueOf(83.28),
            testDescription
        );
        System.out.println("Trying to add fund...");
        fundDao.addFund(fundData);
        FundData result = fundDao
            .getFundData(DEFAULT_USER, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1))
            .stream().filter(t -> t.getDescription().equals(testDescription))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Not found fund with name: " + testDescription));

        Assert.assertEquals(testDescription, result.getDescription());
        System.out.println("Fund successfully created: " + result);

        System.out.println("Trying to delete fund...");
        fundDao.deleteFund(result.getId());
        exists = fundDao
            .getFundData(DEFAULT_USER, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1))
            .stream().anyMatch(t -> t.getDescription().equals(testDescription));
        Assert.assertFalse(exists);
        System.out.println("Fund successfully deleted: " + result);

    }

    public static final int DEFAULT_USER = 1;
}
