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
import smartbudget.util.Numerator;
import smartbudget.util.SystemParams;
import smartbudget.util.AppProperties;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/budget/")
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

    /**
     *
     * @param amount
     * http://localhost:7004/budget/expenses/update/expenses_balance?amount=134112
     * @return
     */
    @RequestMapping(value = "/expenses/update/expenses_balance", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String updateExpensesBalance(@RequestParam Double amount) {
        double currentId = expensesFactory.getCommonService().getMaxIdByNumerator(Numerator.EXPENSES);
        expensesFactory.getCommonService().createReplaceUserParams(1, SystemParams.EXPENSES_OPENING_BALANCE, amount);
        expensesFactory.getCommonService().createReplaceUserParams(1, SystemParams.EXPENSES_OPENING_ID, currentId);

        return "Expenses balance successfully updated";
    }


    /**
     *
     * @param dollar
     * @param euro
     * @param rub
     * http://localhost:7004/budget/expenses/update/fund_balance?dollar=12001&euro=10001&rub=12001
     * @return
     */
    @RequestMapping(value = "/expenses/update/fund_balance", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String updateFundBalance(
            @RequestParam(value = "dollar") Double dollar,
            @RequestParam(value = "euro") Double euro,
            @RequestParam(value = "rub") Double rub
    ) {

        double fundCurrentId = expensesFactory.getCommonService().getMaxIdByNumerator(Numerator.FUND);
        expensesFactory.getCommonService().createReplaceUserParams(1, SystemParams.DOLLAR, dollar);
        expensesFactory.getCommonService().createReplaceUserParams(1, SystemParams.EURO, euro);
        expensesFactory.getCommonService().createReplaceUserParams(1, SystemParams.RUB, rub);
        expensesFactory.getCommonService().createReplaceUserParams(1, SystemParams.FUND, fundCurrentId);

        return "Fund balance successfully updated";
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

    /*---------------------------------Other operations-----------------------*/

    /**
     *
     * @param userId
     * @param paramId
     * http://localhost:7004/budget/common/param?user_id=1&param_id=1
     * @return
     */
    @RequestMapping(value = "/common/param", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public double getUserParamValue(
            @RequestParam(value = "user_id") int userId,
            @RequestParam(value = "param_id") int paramId
    ) {
        return expensesFactory.getCommonService().getUserParamValue(userId, paramId);
    }


    /**
     *
     * @param userId
     * @param paramId
     * @param value
     * http://localhost:7004/budget/common/param/update?user_id=1&param_id=3&value=13
     * @return
     */
    @RequestMapping(value = "/common/param/update", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String updateUserParamValue(
            @RequestParam(value = "user_id") int userId,
            @RequestParam(value = "param_id") int paramId,
            @RequestParam(value = "value") double value
    ) {
        expensesFactory.getCommonService().createReplaceUserParams(userId, paramId, value);
        return "User param successfully updated";
    }

    @RequestMapping(value = "/fund", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Map<String,String>> getFunds() {
        return expensesFactory.getCommonService().getQueryRequest("select * from fund order by id");
    }

    @RequestMapping(value = "/fund/save", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String saveFund(
            @RequestParam int currency,
            @RequestParam String description,
            @RequestParam Double price,
            @RequestParam Double amount
    ){
        String query = String.format("insert into fund values(get_id(5), sysdate(), %d, %.2f, '%s', %.2f)", currency, amount,  description, price);
        expensesFactory.getCommonService().execute(query);
        return "Fund successfully saved";
    }

    @RequestMapping(value = "/fund/delete", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String deleteFund(
            @RequestParam int id
    ) {
        expensesFactory.getCommonService().execute("delete from fund where id = " + id);
        return "Fund successfully deleted";
    }

}
