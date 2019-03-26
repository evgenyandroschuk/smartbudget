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
import smartbudget.service.ExpensesService;
import smartbudget.service.services.DbServiceFactory;
import smartbudget.util.Numerator;
import smartbudget.util.SystemParams;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/budget/")
public class ExpensesOperationController {


    private  final DbServiceFactory dbServiceFactory;

    @Autowired
    public ExpensesOperationController(DbServiceFactory dbServiceFactory) {
        this.dbServiceFactory = dbServiceFactory;
    }

    @RequestMapping(value = "/expenses", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ExpensesData> getExpenses(
            @RequestParam(value = "id", required = false, defaultValue = "") Long id,
            @RequestParam(value = "type",  required = false, defaultValue = "") Integer type,
            @RequestParam(value = "year", required = false, defaultValue = "") Integer year,
            @RequestParam(value = "month", required = false, defaultValue = "") Integer month
    ) throws SQLException {
        List<ExpensesData> result = new LinkedList<>();

        LocalDate now = LocalDate.now();
        year = year == null ? now.getYear() : year;

        if(id != null) {
            return ImmutableList.of(dbServiceFactory.getExpensesService().findById(id));
        }

        if(type == null) {
            if (month == null) {
                result.addAll(dbServiceFactory.getExpensesService().findByYear(year)) ;
            } else {
                result.addAll(dbServiceFactory.getExpensesService().findByMonthYear(month, year));
            }

        } else {
            if (month == null) {
                result.addAll(dbServiceFactory.getExpensesService().findByTypeYear(type, year));
            } else {
                result.addAll(dbServiceFactory.getExpensesService().findByTypeMonthYear(type, month, year));
            }
        }
        return result;
    }

    @RequestMapping(value = "/expenses/description", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ExpensesData> getExpensesByDesc(
            @RequestParam String description,
            @RequestParam String start,
            @RequestParam String end
    ) throws SQLException {
        return dbServiceFactory.getExpensesService().findByDescription(description, start, end);
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
        dbServiceFactory.getExpensesService().create(data);
        return "Expenses saving success";
    }

    @RequestMapping(value = "/expenses/delete", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String deleteExpenses(@RequestParam Long id) {
        dbServiceFactory.getExpensesService().delete(id);
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
    ) throws SQLException {

        ExpensesService service = dbServiceFactory.getExpensesService();
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
        double currentId = dbServiceFactory.getCommonService().getMaxIdByNumerator(Numerator.EXPENSES);
        dbServiceFactory.getCommonService().createReplaceUserParams(1, SystemParams.EXPENSES_OPENING_BALANCE, amount);
        dbServiceFactory.getCommonService().createReplaceUserParams(1, SystemParams.EXPENSES_OPENING_ID, currentId);

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

        double fundCurrentId = dbServiceFactory.getCommonService().getMaxIdByNumerator(Numerator.FUND);
        dbServiceFactory.getCommonService().createReplaceUserParams(1, SystemParams.DOLLAR_OPENING_BALANCE, dollar);
        dbServiceFactory.getCommonService().createReplaceUserParams(1, SystemParams.EURO_OPENING_BALANCE, euro);
        dbServiceFactory.getCommonService().createReplaceUserParams(1, SystemParams.RUB_OPENING_BALANCE, rub);
        dbServiceFactory.getCommonService().createReplaceUserParams(1, SystemParams.FUND_OPENING_ID, fundCurrentId);

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
                return ImmutableList.of(dbServiceFactory.getExpensesTypeService().getById(id));
            }
            if (isActive == null || isActive == true) {
                return dbServiceFactory.getExpensesTypeService().getActiveType();
            }
            return dbServiceFactory.getExpensesTypeService().getAll();

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
        dbServiceFactory.getExpensesTypeService().save(data);

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
        dbServiceFactory.getExpensesTypeService().update(id ,data);
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
        return dbServiceFactory.getCommonService().getUserParamValue(userId, paramId);
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
        dbServiceFactory.getCommonService().createReplaceUserParams(userId, paramId, value);
        return "User param successfully updated";
    }

    @RequestMapping(value = "/fund", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Map<String,String>> getFunds() {
        return dbServiceFactory.getExpensesService().getFunds();
    }

    @RequestMapping(value = "/fund/save", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String saveFund(
            @RequestParam int currency,
            @RequestParam String description,
            @RequestParam Double price,
            @RequestParam Double amount
    ){
        dbServiceFactory.getExpensesService().saveFund(currency, description, price, amount);
        return "Fund successfully saved";
    }

    @RequestMapping(value = "/fund/delete", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String deleteFund(
            @RequestParam int id
    ) {
        dbServiceFactory.getExpensesService().deleteFund(id);
        return "Fund successfully deleted";
    }

    @RequestMapping(value = "/currency/update", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String updateCurrency(@RequestParam String currency, @RequestParam String amount) {
        int currencyId = Integer.parseInt(currency);
        double price = Double.parseDouble(amount);
        dbServiceFactory.getCommonService().updateCurrencyCost(currencyId, price);
        return "Currency successfully updated";
    }

    /*--------------Statistics-----------------*/

    @RequestMapping(value = "/statistic/expenses", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, String> getTotalExpensesByMonthYear(
            @RequestParam (value = "year")Integer year,
            @RequestParam (value = "month", required = false, defaultValue = "")Integer month
    ) {
        if (month != null) {
            return dbServiceFactory.getExpensesService().getStatisticByYearMonth(year, month);
        }
        return dbServiceFactory.getExpensesService().getStatisticByYear(year);
    }

    @RequestMapping(value = "/statistic/current", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, String> getCurrentStatistic() {
        LocalDate now = LocalDate.now();

        Map<String, String> result = getTotalExpensesByMonthYear(now.getYear(), now.getMonthValue());
        double startAmount = dbServiceFactory.getCommonService().getUserParamValue(1, SystemParams.EXPENSES_OPENING_BALANCE);
        String startAmountDate = dbServiceFactory.getCommonService().getUserParamUpdateDateString(1, SystemParams.EXPENSES_OPENING_BALANCE);
        double startIdDouble = dbServiceFactory.getCommonService().getUserParamValue(1, SystemParams.EXPENSES_OPENING_ID);
        int startId = Integer.parseInt(String.format("%.0f", startIdDouble));

        result.put("open_balance_amount", String.format("%.2f", startAmount));
        LocalDate balance_date = LocalDate.parse(startAmountDate,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        result.put("open_balance_date", balance_date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        double expensesAmount = dbServiceFactory.getExpensesService().getLastPeriodExpenses(startId);
        double incomeAmount = dbServiceFactory.getExpensesService().getIncomeAmount(startId);

        double restAmount = startAmount - expensesAmount + incomeAmount;
        result.put("rest_amount", String.format("%.2f", restAmount));

        //Fund values
        double fundIdDouble = dbServiceFactory.getCommonService().getUserParamValue(1, SystemParams.FUND_OPENING_ID);
        int fundId = Integer.parseInt(String.format("%.0f", fundIdDouble));
        double startDollarAmount = dbServiceFactory.getCommonService().getUserParamValue(1, SystemParams.DOLLAR_OPENING_BALANCE);
        double startEuroAmount = dbServiceFactory.getCommonService().getUserParamValue(1, SystemParams.EURO_OPENING_BALANCE);
        double startRubAmount = dbServiceFactory.getCommonService().getUserParamValue(1, SystemParams.RUB_OPENING_BALANCE);

        Map<String, Double> currenciesMap = dbServiceFactory.getExpensesService().getCurrencies();
        double dollarPrice = currenciesMap.get("Dollar");
        double euroPrice = currenciesMap.get("Euro");

        double fundDollarAmount = dbServiceFactory.getExpensesService().getFundAmount(fundId, 1);
        double fundEuroAmount = dbServiceFactory.getExpensesService().getFundAmount(fundId, 2);
        double fundRubAmount = dbServiceFactory.getExpensesService().getFundAmount(fundId, 3);

        double restDollarAmount = (startDollarAmount + fundDollarAmount);
        double restEuroAmount = (startEuroAmount + fundEuroAmount);
        double restRubAmount =  startRubAmount + fundRubAmount;
        double restDollarAmountRub = restDollarAmount * dollarPrice;
        double restEuroAmountRub = restEuroAmount * euroPrice;
        double restAllAmount = restDollarAmountRub + restEuroAmountRub + restRubAmount + restAmount;

        result.put("rest_dollar_amount", String.format("%.2f",restDollarAmount));
        result.put("rest_dollar_in_rub", String.format("%.2f",restDollarAmountRub));

        result.put("rest_euro_amount", String.format("%.2f", restEuroAmount));
        result.put("rest_euro_in_rub", String.format("%.2f", restEuroAmountRub));

        result.put("rest_rub_amount", String.format("%.2f",restRubAmount));
        result.put("rest_all_amount", String.format("%.2f", restAllAmount));
        result.put("dollar_price", String.format("%.2f", dollarPrice));
        result.put("euro_price", String.format("%.2f", euroPrice));

        return result;
    }

    /*  http://localhost:7004/budget/statistic/expenses/yearly?year=2017    */
    @RequestMapping(value = "/statistic/expenses/yearly", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Map<String, String>> getYearlyExpenses(@RequestParam (value = "year")Integer year) {
        return dbServiceFactory.getExpensesService().getYearlyExpenses(year);

    }


}
