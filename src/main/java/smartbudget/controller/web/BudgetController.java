package smartbudget.controller.web;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BudgetController {

    private ExpensesRepository expensesRepository;
    private DateProvider dateProvider;
    private ExpensesDataService expensesDataService;
    private CommonRepository commonRepository;

    @Autowired
    public BudgetController(
            ExpensesRepository expensesRepository,
            DateProvider dateProvider,
            ExpensesDataService expensesDataService,
            CommonRepository commonRepository) {
        this.expensesRepository = expensesRepository;
        this.dateProvider = dateProvider;
        this.expensesDataService = expensesDataService;
        this.commonRepository = commonRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/expenses", method = RequestMethod.GET)
    public String expenses(Model model) {

        LocalDate now = dateProvider.now();

        List<ExpensesData> expensesList = null;
        expensesList = expensesRepository.getExpensesByYearMonth(DEFAULT_USER, now.getYear(), now.getMonthValue());
        CurrentStatistic currentStatistic = expensesDataService.getCurrentStatistic(DEFAULT_USER);

        Map<String, BigDecimal> totalList = new HashMap<>();
        expensesList.forEach(t -> {
            String key = t.getExpensesType().getDescription();
            BigDecimal sum = t.getAmount(); // Избегаем NPE
            totalList.merge(key, sum, BigDecimal::add);
        });

        model.addAttribute("results", expensesList);
        model.addAttribute("totals", totalList.entrySet());
        model.addAttribute("statistic", currentStatistic);
        return "expenses/expenses";
    }

    @RequestMapping(value = "/expenses/form", method = RequestMethod.GET)
    public String expensesForm(Model model) {

        Map<String, String> months = new ImmutableMap.Builder<String, String>()
                .put("1", "Январь")
                .put("2", "Февраль")
                .put("3", "Март")
                .put("4", "Апрель")
                .put("5", "Май")
                .put("6", "Июнь")
                .put("7", "Июль")
                .put("8", "Август")
                .put("9", "Сентябрь")
                .put("10", "Октябрь")
                .put("11", "Ноябрь")
                .put("12", "Декабрь")
                .build();

        List<ExpensesType> expensesTypes = expensesRepository.getExpensesTypes(DEFAULT_USER);
        model.addAttribute("types", expensesTypes);
        model.addAttribute("months", months.entrySet());
        model.addAttribute("current_month", "" + LocalDate.now().getMonthValue());
        model.addAttribute("year", "" + LocalDate.now().getYear());
        return "expenses/expenses_add";
    }

    @RequestMapping(value = "/expenses/response/add", method = RequestMethod.GET)
    public String expensesResponse(Model model,
                                   @RequestParam(value="month") Integer month,
                                   @RequestParam(value="year") Integer year,
                                   @RequestParam(value="type") Integer type,
                                   @RequestParam(value="desc") String description,
                                   @RequestParam(value = "amount") String amount
    ) {

        ExpensesType expensesType = expensesRepository.getExpensesTypes(DEFAULT_USER).stream()
                .filter(t -> t.getExpensesTypeId()== type).findFirst().get();

        BigDecimal amountValue = BigDecimal.valueOf(parseAmountString(amount)).setScale(2);
        ExpensesData expensesData = new ExpensesData(
                DEFAULT_USER,
                month,
                year,
                expensesType,
                description,
                amountValue,
                dateProvider.now()
        );
        expensesRepository.saveExpenses(expensesData);

        model.addAttribute("expenses", expensesData);
        return "expenses/expenses_add_response";
    }

    @RequestMapping(value = "/expenses/balance/update", method = RequestMethod.GET)
    public String updateExpensesBalance()  {
        return "expenses/expenses_update_balance";
    }

    @RequestMapping(value = "/expenses/balance/response", method = RequestMethod.GET)
    public String updateExpensesBalanceResponse(Model model,
                                                @RequestParam(value = "amount") String amount
    ) {


        BigDecimal amountValue = BigDecimal.valueOf(parseAmountString(amount));
        long expensesId = expensesRepository.getLastExpensesId(DEFAULT_USER);
        commonRepository.createOrReplaceUserParams(
                DEFAULT_USER, SystemParams.EXPENSES_OPENING_BALANCE, amountValue.setScale(2)
        );
        commonRepository.createOrReplaceUserParams(
                DEFAULT_USER, SystemParams.EXPENSES_OPENING_ID, BigDecimal.valueOf(expensesId).setScale(2)
        );
        model.addAttribute("updated_amount", amountValue);
        return "expenses/expenses_update_balance_response";
    }

    public static double parseAmountString(String s) {

        double amount = 0;
        double amtPlus;

        String firstSimbol = s.substring(0,1);

        if(firstSimbol.equals("=")){

            String sNumber = "";

            char [] myChar = s.toCharArray ();
            for (int i=0 ; i<myChar.length;i++ ){


                if(Character.isDigit(myChar[i])==true && i!=myChar.length){

                    String chStr = Character.toString(myChar[i]);

                    sNumber = sNumber.concat(chStr);

                }
                if(Character.toString(myChar[i]).equals("-")){

                    amtPlus = Double.parseDouble(sNumber);
                    amount = amount+amtPlus;

                    sNumber = "-";

                }
                if(Character.toString(myChar[i]).equals("+")){

                    amtPlus = Double.parseDouble(sNumber);
                    amount = amount+amtPlus;
                    sNumber = "";

                }
            }

            amtPlus = Double.parseDouble(sNumber);
            amount = amount+amtPlus;

        } else{
            amount = Double.parseDouble(s);
        }
        return amount;
    }

    private static final int DEFAULT_USER = 1;

}
