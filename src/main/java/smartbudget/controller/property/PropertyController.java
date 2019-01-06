package smartbudget.controller.property;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import smartbudget.db.DbUtil;
import smartbudget.model.services.Property;
import smartbudget.model.services.PropertyServiceData;
import smartbudget.model.services.PropertyServiceType;
import smartbudget.service.DbServiceFactory;
import smartbudget.util.AppProperties;
import smartbudget.view.PropertyDataRequest;
import smartbudget.view.property.PropertyResponse;
import smartbudget.view.property.ServiceData;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(value = "/property/")
public class PropertyController {

    private DbServiceFactory dbServiceFactory;

    @Autowired
    public PropertyController (AppProperties properties) throws SQLException {
        String name = properties.getProperty("app.impl");
        Connection connection = new DbUtil(properties).getConnect();
        dbServiceFactory = new DbServiceFactory(name, connection);
    }

    /**
     *
     *
     * http://localhost:7004/property/properties
     * @return
     */
    @RequestMapping(value = "/properties", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Property> properties() {
        return dbServiceFactory.getPropertyService().getProperties();
    }

    /**
     *
     *
     * http://localhost:7004/property/types
     * @return
     */
    @RequestMapping(value = "/types", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PropertyServiceType> propertyServiceTypes() {
        return dbServiceFactory.getPropertyService().getPropertyServiceTypes();
    }

    /**
     *
     *curl -H "Accept: application/json; charset=UTF-8" -H "Content-type: application/json; charset=UTF-8" -X POST -d '{"property":{"id":1,"description":"Фонвизина"},"startDate":"2015-01-01","endDate":"2019-12-31"}' http://localhost:7004/property/data
     */
    @RequestMapping(
            value = "/data", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public @ResponseBody List<PropertyServiceData> serviceDataList(
            @RequestBody PropertyDataRequest propertyDataRequest
    ) {
        int propertyId = propertyDataRequest.getProperty().getId();
        String propertyDescription = propertyDataRequest.getProperty().getDescription();
        Property property = new Property(propertyId, propertyDescription);

        LocalDate start = LocalDate.parse(propertyDataRequest.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate end = LocalDate.parse(propertyDataRequest.getEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return dbServiceFactory.getPropertyService().getPropertyServiceDataByPeriod(property, start, end);
    }

    /**
     *
     curl -H "Accept: application/json; charset=UTF-8" -H "Content-type: application/json; charset=UTF-8" \
     -X POST -d '{"property":{"id":2,"description":"Бибирево"},"propertyServiceType":{"id":8,"description":"Прочее"},"description":"description test","name":"name test","price":0.0,"updateDate":"2019-01-02"}' \
     http://localhost:7004/property/data/save
     * @param serviceData
     * @return
     */
    @RequestMapping(
            value = "/data/save", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public @ResponseBody PropertyResponse saveServiceData(@RequestBody ServiceData serviceData) {

        Property property = new Property(serviceData.getProperty().getId(), serviceData.getProperty().getDescription());
        PropertyServiceType serviceType = new PropertyServiceType(
                serviceData.getPropertyServiceType().getId(),
                serviceData.getPropertyServiceType().getDescription()
        );
        PropertyServiceData propertyServiceData = new PropertyServiceData(
                property,
                serviceType,
                serviceData.getDescription(),
                serviceData.getName(),
                serviceData.getPrice(),
                serviceData.getUpdateDate()
        );
        dbServiceFactory.getPropertyService().savePropertyServiceData(propertyServiceData);
        return new PropertyResponse("Ok", serviceData);
    }


}
