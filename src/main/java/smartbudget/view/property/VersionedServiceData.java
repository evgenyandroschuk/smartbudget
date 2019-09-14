package smartbudget.view.property;

public class VersionedServiceData {

    private int userId;

    private TypeData property;
    private TypeData propertyServiceType;
    private String description;
    private String name;
    private double price;
    private String updateDate;

    public VersionedServiceData() {
    }

    public VersionedServiceData(
            int userId,
            TypeData property,
            TypeData propertyServiceType,
            String description,
            String name,
            double price,
            String updateDate
    ) {
        this.userId = userId;
        this.property = property;
        this.propertyServiceType = propertyServiceType;
        this.description = description;
        this.name = name;
        this.price = price;
        this.updateDate = updateDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public TypeData getProperty() {
        return property;
    }

    public void setProperty(TypeData property) {
        this.property = property;
    }

    public TypeData getPropertyServiceType() {
        return propertyServiceType;
    }

    public void setPropertyServiceType(TypeData propertyServiceType) {
        this.propertyServiceType = propertyServiceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
