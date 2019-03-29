package smartbudget.client.service.postgres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;
import smartbudget.model.vehicles.VersionedVehicle;
import smartbudget.model.vehicles.VersionedVehicleData;
import smartbudget.model.vehicles.VersionedVehicleServiceType;
import smartbudget.service.postres.DbConfig;
import smartbudget.service.postres.PostgreSqlConfig;
import smartbudget.service.postres.VehicleServiceVersioned;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest(classes = {
    PostgreSqlConfig.class,
    DbConfig.class
})
@Test(groups = "manual")
public class VehicleServiceImplManualTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private VehicleServiceVersioned vehicleServiceVersioned;

    public void testFindServiceTypeById() {
        VersionedVehicleServiceType result = vehicleServiceVersioned.findServiceTypeById(USER_ID, 2);
        Assert.assertEquals(result.getDescription(), "Сервис");
    }

    public void testFindVehicle() {
        VersionedVehicle vehicle = vehicleServiceVersioned.findVehicleById(USER_ID, 2);
        Assert.assertEquals(vehicle.getDescription(), "BMW G310R");
    }

    public void testFindDataByPeriod() {
        LocalDate startDate = LocalDate.parse("2019-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse("2019-05-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<VersionedVehicleData> data = vehicleServiceVersioned.findVehicleDataByPeriod(USER_ID, startDate, endDate);
        Assert.assertTrue(!data.isEmpty());
    }

    public void testFindDataById() {
        VersionedVehicleData data = vehicleServiceVersioned.findVehicleDataById(8L);
        Assert.assertNotNull(data);
    }


    private static final int USER_ID = 1;

}
