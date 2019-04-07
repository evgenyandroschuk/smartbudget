package smartbudget.client.service.postgres;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import smartbudget.model.services.VersionedProperty;
import smartbudget.model.services.VersionedPropertyData;
import smartbudget.model.services.VersionedPropertyServiceType;
import smartbudget.service.postres.property.PropertyServiceImpl;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PropertyServiceTest {

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @InjectMocks
    private PropertyServiceImpl propertyService;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void resetMocks() {
        reset(namedParameterJdbcTemplate);
    }

    @Test
    public void testGetProperties() {
        String query = "select id, user_id, property_id, description from t_property";
        List<VersionedProperty> properties = ImmutableList.of(
                new VersionedProperty(1, 1,1, "Property 1"),
                new VersionedProperty(2, 1,1, "Property 2")
        );

        when(namedParameterJdbcTemplate.query(
                eq(query), (ResultSetExtractor<List<VersionedProperty>>) any(ResultSetExtractor.class))
        ).thenReturn(properties);

        List<VersionedProperty> result = propertyService.getProperties();
        Assert.assertEquals(result.get(1).getDescription(), "Property 2");

        verify(namedParameterJdbcTemplate)
                .query(eq(query), (ResultSetExtractor<List<VersionedProperty>>) any(ResultSetExtractor.class));
    }

    @Test
    public void testGetServiceType() {
        String query = "select id, user_id, service_type_id, description from t_property_service_type";
        List<VersionedPropertyServiceType> serviceTypes = ImmutableList.of(
            new VersionedPropertyServiceType(1,1,1,"Service 01"),
            new VersionedPropertyServiceType(1,1,2,"Service 02")
        );
        when(namedParameterJdbcTemplate.query(
            eq(query), (ResultSetExtractor<List<VersionedPropertyServiceType>>) any(ResultSetExtractor.class)
        )).thenReturn(serviceTypes);

        List<VersionedPropertyServiceType> result = propertyService.getServiceTypes();
        Assert.assertEquals(
            result.stream().filter(t -> t.getServiceTypeId() == 2).findFirst().get().getDescription(),
            "Service 02"
        );
    }

    @Test
    public void getPropertyDataTest() {
        LocalDate startDate = LocalDate.of(2019, 04,05);
        LocalDate endDate = startDate.plusDays(3);

        String query = "select id, user_id, property_id, service_type_id, description, master, price, update_date " +
                "from property_data where user_id = :userId and update_date >= :startDate and update_date < :endDate";
        Map<String, Object> params = ImmutableMap.of(
                "userId", USER_ID,
                "startDate", Date.valueOf(startDate),
                "endDate", Date.valueOf(endDate)
        );

        List<VersionedPropertyData> propertyData = ImmutableList.of(
                new VersionedPropertyData(
                        1L, USER_ID, PROPERTY_ID, 1, "desc 01",
                        "master 01", BigDecimal.valueOf(100.02), startDate
                ),
                new VersionedPropertyData(
                        2L, USER_ID, PROPERTY_ID, 1, "desc 02",
                        "master 01", BigDecimal.valueOf(110.02), startDate
                )
        );

        when(namedParameterJdbcTemplate.query(
                eq(query),
                eq(params),
                (ResultSetExtractor<List<VersionedPropertyData>>) any(ResultSetExtractor.class))
        ).thenReturn(propertyData);

        List<VersionedPropertyData> result = propertyService.getPropertyData(1, startDate, endDate);

        Assert.assertEquals(result.get(1).getDescription(), "desc 02");
        verify(namedParameterJdbcTemplate).query(
                eq(query),
                eq(params),
                (ResultSetExtractor<List<VersionedPropertyData>>) any(ResultSetExtractor.class)
        );
    }

    private static final int USER_ID = 1;
    private static final int PROPERTY_ID = 1;

}
