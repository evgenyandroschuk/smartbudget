package smartbudget.client.service.postgres;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;
import smartbudget.service.CommonService;
import smartbudget.service.postres.DbConfig;
import smartbudget.service.postres.PostgreSqlConfig;

import java.math.BigDecimal;

@SpringBootTest(classes = {
    PostgreSqlConfig.class,
    DbConfig.class
})
@Test(groups = "manual")
public class CommonServiceManualTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private CommonService commonService;

    @Test
    public void testParamValue() {
        BigDecimal result = commonService.getParamValue(1, 1);
        Assert.assertNotNull(result); // If no rows it throw DataNotFoundException
    }


}
