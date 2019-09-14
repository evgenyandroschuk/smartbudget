package smartbudget.view.property;

public class VersionedPropertyDataRequest {

    private Integer userId;
    private TypeData property;
    private String startDate;
    private String endDate;

    public VersionedPropertyDataRequest(Integer userId, TypeData property, String startDate, String endDate) {
        this.userId = userId;
        this.property = property;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
