package smartbudget.client.service.postgres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;
import smartbudget.model.expenses.ExpensesType;
import smartbudget.service.postres.DbConfig;
import smartbudget.service.postres.PostgreSqlConfig;
import smartbudget.service.postres.expenses.ExpensesService;

import java.util.List;

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
        List<ExpensesType> result = expensesService.getExpensesTypes();
        System.out.println(result);
        Assert.assertEquals(result.get(0).getDescription(), "Others");
    }

}
