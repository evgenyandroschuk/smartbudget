package smartbudget.client.impl;

import com.google.common.collect.ImmutableMap;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.*;
import smartbudget.model.ExpensesData;
import smartbudget.service.impl.mysql.ExpensesMySQLImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

public class ExpensesMySqlImplTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @Mock
    private ResultSetMetaData resultSetMetaData;

    @InjectMocks
    private ExpensesMySQLImpl expensesMySQLImpl;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void resetMocks() {
        reset(
                connection,
                preparedStatement,
                resultSet,
                resultSetMetaData
        );
    }

    @Test(dataProvider = "statisticByYearProvider")
    public void statisticByYearTest(
            String query,
            int year,
            Map<String, String> resultMap
    ) throws SQLException {
        when(resultSet.first()).thenReturn(true);
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        when(resultSetMetaData.getColumnCount()).thenReturn(1);
        when(resultSetMetaData.getColumnName(1)).thenReturn(COLUMN_YEAR);
        when(resultSet.getString(1)).thenReturn(Integer.toString(year));
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        Map<String, String> result = expensesMySQLImpl.getStatisticByYear(year);

        assertEquals(result.get(COLUMN_YEAR), resultMap.get(COLUMN_YEAR));
        verify(preparedStatement).setInt(1, year);
    }

    @DataProvider
    private static Object[][] statisticByYearProvider() {
        return new Object[][] {
                {
                        STATISTIC_BY_YEAR_QUERY,
                        2018,
                        ImmutableMap.of("year", "2018")
                },
                {
                        STATISTIC_BY_YEAR_QUERY,
                        2019,
                        ImmutableMap.of("year", "2019")
                }
        };
    }

    @Test(dataProvider = "statisticByYearMonthProvider")
    public void statisticByYearMonthTest(
            String query,
            int year,
            int month,
            Map<String, String> resultMap
    ) throws SQLException {
        when(resultSet.first()).thenReturn(true);
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        when(resultSetMetaData.getColumnCount()).thenReturn(2);
        when(resultSetMetaData.getColumnName(1)).thenReturn(COLUMN_YEAR);
        when(resultSetMetaData.getColumnName(2)).thenReturn(COLUMN_MONTH);
        when(resultSet.getString(1)).thenReturn(Integer.toString(year));
        when(resultSet.getString(2)).thenReturn(Integer.toString(month));
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        Map<String, String> result = expensesMySQLImpl.getStatisticByYearMonth(year, month);

        assertEquals(result.get(COLUMN_YEAR), resultMap.get(COLUMN_YEAR));
        assertEquals(result.get(COLUMN_MONTH), resultMap.get(COLUMN_MONTH));
        verify(preparedStatement).setInt(1, year);
        verify(preparedStatement).setInt(2, month);
    }

    @DataProvider
    private static Object[][] statisticByYearMonthProvider() {
        return new Object[][] {
                {
                        STATISTIC_BY_YEAR_MONTH_QUERY,
                        2018,
                        1,
                        ImmutableMap.of("year", "2018", "month","1")
                },
                {
                        STATISTIC_BY_YEAR_MONTH_QUERY,
                        2018,
                        2,
                        ImmutableMap.of("year", "2018", "month","2")
                },
                {
                        STATISTIC_BY_YEAR_MONTH_QUERY,
                        2019,
                        1,
                        ImmutableMap.of("year", "2019", "month","1")
                },
                {
                        STATISTIC_BY_YEAR_MONTH_QUERY,
                        2019,
                        2,
                        ImmutableMap.of("year", "2019", "month","2")
                }
        };
    }

    @Test(dataProvider = "lastPeriodExpensesProvider")
    public void lastPeriodExpensesTest(int startId, double expectedResult) throws SQLException {
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getDouble("expenses_amount")).thenReturn(expectedResult);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(connection.prepareStatement(LAST_PERIOD_EXPENSES_QUERY)).thenReturn(preparedStatement);

        double result = expensesMySQLImpl.getLastPeriodExpenses(startId);
        assertTrue(result == expectedResult);
        verify(preparedStatement).setInt(1, startId);
    }

    @DataProvider
    private static Object[][] lastPeriodExpensesProvider() {
        return new Object[][] {
                {15, 310.0},
                {100, 515.0}
        };
    }




    @Test(dataProvider = "getIncomeAmountProvider")
    public void incomeAmountTest(int startId, double expectedResult) throws SQLException {
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getDouble("income_amount")).thenReturn(expectedResult);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(connection.prepareStatement(INCOME_AMOUNT_QUERY)).thenReturn(preparedStatement);

        double result = expensesMySQLImpl.getIncomeAmount(startId);
        assertTrue(result == expectedResult);
        verify(preparedStatement).setInt(1, startId);
    }

    @DataProvider
    private static Object[][] getIncomeAmountProvider() {
        return new Object[][] {
                {2001, 107000.0},
                {2577, 130500.0}
        };
    }

    @Test(dataProvider = "currenciesDataProvider")
    public void testCurrencies(
            Map<String, Double> expectedCurrenciesMap
    ) throws SQLException {
        when(resultSet.next()).thenReturn(true, true, true, false);
        when(resultSet.getString(DESCRIPTION_FIELD)).thenReturn(DOLLAR, EURO, RUB );
        when(resultSet.getDouble("price"))
                .thenReturn(
                        expectedCurrenciesMap.get(DOLLAR),
                        expectedCurrenciesMap.get(EURO),
                        expectedCurrenciesMap.get(RUB)
                );
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(connection.prepareStatement(CURRENCY_QUERY)).thenReturn(preparedStatement);

        Map<String, Double> currenciesMap = expensesMySQLImpl.getCurrencies();

        assertTrue(currenciesMap.get(DOLLAR).equals(expectedCurrenciesMap.get(DOLLAR)));
        assertTrue(currenciesMap.get(EURO).equals(expectedCurrenciesMap.get(EURO)));
        verify(connection).prepareStatement(CURRENCY_QUERY);
    }

    @DataProvider
    private static Object[][] currenciesDataProvider() {
        return new Object[][] {
                {
                        ImmutableMap.of(
                                DOLLAR, 63.3,
                                EURO, 71.15,
                                RUB, 0.0
                        )
                },

                {
                        ImmutableMap.of(
                                DOLLAR, 63.3,
                                EURO, 71.15,
                                RUB, 0.0
                        )
                }
        };
    }

    @Test(dataProvider = "fundAmountProvider")
    public void testFundAmount(
            int fundId,
            int currencyId,
            double expectedAmount,
            boolean hasNext
    ) throws SQLException {
        when(resultSet.next()).thenReturn(hasNext);
        when(resultSet.getDouble(AMOUNT_FIELD)).thenReturn(FUND_QUERY_RESULT);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(connection.prepareStatement(FUND_AMOUNT_QUERY)).thenReturn(preparedStatement);

        double result = expensesMySQLImpl.getFundAmount(fundId, currencyId);

        assertEquals(result, expectedAmount);
        verify(connection).prepareStatement(FUND_AMOUNT_QUERY);
        verify(preparedStatement).setInt(1, fundId);
        verify(preparedStatement).setInt(2, currencyId);


    }

    @DataProvider
    private static Object[][] fundAmountProvider() {
        return new Object[][] {
                {
                    46, 1, FUND_QUERY_RESULT, true
                },
                {
                    75, 2, FUND_QUERY_RESULT, true
                },
                {
                    44, 3, FUND_QUERY_RESULT, true
                },
                {
                    46, 1, 0, false
                }
        };
    }

    @Test(dataProvider = "yearlyExpensesProvider")
    public void yearlyExpensesTest(
            int year,
            Map<String, String> mapFirst,
            Map<String, String> mapSecond
    ) throws SQLException {
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString(ID_FIELD)).thenReturn(mapFirst.get(ID_FIELD),mapSecond.get(ID_FIELD));
        when(resultSet.getString(MONTH_ID_FIELD)).thenReturn(mapFirst.get(MONTH_ID_FIELD),mapSecond.get(MONTH_ID_FIELD));
        when(resultSet.getString(YEAR_ID_FIELD)).thenReturn(mapFirst.get(YEAR_ID_FIELD), mapSecond.get(YEAR_ID_FIELD));
        when(resultSet.getString(DESCRIPTION_FIELD)).thenReturn(
                mapFirst.get(DESCRIPTION_FIELD), mapSecond.get(DESCRIPTION_FIELD)
        );
        when(resultSet.getString(AMOUNT_FIELD)).thenReturn(mapFirst.get(AMOUNT_FIELD), mapSecond.get(AMOUNT_FIELD));
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(connection.prepareStatement(YEARLY_EXPENSES_QUERY)).thenReturn(preparedStatement);

        List<Map<String, String>> resultList = expensesMySQLImpl.getYearlyExpenses(year);
        assertEquals(resultList.get(0).get(AMOUNT_FIELD), mapFirst.get(AMOUNT_FIELD));
        assertEquals(resultList.get(1).get(AMOUNT_FIELD), mapSecond.get(AMOUNT_FIELD));

        verify(preparedStatement).setInt(1, year);
        verify(connection).prepareStatement(YEARLY_EXPENSES_QUERY);
    }


    @DataProvider
    private static Object[][] yearlyExpensesProvider() {
        return new Object[][] {
                {
                    2018,
                    ImmutableMap.of(
                            ID_FIELD, "2000",
                            MONTH_ID_FIELD, "1",
                            YEAR_ID_FIELD, "2018",
                            DESCRIPTION_FIELD, "test description",
                            AMOUNT_FIELD, "500"
                    ),
                        ImmutableMap.of(
                                ID_FIELD, "2001",
                                MONTH_ID_FIELD, "1",
                                YEAR_ID_FIELD, "2018",
                                DESCRIPTION_FIELD, "test description 2",
                                AMOUNT_FIELD, "505"
                        )
                }
        };
    }

    @Test(dataProvider = "fundProvider")
    public void testFund(Map<String, String> rowResult) throws SQLException {
        when(resultSet.getString(ID_FIELD)).thenReturn(rowResult.get(ID_FIELD));
        when(resultSet.getString(UPDATE_DATE_FIELD)).thenReturn(rowResult.get(UPDATE_DATE_FIELD));
        when(resultSet.getString(CURRENCY_ID_FIELD)).thenReturn(rowResult.get(CURRENCY_ID_FIELD));
        when(resultSet.getString(AMOUNT_FIELD)).thenReturn(rowResult.get(AMOUNT_FIELD));
        when(resultSet.getString(DESCRIPTION_FIELD)).thenReturn(rowResult.get(DESCRIPTION_FIELD));
        when(resultSet.getString(SALE_PRICE_FIELD)).thenReturn(rowResult.get(SALE_PRICE_FIELD));
        when(resultSet.next()).thenReturn(true, false);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(connection.prepareStatement(FUND_QUERY)).thenReturn(preparedStatement);

        List<Map<String, String>> resultList = expensesMySQLImpl.getFunds();

        assertEquals(rowResult.get(SALE_PRICE_FIELD), resultList.get(0).get(SALE_PRICE_FIELD));
        verify(connection).prepareStatement(FUND_QUERY);
    }


    @DataProvider
    private static Object[][] fundProvider() {
        return new Object[][] {
                {
                        ImmutableMap.<String, String>builder()
                                .put(ID_FIELD, "10")
                                .put(UPDATE_DATE_FIELD, "2017-11-12")
                                .put(CURRENCY_ID_FIELD, "1")
                                .put(AMOUNT_FIELD, "1000")
                                .put(DESCRIPTION_FIELD, "Описание")
                                .put(SALE_PRICE_FIELD, "65.01")
                                .build()
                }
        };
    }

    @Test(dataProvider = "insertExpensesProvider")
    public void testInsertExpenses(
            String description,
            int monthId,
            int yearId,
            int typeId,
            String updateDate,
            double amount
    ) throws SQLException {
        when(preparedStatement.execute()).thenReturn(true);
        when(connection.prepareStatement(INSERT_EXPENSES_QUERY)).thenReturn(preparedStatement);
        LocalDate updateLocalDate = LocalDate.parse(updateDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        ExpensesData expensesData = ExpensesData.of(monthId, yearId, typeId, description, amount, updateLocalDate);
        expensesMySQLImpl.create(expensesData);

        verify(connection).prepareStatement(INSERT_EXPENSES_QUERY);
        verify(preparedStatement).setString(1, description);
        verify(preparedStatement).setInt(2, monthId);
        verify(preparedStatement).setInt(3, yearId);
        verify(preparedStatement).setInt(4, typeId);
        verify(preparedStatement).setString(5, updateDate);
        verify(preparedStatement).setDouble(6, amount);
    }

    @DataProvider
    private static Object[][] insertExpensesProvider() {
        return new Object[][] {
                {
                    "Test description",
                        2,
                        2019,
                        1,
                        "2019-02-03",
                        100
                }
        };
    }

    @Test(dataProvider = "updateExpensesProvider")
    public void testUpdateExpenses(
            String description,
            int monthId,
            int yearId,
            int typeId,
            String updateDate,
            double amount,
            long id
    ) throws SQLException {
        when(preparedStatement.execute()).thenReturn(true);
        when(connection.prepareStatement(UPDATE_EXPENSES_QUERY)).thenReturn(preparedStatement);

        LocalDate updateLocalDate = LocalDate.parse(updateDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ExpensesData expensesData = ExpensesData.of(monthId, yearId, typeId, description, amount, updateLocalDate);

        expensesMySQLImpl.update(id, expensesData);

        verify(preparedStatement).setString(1, description);
        verify(preparedStatement).setInt(2, monthId);
        verify(preparedStatement).setInt(3, yearId);
        verify(preparedStatement).setInt(4, typeId);
        verify(preparedStatement).setString(5, updateDate);
        verify(preparedStatement).setDouble(6, amount);
        verify(preparedStatement).setLong(7, id);
    }

    @DataProvider
    private static Object[][] updateExpensesProvider() {
        return new Object[][] {
                {
                        "Test description",
                        2,
                        2019,
                        1,
                        "2019-02-03",
                        100,
                        3263
                }
        };
    }


    @Test(dataProvider = "deleteExpensesProvider")
    public void testDeleteExpenses(long id) throws SQLException {
        when(preparedStatement.execute()).thenReturn(true);
        when(connection.prepareStatement(DELETE_EXPENSES_QUERY)).thenReturn(preparedStatement);

        expensesMySQLImpl.delete(id);

        verify(connection).prepareStatement(DELETE_EXPENSES_QUERY);
        verify(preparedStatement).setLong(1, id);
    }

    @DataProvider
    private static Object[][] deleteExpensesProvider() {
        return new Object[][] {
                {774},
                {775}
        };
    }


    private static final String STATISTIC_BY_YEAR_QUERY =
            "select sum(amount) as amount from expenses, t_operation_type \n" +
            "where expenses.operation_type_id = t_operation_type.id\n" +
            "and is_income = 0 and year_id = ?";

    private static final String STATISTIC_BY_YEAR_MONTH_QUERY =
            "select sum(amount) as amount from expenses, t_operation_type \n" +
                    "where expenses.operation_type_id = t_operation_type.id\n" +
                    "and is_income = 0 and year_id = ? and month_id = ?";

    private static final String LAST_PERIOD_EXPENSES_QUERY =
            "select sum(amount) expenses_amount from expenses e\n" +
                    "join t_operation_type t on t.id = e.operation_type_id \n" +
                    "where e.id > ? and t.is_income = 0";

    private static final String INCOME_AMOUNT_QUERY =
            "select sum(amount) income_amount from expenses e " +
                    "join t_operation_type t on t.id = e.operation_type_id\n" +
                    " where e.id > ?  and t.is_income = 1";

    private static final String YEARLY_EXPENSES_QUERY =
            "select expenses.id, month_id, year_id, is_income, t_operation_type.description, amount " +
            "from expenses, t_operation_type \n" +
            "where expenses.operation_type_id = t_operation_type.id\n" +
            "and year_id = ?";

    private static final String CURRENCY_QUERY = "select * from currency";

    private static final String FUND_AMOUNT_QUERY = "select sum(amount) amount from fund where id > ? and currency_id = ?";

    private static final String FUND_QUERY = "select id, update_date, currency_id, amount, description, sale_price from fund order by id";

    private static final String INSERT_EXPENSES_QUERY =
            "insert into expenses (id, description, month_id, year_id, operation_type_id, update_date, amount)\n" +
            "values(get_id(1), ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_EXPENSES_QUERY =
            "update expenses \n" +
                    "set description = ?,\n" +
                    "month_id = ?,\n" +
                    "year_id = ?,\n" +
                    "operation_type_id = ?,\n" +
                    "update_date = ?,\n" +
                    "amount = ?\n" +
                    "where id = ?";

    private static final String DELETE_EXPENSES_QUERY = "delete from expenses where id = ?";

    private static final String DOLLAR = "Dollar";
    private static final String EURO = "Euro";
    private static final String RUB = "Rub";

    private static final String ID_FIELD = "id";
    private static final String UPDATE_DATE_FIELD = "update_date";
    private static final String CURRENCY_ID_FIELD = "currency_id";
    private static final String AMOUNT_FIELD = "amount";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String SALE_PRICE_FIELD = "sale_price";
    private static final String MONTH_ID_FIELD = "month_id";
    private static final String YEAR_ID_FIELD = "year_id";

    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_MONTH = "month";
    private static final double FUND_QUERY_RESULT = 2000.0;

}
