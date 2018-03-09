package smartbudget.client.service;

import static org.mockito.Mockito.*;

import com.google.common.collect.ImmutableList;
import org.testng.Assert;
import org.testng.annotations.Test;
import smartbudget.model.Expenses;
import smartbudget.model.ExpensesType;
import smartbudget.service.ApplicationProjectService;

import java.time.LocalDate;

public class ApplicationProjectServiceTest {


    @Test
    public void testGetExpTypeSuccess() {

        ApplicationProjectService service = mock(ApplicationProjectService.class);

        when(service.getExpensesType()).thenReturn(ImmutableList.<ExpensesType>builder()
                .add(new ExpensesType("desc","desc", true,true))
                .build());

        Assert.assertNotNull(service.getExpensesType());

    }

    @Test
    public void testGetExpensesSuccess() {
        ApplicationProjectService service = mock(ApplicationProjectService.class);
        when(service.getExpensesByMonthYear(1, 2018)).thenReturn(ImmutableList.<Expenses>builder()
        .add(new Expenses(
                15,
                1,
                2018,
                "expenses description",
                new ExpensesType("desc","desc", true,true),
                1200,
                LocalDate.now()

                ))
        .build()
        );
        Assert.assertNotNull(service.getExpensesByMonthYear(1, 2018));
    }

    @Test
    public void testGetExpensesByQueryString() {
        ApplicationProjectService service = mock(ApplicationProjectService.class);
        when(service.getExpensesByQueryString("select * from expenses where month_id = 1 and year_id = 2018"))
                .thenReturn(ImmutableList.<Expenses>builder()
                .add(new Expenses(
                        15,
                        1,
                        2018,
                        "expenses description",
                        new ExpensesType("desc","desc", true,true),
                        1200,
                        LocalDate.now()
                )).build()
                );
        Assert.assertNotNull(service.getExpensesByQueryString("select * from expenses where month_id = 1 and year_id = 2018"));
    }

    @Test
    public void testGetExpensesByYear() {
        ApplicationProjectService service = mock(ApplicationProjectService.class);
        when(service.getExpensesByYear(2018))
                .thenReturn(ImmutableList.<Expenses>builder()
                        .add(new Expenses(
                                15,
                                1,
                                2018,
                                "expenses description",
                                new ExpensesType("desc","desc", true,true),
                                1200,
                                LocalDate.now()
                        )).build()
                );
        Assert.assertNotNull(service.getExpensesByYear(2018));
    }

    @Test
    public void testGetExpensesByTypeMonthYear() {
        ApplicationProjectService service = mock(ApplicationProjectService.class);
        when(service.getExpensesByTypeMonthYear(1,1,2018))
                .thenReturn(ImmutableList.<Expenses>builder()
                        .add(new Expenses(
                                15,
                                1,
                                2018,
                                "expenses description",
                                new ExpensesType("desc","desc", true,true),
                                1200,
                                LocalDate.now()
                        )).build()
        );

        Assert.assertNotNull(service.getExpensesByTypeMonthYear(1,1,2018));

    }

    @Test
    public void testGetExpensesByTypeYear() {
        ApplicationProjectService service = mock(ApplicationProjectService.class);
        when(service.getExpensesByTypeYear(1,2018))
                .thenReturn(ImmutableList.<Expenses>builder()
                        .add(new Expenses(
                                15,
                                1,
                                2018,
                                "expenses description",
                                new ExpensesType("desc","desc", true,true),
                                1200,
                                LocalDate.now()
                        )).build()
                );
        Assert.assertNotNull(service.getExpensesByTypeYear(1,2018));
    }

    @Test
    public void testGetExpensesByTypeAndPeriod() {
        ApplicationProjectService service = mock(ApplicationProjectService.class);
        when(service.getExpensesByTypeAndPeriod(1,"2018-01-01", "2018-01-14"))
                .thenReturn(ImmutableList.<Expenses>builder()
                        .add(new Expenses(
                                15,
                                1,
                                2018,
                                "expenses description",
                                new ExpensesType("desc","desc", true,true),
                                1200,
                                LocalDate.now()
                        )).build()
                );
        Assert.assertNotNull(service.getExpensesByTypeAndPeriod(1,"2018-01-01", "2018-01-14"));
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testErrorGetExpensesByTypeAndPeriod() {
        ApplicationProjectService service = mock(ApplicationProjectService.class);
        when(service.getExpensesByTypeAndPeriod(1,"2018.01.01", "2018-01.14"))
                .thenThrow(new RuntimeException("date must be in YYYY-MM-DD format"));

        service.getExpensesByTypeAndPeriod(1,"2018.01.01", "2018-01.14");
    }

    @Test
    public void testGetExpensesByDescriptionPeriod() {

        ApplicationProjectService service = mock(ApplicationProjectService.class);
        when(service.getExpensesTypeByDescriptionPeriod("tinkoff","2018-01-01", "2018-01-14"))
                .thenReturn(ImmutableList.<Expenses>builder()
                        .add(new Expenses(
                                15,
                                1,
                                2018,
                                "expenses description",
                                new ExpensesType("desc","desc", true,true),
                                1200,
                                LocalDate.now()
                        )).build()
                );
        Assert.assertNotNull(service.getExpensesTypeByDescriptionPeriod("tinkoff","2018-01-01", "2018-01-14"));
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testErrorGetExpensesByDescriptionPeriod() {
        ApplicationProjectService service = mock(ApplicationProjectService.class);
        when(service.getExpensesByTypeAndPeriod(1,"2018-01-01", "2018-01-14")).thenThrow(
                new RuntimeException("date must be in YYYY-MM-DD format")
        );
        service.getExpensesByTypeAndPeriod(1,"2018-01-01", "2018-01-14");

    }

}
