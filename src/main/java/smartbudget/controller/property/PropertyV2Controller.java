package smartbudget.controller.property;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartbudget.model.services.VersionedProperty;
import smartbudget.model.services.VersionedPropertyServiceType;
import smartbudget.service.postres.property.PropertyRepository;

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
}
