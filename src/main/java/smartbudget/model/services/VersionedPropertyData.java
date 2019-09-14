package smartbudget.model.services;

import com.google.common.base.Objects;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VersionedPropertyData {

    private Long id;
    private int userId;
    private int propertyId;
    private VersionedPropertyServiceType serviceType;
    private String description;
    private String master;
    private BigDecimal price;
    private LocalDate updateDate;

    public VersionedPropertyData(
            int userId,
            int propertyId,
            VersionedPropertyServiceType serviceTypeId,
            String description,
            String master,
            BigDecimal price,
            LocalDate updateDate
    ) {
        this.userId = userId;
        this.propertyId = propertyId;
        this.serviceType = serviceTypeId;
        this.description = description;
        this.master = master;
        this.price = price;
        this.updateDate = updateDate;
    }

    public VersionedPropertyData(
            Long id, 
            int userId, 
            int propertyId,
            VersionedPropertyServiceType serviceType,
            String description, 
            String master, 
            BigDecimal price, 
            LocalDate updateDate
    ) {
        this.id = id;
        this.userId = userId;
        this.propertyId = propertyId;
        this.serviceType = serviceType;
        this.description = description;
        this.master = master;
        this.price = price;
        this.updateDate = updateDate;
    }

    public Long getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public VersionedPropertyServiceType getServiceType() {
        return serviceType;
    }

    public String getDescription() {
        return description;
    }

    public String getMaster() {
        return master;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public String getUpdateDateString() {
        return updateDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VersionedPropertyData that = (VersionedPropertyData) o;
        return userId == that.userId &&
                propertyId == that.propertyId &&
                serviceType == that.serviceType &&
                Objects.equal(id, that.id) &&
                Objects.equal(description, that.description) &&
                Objects.equal(master, that.master) &&
                Objects.equal(price, that.price) &&
                Objects.equal(updateDate, that.updateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, userId, propertyId, serviceType, description, master, price, updateDate);
    }

    @Override
    public String toString() {
        return "VersionedPropertyData{" +
                "id=" + id +
                ", userId=" + userId +
                ", propertyId=" + propertyId +
                ", serviceType=" + serviceType +
                ", description='" + description + '\'' +
                ", master='" + master + '\'' +
                ", price=" + price +
                ", updateDate=" + updateDate +
                '}';
    }
}
