package smartbudget.service.postres.property;

import smartbudget.model.services.VersionedProperty;
import smartbudget.model.services.VersionedPropertyData;
import smartbudget.model.services.VersionedPropertyServiceType;

import java.time.LocalDate;
import java.util.List;

public interface PropertyRepository {

    List<VersionedPropertyData> getPropertyData(int userId, LocalDate startDate, LocalDate endDate);
    List<VersionedProperty> getProperties(int userId);
    List<VersionedPropertyServiceType> getServiceTypes(int userId);

    void savePropertyData(VersionedPropertyData propertyData);

    void deletePropertyData(Long id);
}
