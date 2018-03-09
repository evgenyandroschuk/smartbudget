package smartbudget.controller.expenses;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import smartbudget.model.Expenses;
import smartbudget.service.ApplicationProjectService;
import smartbudget.util.ApplicationProperties;
import smartbudget.view.json.expenses.type.ExpensesAddResponse;
import smartbudget.view.json.expenses.type.ExpensesData;
import smartbudget.view.json.expenses.type.ExpensesRequest;
import smartbudget.view.json.expenses.type.ExpensesResponse;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(value = "/expenses")
public class ExpensesController {

    private ApplicationProjectService service;

    @Autowired
    public ExpensesController(ApplicationProjectService service, ApplicationProperties properties) {
        service.setProperties(properties);
        this.service = service;
    }


    @RequestMapping(value = "/month_year", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ExpensesResponse getExpensesByMonthYear(@RequestParam String month, @RequestParam String year) {

        int intMonth = Integer.parseInt(month);
        int intYear = Integer.parseInt(year);

        List<Expenses> expensesList;
        expensesList = service.getExpensesByMonthYear(intMonth, intYear);
        if(expensesList != null && expensesList.isEmpty() ) {
            return new ExpensesResponse("Expneses list is empty", ImmutableList.of());
        }
        return new ExpensesResponse("Expneses", expensesList);
    }


    @RequestMapping(value = "/year", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ExpensesResponse getExpensesByYear(@RequestParam String year) {
        int intYear = Integer.parseInt(year);
        List<Expenses> expensesList;
        expensesList = service.getExpensesByYear(intYear);
        if(expensesList != null && expensesList.isEmpty()) {
            return new ExpensesResponse("Expneses list is empty", ImmutableList.of());
        }
        return new ExpensesResponse("Expneses", expensesList);
    }

    @RequestMapping(value = "/type_month_year", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ExpensesResponse getExpensesByTypeMonthYear(@RequestParam String type,
                                                       @RequestParam String month,
                                                       @RequestParam String year) {
        int intType = Integer.parseInt(type);
        int intMonth = Integer.parseInt(month);
        int intYear = Integer.parseInt(year);

        List<Expenses> expensesList;
        expensesList = service.getExpensesByTypeMonthYear(intType, intMonth, intYear);
        if(expensesList != null && expensesList.isEmpty()) {
            return new ExpensesResponse("Expneses list is empty", ImmutableList.of());
        }
        return new ExpensesResponse("Expneses", expensesList);

    }

    @RequestMapping(value = "/type_year", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ExpensesResponse getExpensesByTypeYear(@RequestParam String type, @RequestParam String year) {

        int intType = Integer.parseInt(type);
        int intYear = Integer.parseInt(year);

        List<Expenses> expensesList;
        expensesList = service.getExpensesByTypeYear(intType, intYear);

        if(expensesList != null && expensesList.isEmpty()) {
            return new ExpensesResponse("Expneses list is empty", ImmutableList.of());
        }
        return new ExpensesResponse("Expneses", expensesList);
    }

    @RequestMapping(value = "/type_start_end", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ExpensesResponse getExpensesByTypePeriod(@RequestParam String type,
                                                    @RequestParam String startDate,
                                                    @RequestParam String endDate) {
        int intType = Integer.parseInt(type);
        List<Expenses> expensesList;
        expensesList = service.getExpensesByTypeAndPeriod(intType, startDate, endDate);

        if(expensesList != null && expensesList.isEmpty()) {
            return new ExpensesResponse("Expneses list is empty", ImmutableList.of());
        }
        return new ExpensesResponse("Expneses", expensesList);
    }

    @RequestMapping(value = "/description_start_end", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ExpensesResponse getExpensesByDescriptionPeriod(@RequestParam String description,
                                                           @RequestParam String startDate,
                                                           @RequestParam String endDate) {
        List<Expenses> expensesList;
        expensesList = service.getExpensesTypeByDescriptionPeriod(description, startDate, endDate);

        if(expensesList != null && expensesList.isEmpty()) {
            return new ExpensesResponse("Expneses list is empty", ImmutableList.of());
        }
        return new ExpensesResponse("Expneses", expensesList);
    }

    @RequestMapping(value = "/add_expenses", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = "application/json")
    public ExpensesAddResponse addExpenses(@RequestBody ExpensesRequest request) {

        boolean status = service.addExpenses(request);
        if(status){
            return new ExpensesAddResponse("Success",request.getExpensesData());
        }
        return new ExpensesAddResponse("No row added",request.getExpensesData());



    }

    @RequestMapping(value = "/test_request", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ExpensesRequest testRequest() {
        List<ExpensesData> expensesDataList = new LinkedList<>();
        expensesDataList.add(new ExpensesData("Desc01", 2,1200));
        expensesDataList.add(new ExpensesData("Desc02", 3,1400));
        return new ExpensesRequest("2018-03-10", 3, 2018, expensesDataList);
    }
}
