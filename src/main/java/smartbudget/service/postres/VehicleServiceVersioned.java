package smartbudget.service.postres;

import smartbudget.model.vehicles.VersionedVehicle;
import smartbudget.model.vehicles.VersionedVehicleData;
import smartbudget.model.vehicles.VersionedVehicleServiceType;

import java.util.List;

public interface VehicleServiceVersioned {

    void createVehicleData(VersionedVehicleData vehicleData);
    void deleteVehicleData(Long id);
    VersionedVehicleData findVehicleDataById(Long id);
    List<VersionedVehicleData> findVehicleDataByYear(int userId, int year);
    List<VersionedVehicleData> findLastVehicleData(int userId, int vehicleId, int size);

    List<VersionedVehicle> getVehicles(int userId);
    VersionedVehicle findVehicleById(int vehicleId);

    List<VersionedVehicleServiceType> getVehicleServiceTypes(int userId);
    VersionedVehicleServiceType findServiceTypeById(int userId, int serviceTypeId);

}
