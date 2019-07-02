package smartbudget.controller.expenses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartbudget.model.expenses.CurrentStatistic;
import smartbudget.model.expenses.ExpensesData;
import smartbudget.service.postres.DateProvider;
import smartbudget.service.postres.expenses.ExpensesDataService;
import smartbudget.service.postres.expenses.ExpensesRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/budget/v2/")
public class ExpensesOperationV2Controller {

    private ExpensesDataService expensesDataService;
    private ExpensesRepository expensesRepository;
    private DateProvider dateProvider;

    @Autowired
    public ExpensesOperationV2Controller(
        ExpensesDataService expensesDataService,
        ExpensesRepository expensesRepository,
        DateProvider dateProvider
    ) {
        this.expensesDataService = expensesDataService;
        this.expensesRepository = expensesRepository;
        this.dateProvider = dateProvider;
    }

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

}
