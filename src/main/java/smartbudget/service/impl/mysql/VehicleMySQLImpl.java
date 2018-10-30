package smartbudget.service.impl.mysql;

import smartbudget.model.vehicles.Vehicle;
import smartbudget.model.vehicles.VehicleData;
import smartbudget.model.vehicles.VehicleServiceType;
import smartbudget.service.VehicleService;
import smartbudget.service.impl.AbstractService;
import smartbudget.util.HelpUtils;
import smartbudget.util.Numerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class VehicleMySQLImpl extends AbstractService implements VehicleService {

    public VehicleMySQLImpl(Connection connection) {
        super(connection);
    }

    @Override
    public void createVehicleData(VehicleData vehicleData) {
        String query = "insert into vehicle_data (id, vehicle_service_type_id, vehicle_id, description, mileage, price, update_date)\n" +
                "values(get_id(?), ?, ?, ?, ?, ?, ?)";

        String updateDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, Numerator.VEHICLE_DATA);
            preparedStatement.setInt(2, vehicleData.getVehicleServiceType());
            preparedStatement.setInt(3, vehicleData.getVehicleId());
            preparedStatement.setString(4, vehicleData.getDescription());
            preparedStatement.setInt(5, vehicleData.getMileAge());
            preparedStatement.setDouble(6, vehicleData.getPrice());
            preparedStatement.setString(7, updateDate);
            preparedStatement.execute();
        } catch (SQLException e) {
            new RuntimeException(e);
        }
    }

    @Override
    public void deleteVehicleData(Long id) {
        String query = "delete from vehicle_data where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            new RuntimeException(e);
        }
    }

    @Override
    public VehicleData findVehicleDataById(Long id) {
        String query = "select * from vehicle_data where id = ?";
        VehicleData vehicleData = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int serviceID = rs.getInt("vehicle_service_type_id");
                int vehicleId = rs.getInt("vehicle_id");
                String description = rs.getString("description");
                int mileage = rs.getInt("mileage");
                double price = rs.getDouble("price");
                Date date = rs.getDate("update_date");
                LocalDate updateDate = HelpUtils.convertDateToLocalDate(date);
                vehicleData = VehicleData.of(id, serviceID, vehicleId, description, mileage, price, updateDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicleData;
    }

    @Override
    public List<VehicleData> findVehicleDataByYear(int year) {
        return null;
    }

    @Override
    public List<VehicleData> findLastVehicleData(int vehicleId, int size) {
        String query = "select * from vehicle_data where vehicle_id = ? order by id desc limit ?";
        List<VehicleData> vehicleDataList = new LinkedList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, vehicleId);
            statement.setInt(2, size);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                VehicleData vehicleData = null;
                long id = rs.getLong("id");
                int serviceID = rs.getInt("vehicle_service_type_id");
                String description = rs.getString("description");
                int mileage = rs.getInt("mileage");
                double price = rs.getDouble("price");
                Date date = rs.getDate("update_date");
                LocalDate updateDate = HelpUtils.convertDateToLocalDate(date);
                vehicleData = VehicleData.of(id, serviceID, vehicleId, description, mileage, price, updateDate);
                vehicleDataList.add(vehicleData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicleDataList;
    }

    public List<Vehicle> getVehicles() {
        String query = "select * from vehicle";
        List<Vehicle> vehicles = new LinkedList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Vehicle vehicle;
                int id = rs.getInt("id");
                String description = rs.getString("description");
                String licensePlate = rs.getString("license_plate");
                String vin = rs.getString("vin_nr");
                String sts = rs.getString("sts_nr");
                vehicle = Vehicle.of(id, description, licensePlate, vin, sts);
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    @Override
    public Vehicle findVehicleById(int id) {
        String query = "select * from vehicle where id = ?";
        Vehicle vehicle = null;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String description = rs.getString("description");
                String licensePlate = rs.getString("license_plate");
                String vin = rs.getString("vin_nr");
                String sts = rs.getString("sts_nr");
                vehicle = Vehicle.of(id, description, licensePlate, vin, sts);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicle;
    }

    public List<VehicleServiceType> getVehicleServiceTypes() {
        String query = "select * from vehicle_service_type";
        List<VehicleServiceType> serviceTypes = new LinkedList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                VehicleServiceType serviceType = null;
                int id = rs.getInt("id");
                String description = rs.getString("description");
                serviceType = VehicleServiceType.of(id, description);
                serviceTypes.add(serviceType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return serviceTypes;
    }

    @Override
    public VehicleServiceType findServiceTypeById(int id) {
        String query = "select * from vehicle_service_type where id = ?";
        VehicleServiceType serviceType = null;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String description = rs.getString("description");
                serviceType = VehicleServiceType.of(id, description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return serviceType;
    }
}
