package smartbudget.controller.property;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import smartbudget.model.services.VersionedProperty;
import smartbudget.model.services.VersionedPropertyData;
import smartbudget.model.services.VersionedPropertyServiceType;
import smartbudget.service.postres.property.PropertyRepository;
import smartbudget.view.property.VersionedPropertyDataRequest;
import smartbudget.view.property.VersionedPropertyResponse;
import smartbudget.view.property.VersionedServiceData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(value = "/property/v2/")
public class PropertyV2Controller {

    PropertyRepository propertyRepository;

    @Autowired
    public PropertyV2Controller(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @RequestMapping(value = "/properties", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VersionedProperty> getProperties(@RequestParam(value = "user_id") Integer userId) {
        return propertyRepository.getProperties(userId);
    }

    @RequestMapping(value = "/types", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VersionedPropertyServiceType> gerPropertyServiceTypes(@RequestParam(value = "user_id") Integer userId){
        return propertyRepository.getServiceTypes(userId);
    }

    @RequestMapping(
            value = "/data", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public List<VersionedPropertyData> propertyData (@RequestBody VersionedPropertyDataRequest request) {
        return propertyRepository.getPropertyData(
                request.getUserId(),
                1, LocalDate.parse(request.getStartDate(), YYYY_MM_DD_FORMAT),
                LocalDate.parse(request.getEndDate(), YYYY_MM_DD_FORMAT)
        );
    }

    @RequestMapping(
            value = "/data/save", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public VersionedPropertyResponse saveData(@RequestBody VersionedServiceData serviceData) {
        VersionedPropertyServiceType serviceType = propertyRepository.getServiceTypes(serviceData.getUserId())
                .stream()
                .filter(t -> t.getServiceTypeId() == serviceData.getPropertyServiceType().getId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No such serviceTypeId = " + serviceData.getPropertyServiceType().getId()));
        VersionedPropertyData propertyData = new VersionedPropertyData(
                serviceData.getUserId(),
                serviceData.getProperty().getId(),
                serviceType,
                serviceData.getDescription(),
                serviceData.getName(),
                BigDecimal.valueOf(serviceData.getPrice()).setScale(2),
                LocalDate.parse(serviceData.getUpdateDate(), YYYY_MM_DD_FORMAT)
        );
        propertyRepository.savePropertyData(propertyData);
        return new VersionedPropertyResponse("Ok", serviceData);

    }



    private static final DateTimeFormatter YYYY_MM_DD_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
}
