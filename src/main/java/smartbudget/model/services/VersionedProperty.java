package smartbudget.model.services;

import com.google.common.base.Objects;

public class VersionedProperty {

    private final int id;
    private final int userId;
    private final int propertyId;
    private final String description;

    public VersionedProperty(int id, int userId, int propertyId, String description) {
        this.id = id;
        this.userId = userId;
        this.propertyId = propertyId;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VersionedProperty property = (VersionedProperty) o;
        return id == property.id &&
                userId == property.userId &&
                propertyId == property.propertyId &&
                Objects.equal(description, property.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, userId, propertyId, description);
    }

    @Override
    public String toString() {
        return "VersionedProperty{" +
                "id=" + id +
                ", userId=" + userId +
                ", propertyId=" + propertyId +
                ", description='" + description + '\'' +
                '}';
    }
}
