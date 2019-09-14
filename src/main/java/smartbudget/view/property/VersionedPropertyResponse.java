package smartbudget.view.property;

public class VersionedPropertyResponse {

    private String status;
    private VersionedServiceData serviceData;

    public VersionedPropertyResponse() {
    }

    public VersionedPropertyResponse(String status, VersionedServiceData serviceData) {
        this.status = status;
        this.serviceData = serviceData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public VersionedServiceData getServiceData() {
        return serviceData;
    }

    public void setServiceData(VersionedServiceData serviceData) {
        this.serviceData = serviceData;
    }
}
