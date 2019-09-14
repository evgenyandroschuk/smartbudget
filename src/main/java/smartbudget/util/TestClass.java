package smartbudget.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import smartbudget.view.property.PropertyDataRequest;
import smartbudget.view.property.ServiceData;
import smartbudget.view.property.TypeData;

public class TestClass {

    public static void main(String [] args) throws JsonProcessingException {
        PropertyDataRequest request = new PropertyDataRequest(
                new TypeData(1, "Фонвизина"),
                "2015-01-01",
                "2019-12-31"
        );

        ObjectMapper om = new ObjectMapper();
        String jsonPropertyDate = om.writeValueAsString(request);
        System.out.println("jsonPropertyDate = " + jsonPropertyDate);

        ServiceData serviceData = new ServiceData(
                new TypeData(2, "Бибирево"),
                new TypeData(8, "Прочее"),
                "description test",
                "name test",
                0,
                "2019-01-02"

        );
        String serviceDataJson = om.writeValueAsString(serviceData);
        System.out.println("serviceData = " + serviceDataJson);
    }

}
