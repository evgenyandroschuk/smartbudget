package smartbudget.controller;

import groovy.lang.GroovyShell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.*;
import smartbudget.model.Expenses;
import smartbudget.model.ExpensesType;
import smartbudget.service.ExpensesService;
import smartbudget.service.ModelConverter;
import smartbudget.view.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * Created by evgenyandroshchuk on 19.12.17.
 */
@RestController
@EnableAutoConfiguration
@EnableConfigurationProperties
@RequestMapping(value = "/expenses")
public class BudgetController {

    @Autowired
    private ExpensesService expensesService;

    @RequestMapping(value = "/create/expensestype", method = RequestMethod.POST, consumes = "application/xml", produces = "application/xml")
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

    @RequestMapping( value = "/expensestype/active", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public ExpensesTypeListResponse getActiveExpensesType(@RequestHeader Map<String, String> httpHeaders) {

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

    @RequestMapping(value = "/create/expenses", method = RequestMethod.POST, consumes = "application/xml", produces = "application/xml")
    @ResponseBody
    public ExpensesResponse saveExpensesList(@RequestHeader String header, @RequestBody ExpensesRequest expensesRequest) {

        List<Expenses> expensesList = new LinkedList<>();

        ExpensesResponse expensesResponse = new ExpensesResponse();
        String status = "No items of Expenses";


        expensesRequest.getExpensesList().getExpenses().forEach(
                t -> {
                    Expenses e = new Expenses();
                    ExpensesType expensesType = expensesService.getExpensesType(t.getType());
                    if (expensesType == null) {
                        throw new RuntimeException("Expenses type not found by id = " + t.getType());
                    }
                    e.setMonth(expensesRequest.getMonth());
                    e.setYear(expensesRequest.getYear());
                    e.setDate(new java.util.Date());
                    e.setExpensesType(expensesService.getExpensesType(t.getType()));
                    e.setDescription(t.getDescription());
                    e.setAmount(t.getAmount());
                    expensesList.add(e);
                }

        );

        if(!expensesList.isEmpty()) {
            expensesService.saveExpensesList(expensesList);
            status = "Done";
        }

        expensesResponse.setStatus(status);
        expensesResponse.setRequest(expensesRequest);

        return expensesResponse;
    }


    /**
     *
     * @return This is used for JUnit test. DO NOT DELETE!!
     */
    @RequestMapping(value = "/")
    public String testString() {
        return "Connection test for Expenses";
    }

    /**
     *
     * @return string from groovy file. For testing purpose
     */
    @RequestMapping(value = "/kuku")
    public String kuku()  {

        GroovyShell shell = new GroovyShell();
        String result = null;
        try {
            result = (String) shell.evaluate(new FileReader("script.groovy"));
        } catch (IOException e) {
            result = "File not found!";
        }

        return result;
    }

}
