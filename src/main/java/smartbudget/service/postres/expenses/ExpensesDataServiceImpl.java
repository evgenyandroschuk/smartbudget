package smartbudget.service.postres.expenses;

import com.google.common.collect.ImmutableMap;
import smartbudget.model.expenses.CurrentStatistic;
import smartbudget.model.expenses.ExpensesData;
import smartbudget.model.expenses.ExpensesType;
import smartbudget.model.expenses.YearlyReportData;
import smartbudget.service.CommonRepository;
import smartbudget.service.postres.DateProvider;
import smartbudget.util.SystemParams;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static smartbudget.util.SystemParams.EXPENSES_OPENING_BALANCE;

public class ExpensesDataServiceImpl implements ExpensesDataService {

    private final ExpensesRepository expensesRepository;
    private final CommonRepository commonRepository;
    private final DateProvider dateProvider;

    public ExpensesDataServiceImpl(
        ExpensesRepository expensesRepository,
        CommonRepository commonRepository,
        DateProvider dateProvider
    ) {
        this.expensesRepository = expensesRepository;
        this.commonRepository = commonRepository;
        this.dateProvider = dateProvider;
    }

    @Override
    public CurrentStatistic getCurrentStatistic(int userId) {
        LocalDate now = dateProvider.now();

        int year = now.getYear();
        int month = now.getMonthValue();
        // expensesTotalAmount = все расходы за выбранный месяц, доходы не включены
        BigDecimal expensesTotalAmount = expensesRepository.getExpensesByYearMonth(userId, year, month).stream()
            .filter(t -> !t.getExpensesType().isIncome())
            .map(ExpensesData::getAmount)
            .reduce(BigDecimal.valueOf(0, 2), BigDecimal::add);

        // Фиксированная сумма доступных расходов, для расчета остатков
        BigDecimal openBalanceAmount = commonRepository.getParamValue(userId, EXPENSES_OPENING_BALANCE);

        // id в expensesData, от которой считают остаток.
        int startExpensesId = commonRepository.getParamValue(userId, SystemParams.EXPENSES_OPENING_ID).intValue();


        // Сумма расходов
        BigDecimal expensesAmount = expensesRepository.getExpensesSinceId(userId, startExpensesId).stream()
            .filter(t -> !t.getExpensesType().isIncome())
            .map(ExpensesData::getAmount)
            .reduce(BigDecimal.valueOf(0, 2), BigDecimal::add);

        // Сумма доходовы
        BigDecimal incomeAmount = expensesRepository.getExpensesSinceId(userId, startExpensesId).stream()
            .filter(t -> t.getExpensesType().isIncome())
            .map(ExpensesData::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // От значения начального баланса вычитаем расходы и добавляем доходы
        BigDecimal restAmount = openBalanceAmount.subtract(expensesAmount).add(incomeAmount);

        Map<String, BigDecimal> fundAmounts = calculateFunds(userId);
        BigDecimal restDollarAmount = fundAmounts.get(DOLLAR_AMOUNT_IN_RUB).setScale(2);
        BigDecimal restEuroAmount = fundAmounts.get(EURO_AMOUNT_IN_RUB).setScale(2);
        BigDecimal restRubAmount  = fundAmounts.get(RUB_AMOUNT);
        BigDecimal restAllAmount = restAmount.add(restDollarAmount).add(restEuroAmount).add(restRubAmount).setScale(2);

        return CurrentStatistic.CurrentStatisticBuilder.builder()
            .setExpensesTotalAmount(expensesTotalAmount)
            .setOpenBalanceAmount(openBalanceAmount)
            .setRestAmount(restAmount)

            .setFundDollarAmount(fundAmounts.get(DOLLAR_AMOUNT))
            .setFundEuroAmount(fundAmounts.get(EURO_AMOUNT))
            .setFundRubAmount(fundAmounts.get(RUB_AMOUNT))

            .setDollarPrice(fundAmounts.get(DOLLAR_PRICE))
            .setEuroPrice(fundAmounts.get(EURO_PRICE))
            .setFundDollarAmountInRub(restDollarAmount)
            .setFundEuroAmountInRub(restEuroAmount)
            .setAllAmountInRub(restAllAmount)
            .setOpenBalanceDate(commonRepository.getUserParamUpdateDateString(userId, EXPENSES_OPENING_BALANCE))
            .build();
    }

    @Override
    public List<YearlyReportData> getReportsByYear(int userId, int year) {
        List<ExpensesData> expensesByYear = expensesRepository.getExpensesByYear(userId, year);
        List<ExpensesType> types = expensesRepository.getExpensesTypes(userId);
        List<YearlyReportData> result = new LinkedList<>();
        Set<Map.Entry<Integer, Map<ExpensesType, Double>>> entries = expensesByYear.stream().collect(
                Collectors.groupingBy(ExpensesData::getMonth,
                        Collectors.groupingBy(
                                t -> t.getExpensesType(),
                                Collectors.summingDouble(t -> Double.valueOf(t.getAmount().toString()))
                        )
                )
        ).entrySet();

        entries.forEach(entry -> {
            List<String> amounts = types.stream().map(
                    t -> Optional.ofNullable(
                            entry.getValue().get(t))
                            .orElse(0.0).toString()
            ).collect(Collectors.toList());
            String expensesAmount = entry.getValue().entrySet().stream().filter(e -> e.getKey().isIncome() == false)
                    .map(t -> t.getValue()).reduce(0.0, (k, v) -> k+ v).toString();
            amounts.add(expensesAmount);
            result.add(new YearlyReportData(entry.getKey(), amounts));
        });

        return result;
    }

    private Map<String, BigDecimal> calculateFunds(int userId) {
        // Фиксированная сумма в копилке по валютам, для расчета остатка на текующую дату.
        BigDecimal fundOpenId = commonRepository.getParamValue(userId, SystemParams.FUND_OPENING_ID);

        BigDecimal startDollarAmount = commonRepository.getParamValue(userId, SystemParams.DOLLAR_OPENING_BALANCE);
        BigDecimal startEuroAmount = commonRepository.getParamValue(userId, SystemParams.EURO_OPENING_BALANCE);
        BigDecimal startRubAmount = commonRepository.getParamValue(userId, SystemParams.RUB_OPENING_BALANCE);

        Map<Integer, BigDecimal> fundsMap = expensesRepository.getFundState(1, fundOpenId.intValue());

        BigDecimal restDollarAmount = startDollarAmount.add(fundsMap.get(1));
        BigDecimal restEuroAmount = startEuroAmount.add(fundsMap.get(2));
        BigDecimal restRubAmount = startRubAmount.add(fundsMap.get(3));

        Map<Integer, BigDecimal> currencyPrice = expensesRepository.getCurrencyPrice();

        return ImmutableMap.<String, BigDecimal>builder()
            .put(DOLLAR_AMOUNT, restDollarAmount)
            .put(EURO_AMOUNT, restEuroAmount)
            .put(RUB_AMOUNT, restRubAmount)
            .put(DOLLAR_AMOUNT_IN_RUB, restDollarAmount.multiply(currencyPrice.get(1)))
            .put(EURO_AMOUNT_IN_RUB, restEuroAmount.multiply(currencyPrice.get(2)))
            .put(DOLLAR_PRICE, currencyPrice.get(1))
            .put(EURO_PRICE, currencyPrice.get(2))
            .build();
    }

    private static final String DOLLAR_PRICE = "dollarPrice";
    private static final String EURO_PRICE = "euroPrice";

    private static final String DOLLAR_AMOUNT = "dollarAmount";
    private static final String EURO_AMOUNT = "euroAmount";
    private static final String RUB_AMOUNT = "rubAmount";
    private static final String DOLLAR_AMOUNT_IN_RUB = "dollarAmountRub";
    private static final String EURO_AMOUNT_IN_RUB = "euroAmountRub";

}
