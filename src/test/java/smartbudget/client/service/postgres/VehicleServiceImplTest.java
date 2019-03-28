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
import smartbudget.model.vehicles.VersionedVehicleServiceType;
import smartbudget.service.postres.VehicleServiceImpl;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VehicleServiceImplTest {

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void resetMocks() {
        reset(namedParameterJdbcTemplate);
    }

    @Test
    public void testGetVehicleServiceTypes() {
        int userId = 1;
        String query = "select id, user_id, service_type_id, description\n" +
            "from t_vehicle_service_type where user_id = :userId";
        Map<String, Object> params = ImmutableMap.of("userId", userId);

        List<VersionedVehicleServiceType> serviceTypeList = ImmutableList.of(
            new VersionedVehicleServiceType(1, 1, 1, "Страхова"),
            new VersionedVehicleServiceType(2, 1, 2, "Сервис")
        );

        when(namedParameterJdbcTemplate.query(
            eq(query), eq(params),
            (ResultSetExtractor<List<VersionedVehicleServiceType>>) any(ResultSetExtractor.class))
        ).thenReturn(serviceTypeList);

        List<VersionedVehicleServiceType> result = vehicleService.getVehicleServiceTypes(userId);
        Assert.assertTrue(result.size() == serviceTypeList.size());

        verify(namedParameterJdbcTemplate).query(eq(query), eq(params),
            (ResultSetExtractor<List<VersionedVehicleServiceType>>) any(ResultSetExtractor.class));

    }

    @Test
    public void testFindServiceTypeById() {
        int userId = 1;
        String query = "select id, user_id, service_type_id, description\n" +
            "from t_vehicle_service_type where user_id = :userId";
        Map<String, Object> params = ImmutableMap.of("userId", userId);

        List<VersionedVehicleServiceType> serviceTypeList = ImmutableList.of(
            new VersionedVehicleServiceType(1, 1, 1, "Страхова"),
            new VersionedVehicleServiceType(2, 1, 2, "Сервис")
        );

        when(namedParameterJdbcTemplate.query(
            eq(query), eq(params),
            (ResultSetExtractor<List<VersionedVehicleServiceType>>) any(ResultSetExtractor.class))
        ).thenReturn(serviceTypeList);

        VersionedVehicleServiceType result = vehicleService.findServiceTypeById(userId, 2);
        Assert.assertEquals(result.getDescription(), "Сервис");
        verify(namedParameterJdbcTemplate).query(eq(query), eq(params),
            (ResultSetExtractor<List<VersionedVehicleServiceType>>) any(ResultSetExtractor.class));

    }



}
