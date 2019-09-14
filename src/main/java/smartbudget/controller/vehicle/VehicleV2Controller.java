package smartbudget.controller.vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartbudget.model.vehicles.VersionedVehicle;
import smartbudget.model.vehicles.VersionedVehicleData;
import smartbudget.model.vehicles.VersionedVehicleServiceType;
import smartbudget.service.postres.vehicle.VehicleRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(value = "/vehicle/v2/")
public class VehicleV2Controller {

    private final VehicleRepository vehicleRepository;
    private final DateTimeFormatter YYYY_MM_DD_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public VehicleV2Controller(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @RequestMapping(value = "/vehicles", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VersionedVehicle> getVehicles(@RequestParam(value = "user_id") Integer userId) {
        return vehicleRepository.getVehicles(userId);
    }

    @RequestMapping(value = "/servicetype", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VersionedVehicleServiceType> getServiceTypes(@RequestParam(value = "user_id") Integer userId) {
        return vehicleRepository.getVehicleServiceTypes(userId);
    }

    @RequestMapping(value = "/data/last", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VersionedVehicleData> getVehicleData(
            @RequestParam(value = "user_id") Integer userId,
            @RequestParam(value = "start_date") String start
    ) {
        LocalDate startDate = LocalDate.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.now().plusDays(1);
        return vehicleRepository.findVehicleDataByPeriod(userId, startDate, endDate);
    }

    @RequestMapping(value = "/data/save", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String saveVehicleData(
            @RequestParam(value = "user_id") int userId,
            @RequestParam int vehicle,
            @RequestParam int type,
            @RequestParam(value = "description", required = false, defaultValue = "") String description,
            @RequestParam int mileage,
            @RequestParam String price,
            @RequestParam String date
    ) {
        LocalDate updateDate = LocalDate.parse(date, YYYY_MM_DD_FORMAT);
        BigDecimal priceValue = new BigDecimal(price.replace(",", "."));
        VersionedVehicleServiceType serviceType = vehicleRepository.findServiceTypeById(1, type);
        VersionedVehicleData versionedVehicleData = new VersionedVehicleData(
                userId,
                vehicle,
                serviceType,
                description,
                mileage,
                priceValue.setScale(2),
                updateDate
        );
        vehicleRepository.createVehicleData(versionedVehicleData);
        return "successfully saved";
    }

}
