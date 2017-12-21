package smartbudget.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;
import smartbudget.model.ExpensesType;
import smartbudget.service.ExpensesService;
import smartbudget.view.ExpensesTypeRequest;
import smartbudget.view.ExpensesTypeResponse;

/**
 * Created by evgenyandroshchuk on 19.12.17.
 */
@RestController
@EnableAutoConfiguration
public class BudgetController {


    @Autowired
    private ExpensesService expensesService;



    @RequestMapping(value = "/create_expenses_type", method = RequestMethod.POST, consumes = "application/xml", produces = "application/xml")
    public @ResponseBody ExpensesTypeResponse createExpensesType(@RequestBody ExpensesTypeRequest request) {
        //GetMapping

        ExpensesType expensesType = new ExpensesType(request.getDesc(), request.getDescRus(), request.isIncome(), request.isActive());
        expensesService.saveExpensesType(expensesType);

        ExpensesTypeResponse response = new ExpensesTypeResponse();
        response.setRequest(request);
        response.setStatus("Completed");
        return response;
    }


}
