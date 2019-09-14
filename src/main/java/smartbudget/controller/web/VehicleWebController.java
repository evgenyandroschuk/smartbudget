package smartbudget.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import smartbudget.model.vehicles.VersionedVehicle;
import smartbudget.model.vehicles.VersionedVehicleData;
import smartbudget.model.vehicles.VersionedVehicleServiceType;
import smartbudget.service.postres.DateProvider;
import smartbudget.service.postres.vehicle.VehicleRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class VehicleWebController {

    private VehicleRepository vehicleRepository;
    private DateProvider dateProvider;

    @Autowired
    public VehicleWebController(VehicleRepository vehicleRepository, DateProvider dateProvider) {
        this.vehicleRepository = vehicleRepository;
        this.dateProvider = dateProvider;
    }

    @RequestMapping(value = "/vehicle/", method = RequestMethod.GET)
    public String vehicle(Model model) {
        model.addAttribute("vehicles", vehicleRepository.getVehicles(DEFAULT_USER));
        return "vehicle/vehicle";
    }

    @RequestMapping(value = "vehicle/details", method = RequestMethod.GET)
    public String vehicleDetails(Model model, @RequestParam(value="vehicle") String vehicleId) {
        int id = Integer.parseInt(vehicleId);
        VersionedVehicle versionedVehicle = vehicleRepository.getVehicles(DEFAULT_USER)
                .stream()
                .filter(t -> t.getVehicleId() == id)
                .findFirst()
                .get();

        LocalDate startDate = LocalDate.parse("2010-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        List<VersionedVehicleData>  vehicleData = vehicleRepository.findVehicleDataByPeriod(
                DEFAULT_USER, startDate, dateProvider.now()
        ).stream().filter(t -> t.getVehicleId() == id).collect(Collectors.toList());
        model.addAttribute("vehicle", versionedVehicle);
        model.addAttribute("vehicleData", vehicleData);
        return "vehicle/vehicle_details";
    }

    @RequestMapping(value = "vehicle/data/add", method = RequestMethod.GET)
    public String vehicleDetailsAdd(Model model, @RequestParam int vehicleId) {
        model.addAttribute("services", vehicleRepository.getVehicleServiceTypes(DEFAULT_USER));
        model.addAttribute("vehicleId", vehicleId);
        return "vehicle/vehicle_data_add";
    }

    @RequestMapping(value = "/vehicle/data/add/response", method = RequestMethod.GET)
    public String vehicleAddDetailsResponse(
            Model model,
            @RequestParam int vehicleId,
            @RequestParam int service,
            @RequestParam String description,
            @RequestParam int mileAge,
            @RequestParam String price,
            @RequestParam String date
    ) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        VersionedVehicleServiceType serviceType = vehicleRepository.findServiceTypeById(DEFAULT_USER, service);
        VersionedVehicleData vehicleData = new VersionedVehicleData(
                DEFAULT_USER,
                vehicleId,
                serviceType,
                description,
                mileAge, new BigDecimal(price).setScale(2),
                localDate
        );
        model.addAttribute("vehicleData", vehicleData);
        vehicleRepository.createVehicleData(vehicleData);
        return "vehicle/vehicle_data_add_response";
    }

    private static final int DEFAULT_USER = 1;
}
