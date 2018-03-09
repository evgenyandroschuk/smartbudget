package smartbudget.client.service;

import static org.mockito.Mockito.*;

import com.google.common.collect.ImmutableList;
import org.testng.Assert;
import org.testng.annotations.Test;
import smartbudget.model.ExpensesType;
import smartbudget.service.ApplicationProjectService;
import smartbudget.util.ApplicationProperties;

public class ApplicationProjectServiceTest {


    @Test
    public void testSuccess() {

        ApplicationProjectService service = mock(ApplicationProjectService.class);
        ApplicationProperties properties = mock(ApplicationProperties.class);

        when(service.getExpensesType()).thenReturn(ImmutableList.<ExpensesType>builder()
                .add(new ExpensesType("desc","desc", true,true))
                .build());

        Assert.assertNotNull(service.getExpensesType());

    }


}
