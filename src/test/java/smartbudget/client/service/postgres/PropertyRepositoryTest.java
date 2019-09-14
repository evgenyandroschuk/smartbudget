package smartbudget.client.service.postgres;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import smartbudget.model.services.VersionedProperty;
import smartbudget.model.services.VersionedPropertyData;
import smartbudget.model.services.VersionedPropertyServiceType;
import smartbudget.service.postres.property.PropertyRepositoryImpl;

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

public class PropertyRepositoryTest {

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @InjectMocks
    private PropertyRepositoryImpl propertyService;

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
        String query = "select id, user_id, property_id, description from t_property where user_id = :userId";
        List<VersionedProperty> properties = ImmutableList.of(
                new VersionedProperty(1, 1, 1, "Property 1"),
                new VersionedProperty(2, 1, 1, "Property 2")
        );

        Map<String, Object> params = ImmutableMap.of("userId", USER_ID);

        when(namedParameterJdbcTemplate.query(
                eq(query),
                eq(params),
                (ResultSetExtractor<List<VersionedProperty>>) any(ResultSetExtractor.class))
        ).thenReturn(properties);

        List<VersionedProperty> result = propertyService.getProperties(USER_ID);
        Assert.assertEquals(result.get(1).getDescription(), "Property 2");

        verify(namedParameterJdbcTemplate)
                .query(
                        eq(query),
                        eq(params),
                        (ResultSetExtractor<List<VersionedProperty>>) any(ResultSetExtractor.class)
                );
    }

    @Test
    public void testGetServiceType() {
        String query =
                "select id, user_id, service_type_id, description from t_property_service_type where user_id = :userId";
        List<VersionedPropertyServiceType> serviceTypes = ImmutableList.of(
                new VersionedPropertyServiceType(1, 1, 1, "Service 01"),
                new VersionedPropertyServiceType(1, 1, 2, "Service 02")
        );
        Map<String, Object> params = ImmutableMap.of("userId", USER_ID);
        when(namedParameterJdbcTemplate.query(
                eq(query),
                eq(params),
                (ResultSetExtractor<List<VersionedPropertyServiceType>>) any(ResultSetExtractor.class)
        )).thenReturn(serviceTypes);

        List<VersionedPropertyServiceType> result = propertyService.getServiceTypes(USER_ID);
        Assert.assertEquals(
                result.stream().filter(t -> t.getServiceTypeId() == 2).findFirst().get().getDescription(),
                "Service 02"
        );

        verify(namedParameterJdbcTemplate).query(
                eq(query),
                eq(params),
                (ResultSetExtractor<List<VersionedPropertyServiceType>>) any(ResultSetExtractor.class)
        );
    }

    @Test
    public void getPropertyDataTest() {
        LocalDate startDate = LocalDate.of(2019, 04, 05);
        LocalDate endDate = startDate.plusDays(3);

        String query = "select id, user_id, property_id, service_type_id, description, master, price, update_date " +
                "from property_data where user_id = :userId and property_id = :propertyId " +
                "and update_date >= :startDate and update_date < :endDate order by id desc";
        Map<String, Object> params = ImmutableMap.of(
                "userId", USER_ID,
                "propertyId", 1,
                "startDate", Date.valueOf(startDate),
                "endDate", Date.valueOf(endDate)
        );

        List<VersionedPropertyData> propertyData = ImmutableList.of(
                new VersionedPropertyData(
                        1L, USER_ID, PROPERTY_ID, getDefaultServiceType(), "desc 01",
                        "master 01", BigDecimal.valueOf(100.02), startDate
                ),
                new VersionedPropertyData(
                        2L, USER_ID, PROPERTY_ID, getDefaultServiceType(), "desc 02",
                        "master 01", BigDecimal.valueOf(110.02), startDate
                )
        );

        when(namedParameterJdbcTemplate.query(
                eq(query),
                eq(params),
                (ResultSetExtractor<List<VersionedPropertyData>>) any(ResultSetExtractor.class))
        ).thenReturn(propertyData);

        List<VersionedPropertyData> result = propertyService.getPropertyData(1, 1, startDate, endDate);

        Assert.assertEquals(result.get(1).getDescription(), "desc 02");
        verify(namedParameterJdbcTemplate).query(
                eq(query),
                eq(params),
                (ResultSetExtractor<List<VersionedPropertyData>>) any(ResultSetExtractor.class)
        );
    }

    @Test
    public void testSavePropertyData() {
        VersionedPropertyData propertyData = new VersionedPropertyData(
                USER_ID, PROPERTY_ID, getDefaultServiceType(), "Test desc", "Test master",
                BigDecimal.valueOf(10.02), LocalDate.of(2019, 4, 5)
        );

        Map<String, Object> params = ImmutableMap.<String, Object>builder()
                .put("userId", propertyData.getUserId())
                .put("propertyId", propertyData.getPropertyId())
                .put("serviceTypeId", propertyData.getServiceType().getServiceTypeId())
                .put("description", propertyData.getDescription())
                .put("master", propertyData.getMaster())
                .put("price", propertyData.getPrice())
                .put("updateDate", Date.valueOf(propertyData.getUpdateDate()))
                .build();

        when(namedParameterJdbcTemplate.execute(
                eq(INSERT_PROPETY),
                eq(params),
                (PreparedStatementCallback<Boolean>) any(PreparedStatementCallback.class)
        )).thenReturn(true);

        propertyService.savePropertyData(propertyData);

        verify(namedParameterJdbcTemplate).execute(
                eq(INSERT_PROPETY),
                eq(params),
                (PreparedStatementCallback<Boolean>) any(PreparedStatementCallback.class));
    }

    @Test(
            dataProvider = "savePropertyDataFailed",
            expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Scale of price value must not over 2 !!!"
    )
    public void testSavePropertyDataFailed(BigDecimal price) {
        VersionedPropertyData propertyData = new VersionedPropertyData(
                USER_ID, PROPERTY_ID, getDefaultServiceType(), "Test desc", "Test master",
                price, LocalDate.of(2019, 4, 5)
        );

        Map<String, Object> params = ImmutableMap.<String, Object>builder()
                .put("userId", propertyData.getUserId())
                .put("propertyId", propertyData.getPropertyId())
                .put("serviceTypeId", propertyData.getServiceType())
                .put("description", propertyData.getDescription())
                .put("master", propertyData.getMaster())
                .put("price", propertyData.getPrice())
                .put("updateDate", Date.valueOf(propertyData.getUpdateDate()))
                .build();

        when(namedParameterJdbcTemplate.execute(
                eq(INSERT_PROPETY),
                eq(params),
                (PreparedStatementCallback<Boolean>) any(PreparedStatementCallback.class)
        )).thenReturn(true);

        propertyService.savePropertyData(propertyData);

        verify(namedParameterJdbcTemplate).execute(
                eq(INSERT_PROPETY),
                eq(params),
                (PreparedStatementCallback<Boolean>) any(PreparedStatementCallback.class));
    }

    @DataProvider
    private static Object[][] savePropertyDataFailed() {
        return new Object[][]{
                {
                        BigDecimal.valueOf(100.002)
                },
                {
                        BigDecimal.valueOf(100.1002),
                }
        };
    }

    @Test
    public void deletePropertyData() {
        String sql = "delete from property_data where id = :id";
        Map<String, Object> params = ImmutableMap.of("id", 2L);
        when(namedParameterJdbcTemplate.execute(
                eq(sql), eq(params), (PreparedStatementCallback<Boolean>) any(PreparedStatementCallback.class))
        ).thenReturn(true);
        propertyService.deletePropertyData(2L);

        verify(namedParameterJdbcTemplate).execute(
                eq(sql), eq(params), (PreparedStatementCallback<Boolean>) any(PreparedStatementCallback.class));

    }

    private static VersionedPropertyServiceType getDefaultServiceType() {
        return new VersionedPropertyServiceType(1, 1, 1, "Service 01");
    }

    private static final String INSERT_PROPETY = "insert into property_data\n" +
            "(id, user_id, property_id, service_type_id, description, master, price, update_date)\n" +
            "values (nextval('property_seq'), :userId, :propertyId, :serviceTypeId, " +
            ":description, :master, :price, :updateDate)";


    private static final int PROPERTY_ID = 1;
    private static final int USER_ID = 1;

}
