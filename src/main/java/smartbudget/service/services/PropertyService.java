package smartbudget.service.services;

import smartbudget.model.services.Property;
import smartbudget.model.services.PropertyServiceData;
import smartbudget.model.services.PropertyServiceType;

import java.time.LocalDate;
import java.util.List;

public interface PropertyService {

    List<Property> getProperties();
    List<PropertyServiceType> getPropertyServiceTypes();
    List<PropertyServiceData> getPropertyServiceDataByPeriod(Property property, LocalDate startDate, LocalDate endDate);
    void savePropertyServiceData (PropertyServiceData propertyServiceData);

}
