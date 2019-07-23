package smartbudget.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @RequestMapping(value =  "/healthCheck")
    public String healthCheck() {
        return "HealthCheck: Ok";
    }


}
