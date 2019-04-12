package smartbudget.service.postres;

import com.google.common.collect.ImmutableMap;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import smartbudget.model.vehicles.VersionedVehicle;
import smartbudget.model.vehicles.VersionedVehicleData;
import smartbudget.model.vehicles.VersionedVehicleServiceType;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class VehicleRepositoryImpl extends AbstractDao implements VehicleRepository {

    public VehicleRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
    }

    @Override
    public void createVehicleData(VersionedVehicleData vehicleData) {
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

        Map<String, Object> params = new HashMap<>();
        params.put("userId", vehicleData.getUserId());
        params.put("vehicleId", vehicleData.getVehicleId());
        params.put("serviceTypId", vehicleData.getVehicleServiceType());
        params.put("description", vehicleData.getDescription());
        params.put("mileAge", vehicleData.getPrice());
        params.put("price", vehicleData.getPrice());
        params.put("updateDate", Date.valueOf(vehicleData.getDate()));

        namedParameterJdbcTemplate.execute(query, params, PreparedStatement::execute);
    }

    @Override
    public void deleteVehicleData(Long id) {
        String query = "delete from vehicle_data where id = :id";
        Map<String, Object> params = ImmutableMap.of("id", id);

        namedParameterJdbcTemplate.execute(query, params, PreparedStatement::execute);
    }

    @Override
    public VersionedVehicleData findVehicleDataById(Long id) {
        String query =
            "select id, user_id, vehicle_id, vehicle_service_type_id, description, mile_age, price, update_date " +
                "from vehicle_data\n" +
                "where id = :id";
        Map<String, Object> params = ImmutableMap.of("id", id);

        return namedParameterJdbcTemplate.query(
            query,
            params,
            rs -> {
                VersionedVehicleData result = null;
                while(rs.next()) {
                    return new VersionedVehicleData(
                        id,
                        rs.getInt("user_id"),
                        rs.getInt("vehicle_id"),
                        rs.getInt("vehicle_service_type_id"),
                        rs.getString("description"),
                        rs.getInt("mile_age"),
                        rs.getBigDecimal("price"),
                        rs.getDate("update_date").toLocalDate()
                    );
                }
               return result;
            }
        );
    }

    @Override
    public List<VersionedVehicleData> findVehicleDataByPeriod(int userId, LocalDate startDate, LocalDate endDate) {
        String query =
            "select id, user_id, vehicle_id, vehicle_service_type_id, description, mile_age, price, update_date " +
                "from vehicle_data\n" +
                "where user_id = :userId and update_date >= :startDate and update_date <= :endDate";
        Map<String, Object> params = ImmutableMap.of(
            "userId", userId,
            "startDate", Date.valueOf(startDate),
            "endDate", Date.valueOf(endDate)
        );

        return namedParameterJdbcTemplate.query(query, params,
            rs -> {
                List<VersionedVehicleData> result = new LinkedList<>();
                while(rs.next()) {
                    VersionedVehicleData data = new VersionedVehicleData(
                        rs.getLong("id"),
                        rs.getInt("user_id"),
                        rs.getInt("vehicle_id"),
                        rs.getInt("vehicle_service_type_id"),
                        rs.getString("description"),
                        rs.getInt("mile_age"),
                        rs.getBigDecimal("price"),
                        rs.getDate("update_date").toLocalDate()
                    );
                    result.add(data);
                }
                return result;
            });
    }

    @Override
    public List<VersionedVehicle> getVehicles(int userId) {
        return getAllVehicles(userId);
    }

    @Override
    public VersionedVehicle findVehicleById(int userId, int vehicleId) {
        return getAllVehicles(userId).stream()
            .filter(t -> (t.getVehicleId() == vehicleId))
            .findFirst()
            .orElseThrow(() -> new DataNotFoundException("Vehicle not found by vehicleId:" + vehicleId + " and " +
                "userId:" + userId));
    }

    private List<VersionedVehicle> getAllVehicles(int userId) {
        String query = "select id, user_id, vehicle_id, description, license_plate, vin, sts " +
            "from t_vehicle where user_id = :userId";
        Map<String, Object> params = ImmutableMap.of("userId", userId);

        return namedParameterJdbcTemplate.query(query,params, rs -> {
            List<VersionedVehicle> result = new LinkedList<>();
            while (rs.next()) {
                VersionedVehicle vehicle = new VersionedVehicle(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("vehicle_id"),
                    rs.getString("description"),
                    rs.getString("license_plate"),
                    rs.getString("vin"),
                    rs.getString("sts")
                );
                result.add(vehicle);
            }
            return result;
        });
    }

    @Override
    public List<VersionedVehicleServiceType> getVehicleServiceTypes(int userId) {
        return getAllServiceTypes(userId);
    }

    @Override
    public VersionedVehicleServiceType findServiceTypeById(int userId, int serviceTypeId) throws DataNotFoundException {
        return getAllServiceTypes(userId).stream()
            .filter(t -> (t.getServiceTypeId() == serviceTypeId))
            .findFirst()
            .orElseThrow(() -> new DataNotFoundException(
                "VehicleServiceTypes not found by user: " + userId + " and serviceTypeId: " + serviceTypeId
            ));
    }

    private List<VersionedVehicleServiceType> getAllServiceTypes(int userId) {
        String query = "select id, user_id, service_type_id, description\n" +
            "from t_vehicle_service_type where user_id = :userId";
        Map<String, Object> params = ImmutableMap.of("userId", userId);

        return namedParameterJdbcTemplate.query(query, params,
            rs -> {
                List<VersionedVehicleServiceType> result = new LinkedList<>();
                while (rs.next()) {
                    VersionedVehicleServiceType serviceType = new VersionedVehicleServiceType(
                      rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("service_type_id"),
                        rs.getString("description")
                    );
                    result.add(serviceType);
                }
                return result;
            });
    }
}
