package smartbudget.service.postres.property;

import smartbudget.model.services.VersionedProperty;
import smartbudget.model.services.VersionedPropertyData;
import smartbudget.model.services.VersionedPropertyServiceType;

import java.time.LocalDate;
import java.util.List;

public interface PropertyService {

    List<VersionedPropertyData> getPropertyData(int userId, LocalDate startDate, LocalDate endDate);
    List<VersionedProperty> getProperties();
    List<VersionedPropertyServiceType> getServiceTypes();

    void savePropertyData(VersionedPropertyData propertyData);

    void deletePropertyData(Long id);
}
