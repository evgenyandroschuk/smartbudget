package smartbudget.service.postres;

import com.google.common.collect.ImmutableMap;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import smartbudget.model.vehicles.VersionedVehicle;
import smartbudget.model.vehicles.VersionedVehicleData;
import smartbudget.model.vehicles.VersionedVehicleServiceType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class VehicleServiceImpl extends AbstractDao implements VehicleServiceVersioned {

    public VehicleServiceImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
    }

    @Override
    public void createVehicleData(VersionedVehicleData vehicleData) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteVehicleData(Long id) {
        throw new NotImplementedException();
    }

    @Override
    public VersionedVehicleData findVehicleDataById(Long id) {
        throw new NotImplementedException();
    }

    @Override
    public List<VersionedVehicleData> findVehicleDataByYear(int userId, int year) {
        throw new NotImplementedException();
    }

    @Override
    public List<VersionedVehicleData> findLastVehicleData(int userId, int vehicleId, int size) {
        throw new NotImplementedException();
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
