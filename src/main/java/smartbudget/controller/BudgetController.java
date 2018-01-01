package smartbudget.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.*;
import smartbudget.model.ExpensesType;
import smartbudget.service.ExpensesService;
import smartbudget.service.ModelConverter;
import smartbudget.view.ExpensesTypeListResponse;
import smartbudget.view.ExpensesTypeView;
import smartbudget.view.ExpensesTypeResponse;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by evgenyandroshchuk on 19.12.17.
 */
@RestController
@EnableAutoConfiguration
@EnableConfigurationProperties
public class BudgetController {


    @Autowired
    private ExpensesService expensesService;



    @RequestMapping(value = "/create_expenses_type", method = RequestMethod.POST, consumes = "application/xml", produces = "application/xml")
    @ResponseBody
    public ExpensesTypeResponse createExpensesType(@RequestBody ExpensesTypeView request) {
        //GetMapping

        boolean isActive = request.isActive() == null ? true : request.isActive();
        boolean isIncome = request.isIncome() == null ? false :request.isIncome();
        ExpensesType expensesType = new ExpensesType(request.getDescription(), request.getDescriptionRus(), isIncome, isActive);
        expensesService.saveExpensesType(expensesType);

        ExpensesTypeResponse response = new ExpensesTypeResponse();
        response.setRequest(request);
        response.setStatus("Completed");
        return response;
    }

    @RequestMapping(value = "/expensestype/active", method = RequestMethod.GET, produces = "application/xml")
    public ExpensesTypeListResponse getActiveExpensesType() {
        ExpensesTypeListResponse response = new ExpensesTypeListResponse();
        List<ExpensesTypeView> listForView = new LinkedList<>();
        expensesService.findActiveExpensesType().forEach(
                t -> listForView.add(ModelConverter.convertToExpensesTypeView(t))

        );
        response.setExpensesTypeList(listForView);
        return response;
    }

    @RequestMapping(value = "/expensestype/all", method = RequestMethod.GET, produces = "application/xml")
    public ExpensesTypeListResponse getAllExpensesType() {
        ExpensesTypeListResponse response = new ExpensesTypeListResponse();
        List<ExpensesTypeView> typeViewList = new LinkedList<>();
        expensesService.findAllExpensesType().forEach(
                t -> typeViewList.add(ModelConverter.convertToExpensesTypeView(t))
        );
        response.setExpensesTypeList(typeViewList);
        return response;
    }



    @RequestMapping(value = "/")
    public String testString() {
        return "test String";
    }

    @RequestMapping(value = "/kuku")
    public String kuku() {
        return "String = kuku";
    }



}
