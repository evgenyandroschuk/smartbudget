package smartbudget.controller.vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartbudget.db.DbUtil;
import smartbudget.model.vehicles.VehicleData;
import smartbudget.service.DbServiceFactory;
import smartbudget.util.AppProperties;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/vehicle/")
public class VehicleController {

    DbServiceFactory dbServiceFactory;

    @Autowired
    public VehicleController(AppProperties properties) throws SQLException {
        String name = properties.getProperty("app.impl");
        Connection connection = new DbUtil(properties).getConnect();
        dbServiceFactory = new DbServiceFactory(name, connection);
    }

    /**
     *
     * @param id
     * http://localhost:7004/vehicle/data?id=12
     * @return
     */
    @RequestMapping(value = "/data", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VehicleData getVehicleDataById(@RequestParam(value = "id") Long id) {
        return dbServiceFactory.getVehicleService().findVehicleDataById(id);
    }

    /**
     *
     * @param size
     * http://localhost:7004/vehicle/last/data?size=21
     * @return
     */
    @RequestMapping(value = "/data/last", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VehicleData> getLastVehicleData(@RequestParam(value = "size") int size) {
        return dbServiceFactory.getVehicleService().findLastVehicleData(size);
    }


    /**
     *
     * @param type
     * @param vehicle
     * @param description
     * @param mileage
     * @param price
     * http://localhost:7004/vehicle/data/save?type=2&vehicle=1&description=то60000&mileage=61200&price=39400
     * @return
     */
    @RequestMapping(value = "/data/save", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String saveVehicleData(
            @RequestParam int type,
            @RequestParam int vehicle,
            @RequestParam(value = "description", required = false, defaultValue = "") String description,
            @RequestParam int mileage,
            @RequestParam double price
    ) {
        VehicleData vehicleData = VehicleData.of(type, vehicle, description, mileage, price, LocalDate.now());
        dbServiceFactory.getVehicleService().createVehicleData(vehicleData);
        return "successfully saved";
    }
}
