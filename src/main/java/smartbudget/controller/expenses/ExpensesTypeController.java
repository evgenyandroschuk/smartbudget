package smartbudget.controller.expenses;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import smartbudget.model.ExpensesType;
import smartbudget.service.ApplicationProjectService;
import smartbudget.util.ApplicationProperties;
import smartbudget.service.ExpensesService;
import smartbudget.util.ConfigProperties;
import smartbudget.view.json.expenses.type.TypeResponse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/expenses/exptype")
@EnableAutoConfiguration
public class ExpensesTypeController {



    private ApplicationProjectService service;


    private ApplicationProperties properties;

    @Autowired
    public ExpensesTypeController(ApplicationProjectService service, ApplicationProperties properties) {
        this.service = service;
        this.properties = properties;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TypeResponse getAllTypeResponse() {

        List<ExpensesType> types;

        types = service.getExpensesType(properties);

        if(types !=null && types.isEmpty()) {
            return new TypeResponse("properties = " , ImmutableList.of());
        }

        return new TypeResponse("Expenses type", types);
    }

}
