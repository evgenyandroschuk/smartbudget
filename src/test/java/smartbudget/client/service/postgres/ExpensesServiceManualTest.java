package smartbudget.client.service.postgres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;
import smartbudget.model.expenses.ExpensesData;
import smartbudget.model.expenses.ExpensesType;
import smartbudget.service.postres.DbConfig;
import smartbudget.service.postres.PostgreSqlConfig;
import smartbudget.service.postres.expenses.ExpensesService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = {
    PostgreSqlConfig.class,
    DbConfig.class,
    PostgreSqlConfig.class
})
@Test(groups = "manual")
public class ExpensesServiceManualTest extends AbstractTestNGSpringContextTests {

    @Autowired
    ExpensesService expensesService;

    public void testExpensesServiceTypes() {
        List<ExpensesType> result = expensesService.getExpensesTypes(USER_ID);
        System.out.println(result);
        Assert.assertEquals(result.get(0).getDescription(), "Others");
    }

    @Test
    public void testExpensesByYear() {
        List<ExpensesData> result = expensesService.getExpensesByYear(USER_ID, YEAR);
        System.out.println(result);
        Assert.assertEquals(
            result.stream().filter(t -> t.getId() == 1L).findFirst().get().getDescription(),
            "Test expenses description"
        );
    }

    @Test
    public void testExpensesByYearMonth() {
        List<ExpensesData> result = expensesService.getExpensesByYearMonth(USER_ID, YEAR, MONTH);
        Assert.assertEquals(
            result.stream().filter(t -> t.getId() == 1L).findFirst().get().getDescription(),
            "Test expenses description"
        );
        System.out.println(result);
    }

    public void testExpensesByPeriod() {
        LocalDate startDate = LocalDate.of(2019, 1, 1);
        LocalDate endDate = LocalDate.of(2020, 1, 1);
        List<ExpensesData> result = expensesService.getExpensesByPeriod(USER_ID, startDate, endDate);

        System.out.println("result = " + result);

        Assert.assertEquals(
            result.stream().filter(t -> t.getId() == 1L).findFirst().get().getDescription(),
            "Test expenses description"
        );
    }

    public void testFundState() {
        Map<Integer, BigDecimal> result = expensesService.getFundState(1, 0);
        System.out.println("FundState: " + result);
        Assert.assertTrue(!result.isEmpty());
    }


    private static final int USER_ID = 1;
    private static final int YEAR = 2019;
    private static final int MONTH = 1;

}
