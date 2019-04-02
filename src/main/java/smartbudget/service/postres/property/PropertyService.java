package smartbudget.service.postres.property;

import smartbudget.model.services.VersionedProperty;
import smartbudget.model.services.VersionedPropertyServiceType;

import java.util.List;

public interface PropertyService {

    List<VersionedProperty> getProperties();
    List<VersionedPropertyServiceType> getServiceTypes();

}
