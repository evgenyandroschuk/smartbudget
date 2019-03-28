package smartbudget.model.vehicles;

import com.google.common.base.Objects;

public class VersionedVehicleServiceType {

    private final int id;
    private final int userId;
    private final int serviceTypeId;
    private final String description;

    public VersionedVehicleServiceType(int id, int userId, int serviceTypeId, String description) {
        this.id = id;
        this.userId = userId;
        this.serviceTypeId = serviceTypeId;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VersionedVehicleServiceType that = (VersionedVehicleServiceType) o;
        return id == that.id &&
            userId == that.userId &&
            serviceTypeId == that.serviceTypeId &&
            Objects.equal(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, userId, serviceTypeId, description);
    }
}
