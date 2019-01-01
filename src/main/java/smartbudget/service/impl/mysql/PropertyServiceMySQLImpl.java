package smartbudget.service.impl.mysql;

import smartbudget.model.services.Property;
import smartbudget.model.services.PropertyServiceData;
import smartbudget.model.services.PropertyServiceType;
import smartbudget.service.impl.AbstractService;
import smartbudget.service.services.PropertyService;
import smartbudget.util.HelpUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class PropertyServiceMySQLImpl extends AbstractService implements PropertyService {

    private List<PropertyServiceType> propertyServiceTypeList = new LinkedList<>();
    private List<Property> propertyList = new LinkedList<>();

    public PropertyServiceMySQLImpl(Connection connection) {
        super(connection);
    }

    @Override
    public List<Property> getProperties() {
        if (propertyList.isEmpty()) {
            refreshProperties();
        }
        return propertyList;
    }

    @Override
    public List<PropertyServiceType> getPropertyServiceTypes() {
        if (propertyServiceTypeList.isEmpty()) {
            refreshServiceTypes();
        }
        return propertyServiceTypeList;
    }

    @Override
    public List<PropertyServiceData> getPropertyServiceDataByPeriod(
            Property property, LocalDate startDate, LocalDate endDate
    ) {
        String query = "select * from communication_data " +
                "where property_id = ? and update_date between ? and ? order by id desc";
        int propertyId = property.getId();
        String start = formattedDate(startDate);
        String end = formattedDate(endDate);
        List<PropertyServiceData> serviceDataList = new LinkedList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, propertyId);
            statement.setString(2, start);
            statement.setString(3, end);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("id");
                int serviceTypeId = rs.getInt("com_service_type_id");
                PropertyServiceType serviceType = getServiceTypeById(serviceTypeId);
                String description = rs.getString("description");
                String name = rs.getString("master_name");
                double price = rs.getDouble("price");
                Date date = rs.getDate("update_date");
                LocalDate updateDate = HelpUtils.convertDateToLocalDate(date);
                PropertyServiceData data = new PropertyServiceData(
                        id, property,serviceType, description, name, price, updateDate
                );
                serviceDataList.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return serviceDataList;
    }

    @Override
    public void savePropertyServiceData(PropertyServiceData propertyServiceData) {
        String query = "insert into communication_data (id, property_id, com_service_type_id, " +
                        "description, master_name, price, update_date)\n" +
                        "values(get_id(4), ?, ?, ?, ?, ?, ?)";
        int propertyId = propertyServiceData.getProperty().getId();
        int serviceTypeId = propertyServiceData.getPropertyServiceType().getId();
        String description = propertyServiceData.getDescription();
        String name = propertyServiceData.getName();
        double price = propertyServiceData.getPrice();
        String updateDate = formattedDate(propertyServiceData.getUpdateDate());
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, propertyId);
            statement.setInt(2, serviceTypeId);
            statement.setString(3, description);
            statement.setString(4, name);
            statement.setDouble(5, price);
            statement.setString(6, updateDate);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshServiceTypes() {
        propertyServiceTypeList.clear();
        String query = "select * from com_service_type";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                String description = rs.getString("description");
                propertyServiceTypeList.add(new PropertyServiceType(id, description));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshProperties() {
        propertyList.clear();
        String query = "select * from property";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String description = rs.getString("description");
                propertyList.add(new Property(id, description));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PropertyServiceType getServiceTypeById(int id) {
        if (propertyServiceTypeList.isEmpty()) {
            refreshServiceTypes();
        }
        return propertyServiceTypeList.stream().filter(t -> t.getId()==id).findFirst().get();
    }

    private String formattedDate(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

}
