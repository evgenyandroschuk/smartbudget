package smartbudget.controller.expenses;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartbudget.model.ExpensesData;
import smartbudget.model.ExpensesTypeData;
import smartbudget.service.ExpensesFactory;
import smartbudget.service.ExpensesService;
import smartbudget.util.AppProperties;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(value = "/budget")
public class ExpensesOperationController {

    ExpensesFactory expensesFactory;

    @Autowired
    public ExpensesOperationController(AppProperties properties) {
        String name = properties.getProperty("app.impl");
        expensesFactory = new ExpensesFactory(properties, name);
    }

    @RequestMapping(value = "/expenses", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ExpensesData> getExpenses(
            @RequestParam(value = "id", required = false, defaultValue = "") Long id,
            @RequestParam(value = "type",  required = false, defaultValue = "") Integer type,
            @RequestParam(value = "year", required = false, defaultValue = "") Integer year,
            @RequestParam(value = "month", required = false, defaultValue = "") Integer month
    ) {
        List<ExpensesData> result = new LinkedList<>();

        LocalDate now = LocalDate.now();
        year = year == null ? now.getYear() : year;

        if(id != null) {
            return ImmutableList.of(expensesFactory.getExpensesService().findById(id));
        }

        if(type == null) {
            if (month == null) {
                result.addAll(expensesFactory.getExpensesService().findByYear(year)) ;
            } else {
                result.addAll(expensesFactory.getExpensesService().findByMonthYear(month, year));
            }

        } else {
            if (month == null) {
                result.addAll(expensesFactory.getExpensesService().findByTypeYear(type, year));
            } else {
                result.addAll(expensesFactory.getExpensesService().findByTypeMonthYear(type, month, year));
            }
        }
        return result;
    }

    @RequestMapping(value = "/expenses/description", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ExpensesData> getExpensesByDesc(
            @RequestParam String description,
            @RequestParam String start,
            @RequestParam String end
    ) {
        return expensesFactory.getExpensesService().findByDescription(description, start, end);
    }

    @RequestMapping(value = "/expenses/save", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String saveExpenses(
            @RequestParam Integer month,
            @RequestParam Integer year,
            @RequestParam Integer type,
            @RequestParam Double amount,
            @RequestParam(value = "description", required = false, defaultValue = "") String description
    ) {

        ExpensesData data = ExpensesData.of(month, year, type, description, amount, LocalDate.now());
        expensesFactory.getExpensesService().create(data);
        return "Expenses saving success";
    }

    @RequestMapping(value = "/expenses/delete", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String deleteExpenses(@RequestParam Long id) {
        expensesFactory.getExpensesService().delete(id);
        return "Expenses with id = " + id + " successfully deleted";
    }

    @RequestMapping(value = "/expenses/update", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String updateExpenses(
            @RequestParam Long id,
            @RequestParam (value = "month", required = false, defaultValue = "")Integer month,
            @RequestParam (value = "year", required = false, defaultValue = "") Integer year,
            @RequestParam (value = "type", required = false, defaultValue = "") Integer type,
            @RequestParam (value = "amount", required = false, defaultValue = "") Double amount,
            @RequestParam(value = "description", required = false, defaultValue = "") String description
    ) {

        ExpensesService service = expensesFactory.getExpensesService();
        ExpensesData oldData = service.findById(id);
        int newMonth = month == null ? oldData.getMonth() : month;
        int newYear = year == null ? oldData.getYear() : year;
        int newType = type == null ? oldData.getType() : type;
        double newAmount = amount == null ? oldData.getAmount() : amount;
        String newDescription = description == null ? oldData.getDescription() : description;

        ExpensesData newData = ExpensesData.of(newMonth, newYear, newType, newDescription, newAmount, oldData.getDate());
        service.update(id, newData);

        return "Expenses successfully updated";
    }


    /*---------------------------------EXPENSES TYPE-----------------------*/

    @RequestMapping(value = "/expenses/type", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ExpensesTypeData> expensesTypeData(
            @RequestParam (value = "id", required = false, defaultValue = "")Integer id,
            @RequestParam (value = "is_active", required = false, defaultValue = "")Boolean isActive
    ) {
        try {
            if (id != null) {
                return ImmutableList.of(expensesFactory.getExpensesTypeService().getById(id));
            }
            if (isActive == null || isActive == true) {
                return expensesFactory.getExpensesTypeService().getActiveType();
            }
            return expensesFactory.getExpensesTypeService().getAll();

        } catch (RuntimeException e) {
            throw e;
        }
    }

    /**
     *
     * @param id
     * @param description
     * @param descriptionRus
     * @param isIncome
     * @param isActive
     * http://localhost:7004/budget/expenses/type/save?id=13&description=TestRu&description_ru=ТестРу&is_income=false&is_active=true
     * @return
     */
    @RequestMapping(value = "/expenses/type/save", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String saveExpensesType(
            @RequestParam int id,
            @RequestParam String description,
            @RequestParam(value = "description_ru") String descriptionRus,
            @RequestParam(value = "is_income") boolean isIncome,
            @RequestParam(value = "is_active")  boolean isActive
    ) {

        ExpensesTypeData data = ExpensesTypeData.of(id, description, descriptionRus, isIncome, isActive);
        expensesFactory.getExpensesTypeService().save(data);

        return "Expenses type successfully saved";
    }

    /**
     *
     * @param id
     * @param description
     * @param descriptionRus
     * @param isIncome
     * @param isActive
     * http://localhost:7004/budget/expenses/type/update?id=13&description=TestRu2&description_ru=%D0%A2%D0%B5%D1%81%D1%82%D0%A2%D0%B5%D1%81%D1%822&is_income=true&is_active=false
     * @return
     */
    @RequestMapping(value = "/expenses/type/update", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String updateExpensesType(
            @RequestParam int id,
            @RequestParam String description,
            @RequestParam(value = "description_ru") String descriptionRus,
            @RequestParam(value = "is_income") boolean isIncome,
            @RequestParam(value = "is_active")  boolean isActive
    ){
        ExpensesTypeData data = ExpensesTypeData.of(description, descriptionRus, isIncome, isActive);
        expensesFactory.getExpensesTypeService().update(id ,data);
        return "Expenses type successfully updated";
    }
}
