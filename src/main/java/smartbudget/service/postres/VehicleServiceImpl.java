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
        throw new NotImplementedException();
    }

    @Override
    public VersionedVehicle findVehicleById(int vehicleId) {
        throw new NotImplementedException();
    }

    @Override
    public List<VersionedVehicleServiceType> getVehicleServiceTypes(int userId) {
        return getAllServiceTypes(userId);
    }

    @Override
    public VersionedVehicleServiceType findServiceTypeById(int userId, int serviceTypeId) throws DataNotFoundException {
        VersionedVehicleServiceType result = getAllServiceTypes(userId).stream()
            .filter(t -> (t.getUserId() == userId && t.getServiceTypeId() == serviceTypeId))
            .findFirst()
            .orElseThrow(() -> new DataNotFoundException(
                "VehicleServiceTypes not found by user: " + userId + " and serviceTypeId: " + serviceTypeId
            ));
        return result;
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
