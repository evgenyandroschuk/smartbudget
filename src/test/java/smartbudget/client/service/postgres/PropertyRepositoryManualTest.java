package smartbudget.client.service.postgres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;
import smartbudget.model.services.VersionedProperty;
import smartbudget.model.services.VersionedPropertyData;
import smartbudget.model.services.VersionedPropertyServiceType;
import smartbudget.service.postres.DbConfig;
import smartbudget.service.postres.PostgreSqlConfig;
import smartbudget.service.postres.property.PropertyRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest(classes = {
        PostgreSqlConfig.class,
        DbConfig.class,
        PostgreSqlConfig.class
})
@Test(groups = "manual")
public class PropertyRepositoryManualTest extends AbstractTestNGSpringContextTests {


    @Autowired
    private PropertyRepository propertyRepository;

    public void testGetProperties() {
        List<VersionedProperty> result = propertyRepository.getProperties(USER_ID);
        System.out.println(result);
        Assert.assertTrue(!result.isEmpty());
    }

    public void testGetServiceTypes() {
        List<VersionedPropertyServiceType> serviceTypes = propertyRepository.getServiceTypes(USER_ID);
        System.out.println(serviceTypes);
        Assert.assertEquals(
            serviceTypes.stream()
                .filter(t -> t.getUserId() == 1 && t.getServiceTypeId() == 1)
                .findFirst().get().getDescription(),
            "Фильтры питьевой воды"
        );
    }

    public void testGetPropertyData() {
        LocalDate startDate = LocalDate.of(2019, 4,6);
        LocalDate endDate = startDate.plusDays(2);
        List<VersionedPropertyData> propertyData = propertyRepository.getPropertyData(1, 1, startDate, endDate);
        System.out.println(propertyData);
    }

    public void testSavePropertyData() {
        VersionedPropertyData propertyData = new VersionedPropertyData(
                USER_ID, PROPERTY_ID, getDefaultServiceType(), "Test desc", "Test master",
                BigDecimal.valueOf(100.00), LocalDate.of(2019, 4, 5)
        );
        propertyRepository.savePropertyData(propertyData);
    }

    private static VersionedPropertyServiceType getDefaultServiceType() {
        return new VersionedPropertyServiceType(1, 1, 1, "Service 01");
    }

    public void testDeletePropertyData() {
        propertyRepository.deletePropertyData(4L);
    }

    private static final int USER_ID = 1;
    private static final int PROPERTY_ID = 1;

}
