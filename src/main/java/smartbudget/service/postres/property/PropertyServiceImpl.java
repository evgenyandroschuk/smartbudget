package smartbudget.service.postres.property;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import smartbudget.model.services.VersionedProperty;
import smartbudget.service.postres.AbstractDao;

import java.util.LinkedList;
import java.util.List;

public class PropertyServiceImpl extends AbstractDao implements PropertyService {

    protected PropertyServiceImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
    }

    @Override
    public List<VersionedProperty> getProperties() {
        String query = "select id, user_id, property_id, description from t_property";

        return namedParameterJdbcTemplate.query(query, rs -> {
            List<VersionedProperty> properties = new LinkedList<>();
            while(rs.next()) {
                VersionedProperty property = new VersionedProperty(
                  rs.getInt("id"),
                  rs.getInt("user_id"),
                  rs.getInt("property_id"),
                  rs.getString("description")
                );
                properties.add(property);
            }
            return properties;
        });
    }
}
