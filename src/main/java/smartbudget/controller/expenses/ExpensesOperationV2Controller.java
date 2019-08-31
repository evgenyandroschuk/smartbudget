package smartbudget.controller.expenses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartbudget.model.expenses.CurrentStatistic;
import smartbudget.model.expenses.ExpensesData;
import smartbudget.model.expenses.ExpensesType;
import smartbudget.service.CommonRepository;
import smartbudget.service.postres.DateProvider;
import smartbudget.service.postres.expenses.ExpensesDataService;
import smartbudget.service.postres.expenses.ExpensesRepository;
import smartbudget.util.SystemParams;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/budget/v2/")
public class ExpensesOperationV2Controller {

    private ExpensesDataService expensesDataService;
    private ExpensesRepository expensesRepository;
    private DateProvider dateProvider;
    private CommonRepository commonRepository;

    @Autowired
    public ExpensesOperationV2Controller(
        ExpensesDataService expensesDataService,
        ExpensesRepository expensesRepository,
        DateProvider dateProvider,
        CommonRepository commonRepository
    ) {
        this.expensesDataService = expensesDataService;
        this.expensesRepository = expensesRepository;
        this.dateProvider = dateProvider;
        this.commonRepository = commonRepository;
    }

    @RequestMapping(value = "/expenses", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ExpensesData> getExpenses(
        @RequestParam(value = "user_id", required = false, defaultValue = "") Integer userId,
        @RequestParam(value = "id", required = false, defaultValue = "") Long id,
        @RequestParam(value = "type",  required = false, defaultValue = "") Integer type,
        @RequestParam(value = "year", required = false, defaultValue = "") Integer year,
        @RequestParam(value = "month", required = false, defaultValue = "") Integer month
    ) {
        LocalDate now = dateProvider.now();
        int y = year == null ? now.getYear() : year;
        if (id != null) {
            return expensesRepository.getExpensesDataById(userId, id);
        }

        if(type == null) {
            if(month == null) {
                return expensesRepository.getExpensesByYear(userId, y);
            } else {
                return expensesRepository.getExpensesByYearMonth(userId, y, month);
            }
        } else {
            if(month == null) {
                return expensesRepository.getExpensesByYear(userId, y)
                    .stream()
                    .filter(data -> data.getExpensesType().getExpensesTypeId() == type)
                    .collect(Collectors.toList());
            } else {
                return expensesRepository.getExpensesByYearMonth(userId, y, month)
                    .stream()
                    .filter(data -> data.getExpensesType().getExpensesTypeId() == type)
                    .collect(Collectors.toList());
            }
        }

    }

    @RequestMapping(value = "/statistic/current", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CurrentStatistic getCurrentStatistic(@RequestParam(value = "user_id")int userId) {
        return expensesDataService.getCurrentStatistic(userId);
    }

    @RequestMapping(value =  "/expenses/save")
    public String saveExpenses(
        @RequestParam(value = "user_id") Integer userId,
        @RequestParam Integer month,
        @RequestParam Integer year,
        @RequestParam Integer type,
        @RequestParam String amount,
        @RequestParam(value = "description", required = false, defaultValue = "") String description
    ) {

        BigDecimal amountValue = new BigDecimal(amount.replace(",", "."));
        ExpensesType expensesType = expensesRepository.getExpensesTypes(userId)
            .stream().filter(t -> type.equals(t.getExpensesTypeId())).findFirst()
            .orElseThrow(() -> new RuntimeException("Expenses type not found by Id = " + type));
        ExpensesData data = new ExpensesData(
            userId, month, year, expensesType, description, amountValue, LocalDate.now()
        );

        expensesRepository.saveExpenses(data);
        return "expenses successfully saved";
    }

    @RequestMapping(value =  "/expenses/types")
    public List<ExpensesType> getExpensesTypes(@RequestParam(value = "user_id") Integer userId) {
        return expensesRepository.getExpensesTypes(userId);
    }

    @RequestMapping(value =  "/expenses/balance/update")
    public String updateBalance(
        @RequestParam(value = "user_id") Integer userId,
        @RequestParam String amount
    ) {
        // Получить последниий id
        BigDecimal amountValue = new BigDecimal(amount.replace(",", "."));
        long expensesId = expensesRepository.getLastExpensesId(userId);
        commonRepository.createOrReplaceUserParams(
                userId, SystemParams.EXPENSES_OPENING_BALANCE, amountValue.setScale(2)
        );
        commonRepository.createOrReplaceUserParams(
                userId, SystemParams.EXPENSES_OPENING_ID, BigDecimal.valueOf(expensesId).setScale(2)
        );
        return "balance successfully updated";
    }

}
