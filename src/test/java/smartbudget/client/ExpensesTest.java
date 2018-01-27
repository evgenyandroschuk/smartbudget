package smartbudget.client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import smartbudget.controller.BudgetController;
import smartbudget.service.ExpensesService;


/**
 * Created by evgenyandroshchuk on 27.01.2018.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = BudgetController.class, secure = false)
public class ExpensesTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    ExpensesService expensesService;

    @Before
    public void startTest() {
        System.out.println("---------Starting to test Expenses services-----------------");
    }


    @Test
    public void connectionTest() throws Exception {

        String path = "/expenses/";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(path).accept(MediaType.TEXT_HTML);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.print("Testing: " + path);
        assert  HttpStatus.OK.value() == result.getResponse().getStatus() : "Expenses not available!";
        System.out.print("  - Test Success!\n");

    }

}
