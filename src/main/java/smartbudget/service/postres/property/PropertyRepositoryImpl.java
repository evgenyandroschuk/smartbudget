package smartbudget.service.postres.property;

import com.google.common.collect.ImmutableMap;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import smartbudget.model.services.VersionedProperty;
import smartbudget.model.services.VersionedPropertyData;
import smartbudget.model.services.VersionedPropertyServiceType;
import smartbudget.service.postres.AbstractDao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PropertyRepositoryImpl extends AbstractDao implements PropertyRepository {

    public PropertyRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
    }

    @Override
    public List<VersionedPropertyData> getPropertyData(int userId, LocalDate startDate, LocalDate endDate) {
        String query = "select id, user_id, property_id, service_type_id, description, master, price, update_date " +
                "from property_data where user_id = :userId and update_date >= :startDate and update_date < :endDate";
        Map<String, Object> params = ImmutableMap.of(
                "userId", userId,
                "startDate", Date.valueOf(startDate),
                "endDate", Date.valueOf(endDate)
                );

        return namedParameterJdbcTemplate.query(query, params, rs -> {
            List<VersionedPropertyData> data = new LinkedList<>();
            while(rs.next()) {
                VersionedPropertyData propertyData = new VersionedPropertyData(
                  rs.getLong("id"),
                  rs.getInt("user_id"),
                  rs.getInt("property_id"),
                  rs.getInt("service_type_id"),
                  rs.getString("description"),
                  rs.getString("master"),
                  rs.getBigDecimal("price"),
                  rs.getDate("update_date").toLocalDate()
                );
                data.add(propertyData);
            }
            return data;
        });
    }

    @Override
    public List<VersionedProperty> getProperties(int userId) {
        String query = "select id, user_id, property_id, description from t_property where user_id = :userId";

        Map<String, Object> params = ImmutableMap.of("userId", userId);

        return namedParameterJdbcTemplate.query(query, params, rs -> {
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

    @Override
    public List<VersionedPropertyServiceType> getServiceTypes(int userId) {
        String query =
                "select id, user_id, service_type_id, description from t_property_service_type where user_id = :userId";
        Map<String, Object> params = ImmutableMap.of("userId", userId);
        return namedParameterJdbcTemplate.query(query, params, rs -> {
            List<VersionedPropertyServiceType> serviceTypes = new LinkedList<>();
            while(rs.next()) {
                VersionedPropertyServiceType serviceType = new VersionedPropertyServiceType(
                  rs.getInt("id"),
                  rs.getInt("user_id"),
                  rs.getInt("service_type_id"),
                  rs.getString("description")
                );
                serviceTypes.add(serviceType);
            }
            return serviceTypes;
        });
    }

    @Override
    public void savePropertyData(VersionedPropertyData propertyData) {
        if (propertyData.getPrice().scale() > 2) {
            throw new IllegalArgumentException("Scale of price value must not over 2 !!!");
        }
        String sql = "insert into property_data\n" +
                "(id, user_id, property_id, service_type_id, description, master, price, update_date)\n" +
                "values (nextval('property_seq'), :userId, :propertyId, :serviceTypeId, " +
                ":description, :master, :price, :updateDate)";

        Map<String, Object> params = ImmutableMap.<String, Object>builder()
                .put("userId", propertyData.getUserId())
                .put("propertyId", propertyData.getPropertyId())
                .put("serviceTypeId", propertyData.getServiceTypeId())
                .put("description", propertyData.getDescription())
                .put("master", propertyData.getMaster())
                .put("price", propertyData.getPrice())
                .put("updateDate", Date.valueOf(propertyData.getUpdateDate()))
                .build();

        namedParameterJdbcTemplate.execute(sql, params, PreparedStatement::execute);
    }

    @Override
    public void deletePropertyData(Long id) {
        String sql = "delete from property_data where id = :id";
        Map<String, Object> params = ImmutableMap.of("id", id);
        namedParameterJdbcTemplate.execute(sql, params, PreparedStatement::execute);
    }
}
