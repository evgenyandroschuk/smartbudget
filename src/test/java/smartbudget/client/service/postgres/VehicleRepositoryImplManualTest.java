package smartbudget.client.service.postgres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;
import smartbudget.model.vehicles.VersionedVehicle;
import smartbudget.model.vehicles.VersionedVehicleData;
import smartbudget.model.vehicles.VersionedVehicleServiceType;
import smartbudget.service.postres.DbConfig;
import smartbudget.service.postres.JdbcSettings;
import smartbudget.service.postres.PostgreSqlConfig;
import smartbudget.service.postres.vehicle.VehicleRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@EnableConfigurationProperties(JdbcSettings.class)
@SpringBootTest(classes = {
    PostgreSqlConfig.class,
    DbConfig.class
})
@Test(groups = "manual")
public class VehicleRepositoryImplManualTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private VehicleRepository vehicleRepository;

    public void testFindServiceTypeById() {
        VersionedVehicleServiceType result = vehicleRepository.findServiceTypeById(USER_ID, 2);
        Assert.assertEquals(result.getDescription(), "Сервис");
    }

    public void testFindVehicle() {
        VersionedVehicle vehicle = vehicleRepository.findVehicleById(USER_ID, 2);
        Assert.assertEquals(vehicle.getDescription(), "BMW G310R");
    }

    public void testFindDataByPeriod() {
        LocalDate startDate = LocalDate.parse("2019-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse("2019-05-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<VersionedVehicleData> data = vehicleRepository.findVehicleDataByPeriod(USER_ID, startDate, endDate);
        Assert.assertTrue(!data.isEmpty());
    }

    public void testFindDataById() {
        VersionedVehicleData data = vehicleRepository.findVehicleDataById(1L);
        Assert.assertNotNull(data);
    }

    public void testCreateVehicleData() {
        VersionedVehicleData vehicleData = new VersionedVehicleData(
                1, 1, getVersionedVehicleServiceType(), "test desc",
                31002, BigDecimal.valueOf(12000.10), LocalDate.now()
        );
        vehicleRepository.createVehicleData(vehicleData);
    }

    public void testDeleteVehicleData() {
        vehicleRepository.deleteVehicleData(2L);
    }

    private static VersionedVehicleServiceType getVersionedVehicleServiceType() {
        return new VersionedVehicleServiceType(
                1, USER_ID, 1, "service"
        );
    }


    private static final int USER_ID = 1;

}
