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
import org.testng.annotations.Test;
import smartbudget.model.vehicles.VersionedVehicle;
import smartbudget.model.vehicles.VersionedVehicleData;
import smartbudget.model.vehicles.VersionedVehicleServiceType;
import smartbudget.service.postres.vehicle.VehicleRepositoryImpl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VehicleRepositoryImplTest {

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @InjectMocks
    private VehicleRepositoryImpl vehicleService;

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
        String query = "select id, user_id, service_type_id, description\n" +
            "from t_vehicle_service_type where user_id = :userId";

        List<VersionedVehicleServiceType> serviceTypeList = ImmutableList.of(
            new VersionedVehicleServiceType(1, 1, 1, "Страхова"),
            new VersionedVehicleServiceType(2, 1, 2, "Сервис")
        );

        when(namedParameterJdbcTemplate.query(
            eq(query), eq(getUserIdMap()),
            (ResultSetExtractor<List<VersionedVehicleServiceType>>) any(ResultSetExtractor.class))
        ).thenReturn(serviceTypeList);

        VersionedVehicleServiceType result = vehicleService.findServiceTypeById(USER_ID, 2);
        Assert.assertEquals(result.getDescription(), "Сервис");
        verify(namedParameterJdbcTemplate).query(eq(query), eq(getUserIdMap()),
            (ResultSetExtractor<List<VersionedVehicleServiceType>>) any(ResultSetExtractor.class));
    }

    @Test
    public void testFindVehicle() {
        String query = "select id, user_id, vehicle_id, description, license_plate, vin, sts " +
            "from t_vehicle where user_id = :userId";

        List<VersionedVehicle> vehicles = ImmutableList.of(
            new VersionedVehicle(1,1,1, "Tiguan", "license1", "vin", "sts"),
            new VersionedVehicle(2,1,2, "Nissan", "license2", "vin", "sts")
        );

        when(namedParameterJdbcTemplate.query(
            eq(query),
            eq(getUserIdMap()),
            (ResultSetExtractor<List<VersionedVehicle>>) any(ResultSetExtractor.class))
        ).thenReturn(vehicles);

        VersionedVehicle result = vehicleService.findVehicleById(USER_ID, 2);
        Assert.assertEquals(result.getDescription(), "Nissan");

        verify(namedParameterJdbcTemplate).query(
            eq(query),
            eq(getUserIdMap()),
            (ResultSetExtractor<List<VersionedVehicle>>) any(ResultSetExtractor.class)
        );

    }


    @Test
    public void testFindDataByPeriod() {
        String query =
            "select id, user_id, vehicle_id, vehicle_service_type_id, description, mile_age, price, update_date " +
                "from vehicle_data\n" +
                "where user_id = :userId and update_date >= :startDate and update_date <= :endDate order by id desc";

        LocalDate startDate = LocalDate.parse("2019-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse("2019-05-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<VersionedVehicleData> vehicleDataList = ImmutableList.of(
            getVehicleData(33000.0, "description 1", "2019-02-21"),
            getVehicleData(9000.0, "description 2", "2019-04-02")
        );

        Map<String, Object> params = ImmutableMap.of(
            "userId", USER_ID,
            "startDate", Date.valueOf(startDate),
            "endDate", Date.valueOf(endDate)
        );

        when(namedParameterJdbcTemplate.query(
            eq(query),
            eq(params),
            (ResultSetExtractor<List<VersionedVehicleData>>) any(ResultSetExtractor.class))
        ).thenReturn(vehicleDataList);

        List<VersionedVehicleData> result = vehicleService.findVehicleDataByPeriod(USER_ID, startDate, endDate);
        Assert.assertEquals(result.get(1).getDescription(), "description 2");

        verify(namedParameterJdbcTemplate).query(
            eq(query),
            eq(params),
            (ResultSetExtractor<List<VersionedVehicleData>>) any(ResultSetExtractor.class)
        );
    }

    @Test
    public void testFindDataById() {
        String query =
            "select id, user_id, vehicle_id, vehicle_service_type_id, description, mile_age, price, update_date " +
                "from vehicle_data\n" +
                "where id = :id";
        Map<String, Object> params = ImmutableMap.of("id", 1L);
        when(namedParameterJdbcTemplate.query(
            eq(query),
            eq(params),
            (ResultSetExtractor<VersionedVehicleData>) any(ResultSetExtractor.class)
            )
        ).thenReturn(getVehicleData(30001, "description test", "2019-03-04"));

        VersionedVehicleData result = vehicleService.findVehicleDataById(1L);

        Assert.assertEquals(result.getDescription(), "description test");
        verify(namedParameterJdbcTemplate).query(
            eq(query),
            eq(params),
            (ResultSetExtractor<VersionedVehicleData>) any(ResultSetExtractor.class)
        );
    }

    @Test
    public void testCreateVehicleData() {
        String query =
                "insert into vehicle_data(\n" +
                        "  id,\n" +
                        "  user_id,\n" +
                        "  vehicle_id,\n" +
                        "  vehicle_service_type_id,\n" +
                        "  description,\n" +
                        "  mile_age,\n" +
                        "  price,\n" +
                        "  update_date\n" +
                        ")\n" +
                        "values (\n" +
                        "  nextval('vehicle_seq'),\n" +
                        "  :userId,\n" +
                        "  :vehicleId,\n" +
                        "  :serviceTypId,\n" +
                        "  :description,\n" +
                        "  :mileAge,\n" +
                        "  :price,\n" +
                        "  :updateDate\n" +
                        ")";


        VersionedVehicleData vehicleData = new VersionedVehicleData(
                1, 1, getVersionedVehicleServiceType(), "test desc",
                31002, BigDecimal.valueOf(12000.10), LocalDate.of(2019,1,20)
        );

        Map<String, Object> params = new HashMap<>();
        params.put("userId", vehicleData.getUserId());
        params.put("vehicleId", vehicleData.getVehicleId());
        params.put("serviceTypId", vehicleData.getVehicleServiceType().getId());
        params.put("description", vehicleData.getDescription());
        params.put("mileAge", vehicleData.getMileAge());
        params.put("price", vehicleData.getPrice());
        params.put("updateDate", Date.valueOf(vehicleData.getDate()));

        when(namedParameterJdbcTemplate.execute(
                eq(query), eq(params),
                (PreparedStatementCallback<Boolean>) any(PreparedStatementCallback.class))).thenReturn(true);

        vehicleService.createVehicleData(vehicleData);

        verify(namedParameterJdbcTemplate).execute(
                eq(query), eq(params),
                (PreparedStatementCallback<PreparedStatement>) any(PreparedStatementCallback.class));
    }

    @Test
    public void testDeleteVehicleData() {
        String query = "delete from vehicle_data where id = :id";
        Map<String, Object> params = ImmutableMap.of("id", 2L);

        when(namedParameterJdbcTemplate.execute(eq(query), eq(params),
                (PreparedStatementCallback<Boolean>) any(PreparedStatementCallback.class))).thenReturn(true);

        vehicleService.deleteVehicleData(2L);

        verify(namedParameterJdbcTemplate).execute(eq(query), eq(params),
                (PreparedStatementCallback<Boolean>) any(PreparedStatementCallback.class));

    }


    private static VersionedVehicleData getVehicleData(double price, String description,  String dateString) {
        VersionedVehicleServiceType vehicleServiceType = getVersionedVehicleServiceType();
        return new VersionedVehicleData(
            1L, 1, 1, vehicleServiceType, description, 43000, BigDecimal.valueOf(price),
            LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );

    }

    private static VersionedVehicleServiceType getVersionedVehicleServiceType() {
        return new VersionedVehicleServiceType(
                    1, USER_ID, 1, "service"
            );
    }


    private static Map<String, Object> getUserIdMap() {
        return ImmutableMap.of("userId", USER_ID);
    }

    private static final int USER_ID = 1;



}
