package smartbudget.model.vehicles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VersionedVehicleData  {

    private Long id;
    private final int userId;
    private final int vehicleId;
    private final VersionedVehicleServiceType vehicleServiceType;
    private final String description;
    private final int mileAge;
    private final BigDecimal price;
    private final LocalDate date;

    public VersionedVehicleData(
        Long id,
        int userId,
        int vehicleId,
        VersionedVehicleServiceType vehicleServiceType,
        String description,
        int mileAge,
        BigDecimal price,
        LocalDate date
    ) {
        this.id = id;
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.vehicleServiceType = vehicleServiceType;
        this.description = description;
        this.mileAge = mileAge;
        this.price = price;
        this.date = date;

    }

    public VersionedVehicleData(
            int userId,
            int vehicleId,
            VersionedVehicleServiceType vehicleServiceType,
            String description,
            int mileAge,
            BigDecimal price,
            LocalDate date
    ) {
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.vehicleServiceType = vehicleServiceType;
        this.description = description;
        this.mileAge = mileAge;
        this.price = price;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public VersionedVehicleServiceType getVehicleServiceType() {
        return vehicleServiceType;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public String getDescription() {
        return description;
    }

    public int getMileAge() {
        return mileAge;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDateString() {
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    @Override
    public String toString() {
        return "VersionedVehicleData{" +
                "userId=" + userId +
                ", vehicleId=" + vehicleId +
                ", vehicleServiceType=" + vehicleServiceType +
                ", description='" + description + '\'' +
                ", mileAge=" + mileAge +
                ", price=" + price +
                ", date=" + date +
                '}';
    }
}
