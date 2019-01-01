package smartbudget.view.property;

public class PropertyResponse {

    private String status;
    private ServiceData serviceData;

    public PropertyResponse() {
    }

    public PropertyResponse(String status, ServiceData serviceData) {
        this.status = status;
        this.serviceData = serviceData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ServiceData getServiceData() {
        return serviceData;
    }

    public void setServiceData(ServiceData serviceData) {
        this.serviceData = serviceData;
    }
}
