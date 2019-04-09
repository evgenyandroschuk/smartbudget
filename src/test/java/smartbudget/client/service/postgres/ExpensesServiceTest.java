package smartbudget.client.service.postgres;

import com.google.common.collect.ImmutableList;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import smartbudget.model.expenses.ExpensesType;
import smartbudget.service.postres.expenses.ExpensesServiceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;


public class ExpensesServiceTest {

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @InjectMocks
    private ExpensesServiceImpl expensesService;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void resetMocks() {
        reset(namedParameterJdbcTemplate);
    }

    @Test
    public void testGetExpensesTypes() {
        String query = "select id, user_id, expenses_type_id, description, is_income from t_expenses_type";

        List<ExpensesType> expensesTypes = ImmutableList.of(
            new ExpensesType(1,1,1, "Others", false),
            new ExpensesType(1,2,1, "Health", false)
        );
        when(namedParameterJdbcTemplate.query(
            eq(query), (ResultSetExtractor<List<ExpensesType>>) any(ResultSetExtractor.class))
        ).thenReturn(expensesTypes);

        List<ExpensesType> result = expensesService.getExpensesTypes();
        Assert.assertEquals(result.get(1).getDescription(), "Health");


    }

}
