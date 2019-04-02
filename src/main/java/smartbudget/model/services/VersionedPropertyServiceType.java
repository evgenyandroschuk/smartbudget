package smartbudget.model.services;

import com.google.common.base.Objects;

public class VersionedPropertyServiceType {

    private int id;
    private int userId;
    private int serviceTypeId;
    private String description;

    public VersionedPropertyServiceType(int id, int userId, int serviceTypeId, String description) {
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
        VersionedPropertyServiceType that = (VersionedPropertyServiceType) o;
        return id == that.id &&
            userId == that.userId &&
            serviceTypeId == that.serviceTypeId &&
            Objects.equal(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, userId, serviceTypeId, description);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VersionedPropertyServiceType{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", serviceTypeId=").append(serviceTypeId);
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
