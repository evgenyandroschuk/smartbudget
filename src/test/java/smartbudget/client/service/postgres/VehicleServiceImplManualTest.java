package smartbudget.client.service.postgres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;
import smartbudget.model.vehicles.VersionedVehicleServiceType;
import smartbudget.service.postres.DbConfig;
import smartbudget.service.postres.PostgreSqlConfig;
import smartbudget.service.postres.VehicleServiceVersioned;

@SpringBootTest(classes = {
    PostgreSqlConfig.class,
    DbConfig.class
})
@Test(groups = "manual")
public class VehicleServiceImplManualTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private VehicleServiceVersioned vehicleServiceVersioned;

    public void testFindServiceTypeById() {
        VersionedVehicleServiceType result = vehicleServiceVersioned.findServiceTypeById(1, 2);
        Assert.assertEquals(result.getDescription(), "Сервис");
    }

}
