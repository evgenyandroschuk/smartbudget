package smartbudget.view;

import smartbudget.view.property.TypeData;


public class PropertyDataRequest {

    private TypeData property;
    private String startDate;
    private String endDate;

    public PropertyDataRequest() {
        super();
    }

    public PropertyDataRequest(TypeData property, String startDate, String endDate) {
        this.property = property;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public TypeData getProperty() {
        return property;
    }

    public void setProperty(TypeData property) {
        this.property = property;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
