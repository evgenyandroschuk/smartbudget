package smartbudget.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import smartbudget.model.services.VersionedProperty;
import smartbudget.model.services.VersionedPropertyData;
import smartbudget.model.services.VersionedPropertyServiceType;
import smartbudget.service.postres.property.PropertyRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class PropertyWebController {

    private PropertyRepository propertyRepository;

    @Autowired
    public PropertyWebController(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @RequestMapping(value = "/communication", method = RequestMethod.GET)
    public String properties(Model model) {
        model.addAttribute("properties", propertyRepository.getProperties(DEFAULT_USER));
        return "property/property";
    }

    @RequestMapping(value = "/property/details", method = RequestMethod.GET)
    public String propertyData(Model model, @RequestParam(value="property") Integer propertyId) {
        VersionedProperty property = propertyRepository.getProperties(DEFAULT_USER)
                .stream()
                .filter(t -> t.getId() == propertyId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no such property id = " + propertyId));
        LocalDate start = LocalDate.parse("2011-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate end = LocalDate.now().plusDays(1);
        List<VersionedPropertyData> dataList = propertyRepository.getPropertyData(DEFAULT_USER, propertyId, start, end);
        model.addAttribute("propertyData", dataList);
        model.addAttribute("property", property);
        return "property/property_details";
    }

    @RequestMapping(value = "/property/data/add", method = RequestMethod.GET)
    public String savePropertyData(Model model, @RequestParam int propertyId) {


        VersionedProperty property = propertyRepository.getProperties(DEFAULT_USER)
                .stream().filter(t -> t.getId() == propertyId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no such property id = " + propertyId));
        model.addAttribute("services", propertyRepository.getServiceTypes(DEFAULT_USER));
        model.addAttribute("property", property);
        return "property/property_data_add";
    }

    @RequestMapping(value = "/property/data/add/response", method = RequestMethod.GET)
    public String savePropertyDataResponse(
            Model model,
            @RequestParam int propertyId,
            @RequestParam int serviceId,
            @RequestParam String description,
            @RequestParam String name,
            @RequestParam double price,
            @RequestParam String date
    )  {
        VersionedProperty property = propertyRepository.getProperties(DEFAULT_USER)
                .stream().filter(t -> t.getId() == propertyId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no such property id = " + propertyId));

        VersionedPropertyServiceType serviceType = propertyRepository.getServiceTypes(DEFAULT_USER)
                .stream()
                .filter(t -> t.getServiceTypeId() == serviceId)
                .findFirst().orElseThrow(() -> new RuntimeException("no such service type id = " + serviceId));
        VersionedPropertyData propertyData = new VersionedPropertyData(
                DEFAULT_USER, propertyId, serviceType,description, name, BigDecimal.valueOf(price).setScale(2),
                LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
        model.addAttribute("propertyData", propertyData);
        model.addAttribute("property", property);
        propertyRepository.savePropertyData(propertyData);
        return "property/property_data_add_response";
    }

    private final static int DEFAULT_USER = 1;
}
