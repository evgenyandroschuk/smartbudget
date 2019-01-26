package smartbudget.model.services;

public class PropertyServiceData {

    private long id;
    private Property property;
    private PropertyServiceType propertyServiceType;
    private String description;
    private String name;
    private double price;
    private String updateDate;

    public PropertyServiceData(
            long id,
            Property property,
            PropertyServiceType propertyServiceType,
            String description,
            String name,
            double price,
            String updateDate
    ) {
        this.id = id;
        this.property = property;
        this.propertyServiceType = propertyServiceType;
        this.description = description;
        this.name = name;
        this.price = price;
        this.updateDate = updateDate;
    }

    public PropertyServiceData(
            Property property,
            PropertyServiceType propertyServiceType,
            String description,
            String name,
            double price,
            String updateDate
    ) {
        this.property = property;
        this.propertyServiceType = propertyServiceType;
        this.description = description;
        this.name = name;
        this.price = price;
        this.updateDate = updateDate;
    }

    public long getId() {
        return id;
    }

    public Property getProperty() {
        return property;
    }

    public PropertyServiceType getPropertyServiceType() {
        return propertyServiceType;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getUpdateDate() {
        return updateDate;
    }

}
