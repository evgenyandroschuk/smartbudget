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
import smartbudget.service.postres.property.PropertyService;
import smartbudget.service.postres.property.PropertyServiceConfiguration;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(classes = {
        PostgreSqlConfig.class,
        DbConfig.class,
        PropertyServiceConfiguration.class
})
@Test(groups = "manual")
public class PropertyServiceManualTest extends AbstractTestNGSpringContextTests {


    @Autowired
    private PropertyService propertyService;

    public void testGetProperties() {
        List<VersionedProperty> result = propertyService.getProperties();
        System.out.println(result);
        Assert.assertTrue(!result.isEmpty());
    }

    public void testGetServiceTypes() {
        List<VersionedPropertyServiceType> serviceTypes = propertyService.getServiceTypes();
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
        List<VersionedPropertyData> propertyData = propertyService.getPropertyData(1,startDate, endDate);
        System.out.println(propertyData);
    }

}
