package smartbudget.service.postres.property;

import smartbudget.model.services.VersionedProperty;

import java.util.List;

public interface PropertyService {

    List<VersionedProperty> getProperties();

}
