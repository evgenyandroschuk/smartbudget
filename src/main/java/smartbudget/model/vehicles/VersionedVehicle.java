package smartbudget.model.vehicles;

public class VersionedVehicle  {

    private final int id;
    private final int userId;
    private final String description;
    private final String licensePlate;
    private final String vin;
    private final String sts;

    public VersionedVehicle(int id, int userId, String description, String licensePlate, String vin, String sts) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.licensePlate = licensePlate;
        this.vin = vin;
        this.sts = sts;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getVin() {
        return vin;
    }

    public String getSts() {
        return sts;
    }
}
