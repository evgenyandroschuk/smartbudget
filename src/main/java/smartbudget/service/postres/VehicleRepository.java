package smartbudget.service.postres;

import smartbudget.model.vehicles.VersionedVehicle;
import smartbudget.model.vehicles.VersionedVehicleData;
import smartbudget.model.vehicles.VersionedVehicleServiceType;

import java.time.LocalDate;
import java.util.List;

public interface VehicleRepository {

    void createVehicleData(VersionedVehicleData vehicleData);
    void deleteVehicleData(Long id);
    VersionedVehicleData findVehicleDataById(Long id);
    List<VersionedVehicleData> findVehicleDataByPeriod(int userId, LocalDate startDate, LocalDate endDate);

    List<VersionedVehicle> getVehicles(int userId);
    VersionedVehicle findVehicleById(int userId, int vehicleId);

    List<VersionedVehicleServiceType> getVehicleServiceTypes(int userId);
    VersionedVehicleServiceType findServiceTypeById(int userId, int serviceTypeId);

}
