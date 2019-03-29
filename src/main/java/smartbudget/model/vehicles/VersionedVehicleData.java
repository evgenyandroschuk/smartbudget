package smartbudget.model.vehicles;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VersionedVehicleData  {

    private final Long id;
    private final int userId;
    private final int vehicleId;
    private final int vehicleServiceType;
    private final String description;
    private final int mileAge;
    private final BigDecimal price;
    private final LocalDate date;

    public VersionedVehicleData(
        Long id,
        int userId,
        int vehicleId,
        int vehicleServiceType,
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

    public Long getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getVehicleServiceType() {
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
}
