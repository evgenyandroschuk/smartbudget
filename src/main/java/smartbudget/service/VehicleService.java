package smartbudget.service;

import smartbudget.model.vehicles.Vehicle;
import smartbudget.model.vehicles.VehicleData;
import smartbudget.model.vehicles.VehicleServiceType;

import java.util.List;

public interface VehicleService {

    void createVehicleData(VehicleData vehicleData);
    void deleteVehicleData(Long id);
    VehicleData findVehicleDataById(Long id);
    List<VehicleData> findVehicleDataByYear(int year);
    List<VehicleData> findLastVehicleData(int vehicleId, int size);

    List<Vehicle> getVehicles();
    Vehicle findVehicleById(int id);

    List<VehicleServiceType> getVehicleServiceTypes();
    VehicleServiceType findServiceTypeById(int id);

}
