package smartbudget.client.service.postgres.expenses;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static smartbudget.util.SystemParams.EXPENSES_OPENING_BALANCE;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import smartbudget.model.expenses.CurrentStatistic;
import smartbudget.model.expenses.ExpensesData;
import smartbudget.model.expenses.ExpensesType;
import smartbudget.model.expenses.YearlyReportData;
import smartbudget.model.expenses.YearlyReport;
import smartbudget.service.CommonRepository;
import smartbudget.service.postres.DateProvider;
import smartbudget.service.postres.expenses.ExpensesDataServiceImpl;
import smartbudget.service.postres.expenses.ExpensesRepository;
import smartbudget.util.SystemParams;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExpensesDataServiceTest {

    private static final int USER_ID = 1;
    private static final int MONTH = 4;
    private static final int YEAR = 2019;
    private static final LocalDate LOCAL_DATE_NOW = LocalDate.of(YEAR, MONTH, 18);

    @Mock
    private ExpensesRepository expensesRepository;
    @Mock
    private CommonRepository commonRepository;
    @Mock
    private DateProvider dateProvider;

    @InjectMocks
    private ExpensesDataServiceImpl expensesDataService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(commonRepository.getParamValue(USER_ID, SystemParams.FUND_OPENING_ID)).thenReturn(BigDecimal.valueOf(5));
        when(commonRepository.getParamValue(USER_ID, SystemParams.DOLLAR_OPENING_BALANCE)).thenReturn(BigDecimal.valueOf(10000));
        when(commonRepository.getParamValue(USER_ID, SystemParams.EURO_OPENING_BALANCE)).thenReturn(BigDecimal.valueOf(5000));
        when(commonRepository.getParamValue(USER_ID, SystemParams.RUB_OPENING_BALANCE)).thenReturn(BigDecimal.valueOf(300000));
    }

    @AfterMethod
    public void reset() {
        expensesDataService = null;
        Mockito.reset(expensesRepository);
    }

    @Test(dataProvider = "successProvider")
    public void testGetStatistic(
            LocalDate dateNow,
            BigDecimal openBalance,
            BigDecimal openExpensesId,
            List<ExpensesData> expensesSinceId
    ) {
        when(dateProvider.now()).thenReturn(dateNow);
        List<ExpensesData> expenses = ImmutableList.of(
                createExpenses(11, 2, "Spar", 1800, LOCAL_DATE_NOW.minusDays(3), MONTH, EXPENSES_TYPES),
                createExpenses(12, 3, "Tests", 400, LOCAL_DATE_NOW.minusDays(3), MONTH, EXPENSES_TYPES),
                createExpenses(13, 1, "-", 500, LOCAL_DATE_NOW.minusDays(3), MONTH, EXPENSES_TYPES),
                createExpenses(14, 4, "Salary", 80000, LOCAL_DATE_NOW.minusDays(2), MONTH, EXPENSES_TYPES)
        );
        when(expensesRepository.getExpensesByYearMonth(USER_ID, YEAR, MONTH)).thenReturn(expenses);
        when(commonRepository.getParamValue(USER_ID, EXPENSES_OPENING_BALANCE)).thenReturn(openBalance);
        when(commonRepository.getParamValue(USER_ID, SystemParams.EXPENSES_OPENING_ID)).thenReturn(openExpensesId);
        when(commonRepository.getUserParamUpdateDateString(USER_ID, SystemParams.EXPENSES_OPENING_BALANCE))
                .thenReturn(LOCAL_DATE_NOW.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        when(expensesRepository.getExpensesSinceId(USER_ID, openExpensesId.intValue()))
                .thenReturn(ImmutableList.<ExpensesData>builder()
                        .addAll(expenses)
                        .addAll(expensesSinceId)
                        .build()
                );

        when(commonRepository.getParamValue(USER_ID, SystemParams.FUND_OPENING_ID)).thenReturn(BigDecimal.valueOf(5));
        when(commonRepository.getParamValue(USER_ID, SystemParams.DOLLAR_OPENING_BALANCE)).thenReturn(BigDecimal.valueOf(10000));
        when(commonRepository.getParamValue(USER_ID, SystemParams.EURO_OPENING_BALANCE)).thenReturn(BigDecimal.valueOf(5000));
        when(commonRepository.getParamValue(USER_ID, SystemParams.RUB_OPENING_BALANCE)).thenReturn(BigDecimal.valueOf(300000));

        when(expensesRepository.getFundState(1, 5)).thenReturn(ImmutableMap.of(
                1, BigDecimal.valueOf(12000),
                2, BigDecimal.valueOf(7000),
                3, BigDecimal.valueOf(400000)
        ));

        when(expensesRepository.getCurrencyPrice()).thenReturn(
                ImmutableMap.of(
                        1, BigDecimal.valueOf(64.02),
                        2, BigDecimal.valueOf(75.03),
                        3, BigDecimal.valueOf(1)
                )
        );

        CurrentStatistic statistic = expensesDataService.getCurrentStatistic(USER_ID);

        Assert.assertEquals(statistic.getExpensesTotalAmount(), BigDecimal.valueOf(270000, 2));
        Assert.assertEquals(statistic.getOpenBalanceAmount(), openBalance);
        Assert.assertEquals(statistic.getRestAmount(), BigDecimal.valueOf(10988700, 2));

        Assert.assertEquals(statistic.getFundDollarAmount(), BigDecimal.valueOf(22000));
        Assert.assertEquals(statistic.getFundEuroAmount(), BigDecimal.valueOf(12000));
        Assert.assertEquals(statistic.getFundRubAmount(), BigDecimal.valueOf(700000));

        Assert.assertEquals(statistic.getDollarPrice(), BigDecimal.valueOf(64.02));
        Assert.assertEquals(statistic.getEuroPrice(), BigDecimal.valueOf(75.03));

        Assert.assertEquals(statistic.getFundDollarAmountInRub(), BigDecimal.valueOf(140844000, 2));
        Assert.assertEquals(statistic.getFundEuroAmountInRub(), BigDecimal.valueOf(90036000, 2));

        Assert.assertEquals(statistic.getAllAmountInRub(), BigDecimal.valueOf(311868700, 2));

        Assert.assertEquals(statistic.getOpenBalanceDate(), LOCAL_DATE_NOW.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        System.out.println(statistic);
    }

    @DataProvider
    private static Object[][] successProvider() {
        return new Object[][]{
                {
                        LOCAL_DATE_NOW,
                        BigDecimal.valueOf(34000),
                        BigDecimal.valueOf(11),
                        ImmutableList.of(
                                createExpenses(1, 2, "Spar 01", 1005, LOCAL_DATE_NOW.minusDays(3), MONTH, EXPENSES_TYPES),
                                createExpenses(2, 3, "Tests 01", 408, LOCAL_DATE_NOW.minusDays(3), MONTH, EXPENSES_TYPES)
                        )
                }
        };
    }

    @Test
    public void testYearStatistic() {
        List<ExpensesType> types = ImmutableList.of(
                new ExpensesType(1, 1, 1, "Products", false),
                new ExpensesType(2, 1, 2, "Lunch", false),
                new ExpensesType(3, 1, 3, "Income", true),
                new ExpensesType(4, 1, 4, "Health", false),
                new ExpensesType(5, 1, 5, "other", false)
        );
        when(expensesRepository.getExpensesTypes(USER_ID)).thenReturn(types);

        when(expensesRepository.getExpensesByYear(USER_ID, 2019)).thenReturn(
                ImmutableList.of(
                        createExpenses(1, 1, "1", 1000, LOCAL_DATE_NOW, 1, types),
                        createExpenses(2, 2, "2", 2000, LOCAL_DATE_NOW, 2, types),
                        createExpenses(3, 3, "3", 3000, LOCAL_DATE_NOW, 3, types),
                        createExpenses(4, 4, "4", 4000, LOCAL_DATE_NOW, 4, types),
                        createExpenses(5, 2, "5", 5000, LOCAL_DATE_NOW, 4, types)
                )
        );

        List<YearlyReportData> expectedResult = ImmutableList.of(
                new YearlyReportData(1, ImmutableList.of("1000.0", "0.0", "0.0", "0.0", "0.0", "1000.0")),
                new YearlyReportData(2, ImmutableList.of("0.0", "2000.0", "0.0", "0.0", "0.0", "2000.0")),
                new YearlyReportData(3, ImmutableList.of("0.0", "0.0", "3000.0", "0.0", "0.0", "0.0")),
                new YearlyReportData(4, ImmutableList.of("0.0", "5000.0", "0.0", "4000.0", "0.0", "9000.0"))
        );

        List<String> expectedTotals = ImmutableList.of("1000.0", "7000.0", "3000.0", "4000.0", "0.0", "12000.0");

        YearlyReport reportsByYear = expensesDataService.getReportsByYear(USER_ID, 2019);
        List<YearlyReportData> result = reportsByYear.getReportDataList();
        List<String> totals = reportsByYear.getTotals();

        Assert.assertEquals(result, expectedResult);
        Assert.assertEquals(totals, expectedTotals);
    }

    private static final List<ExpensesType> EXPENSES_TYPES = ImmutableList.<ExpensesType>builder()
            .add(new ExpensesType(1, USER_ID, 1, "other", false))
            .add(new ExpensesType(2, USER_ID, 2, "Products", false))
            .add(new ExpensesType(3, USER_ID, 3, "Car", false))
            .add(new ExpensesType(4, USER_ID, 4, "Income", true))
            .build();

    private static ExpensesData createExpenses(
            long id, int typeId, String description, int amount, LocalDate date, int month,
            List<ExpensesType> expensesTypes
    ) {
        ExpensesType type = expensesTypes.stream().filter(t -> t.getExpensesTypeId() == typeId).findFirst().get();
        return new ExpensesData(id, USER_ID, month, YEAR, type, description, BigDecimal.valueOf(amount), date);
    }

}
