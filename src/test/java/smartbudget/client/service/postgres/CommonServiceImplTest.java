package smartbudget.client.service.postgres;

import com.google.common.collect.ImmutableMap;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.any;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import smartbudget.service.postres.CommonServiceImpl;

import java.math.BigDecimal;
import java.util.Map;

public class CommonServiceImplTest {

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @InjectMocks
    private CommonServiceImpl commonService;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(dataProvider = "paramValueProvider")
    public void testParamValue(
        int userId,
        int paramId,
        BigDecimal returnAmount
    ) {
        String query = "select system_value from  t_user_system_params where user_id = :userId and system_param_id = :paramId";
        Map<String, Object> namedParameters = ImmutableMap.of(
            "userId", userId,
            "paramId", paramId
        );
        when(namedParameterJdbcTemplate.query(
            eq(query),
            eq(namedParameters),
            (ResultSetExtractor<BigDecimal>) any(ResultSetExtractor.class))
        ).thenReturn(returnAmount);

        BigDecimal result = commonService.getParamValue(userId, paramId);
        Assert.assertEquals(result, returnAmount);
        verify(namedParameterJdbcTemplate).query(eq(query), eq(namedParameters), any(ResultSetExtractor.class));
    }


    @DataProvider
    private Object[][] paramValueProvider() {
        return new Object[][] {
            {
                1, 12, BigDecimal.valueOf(100.00)
            },
            {
                2, 10, BigDecimal.valueOf(100.01)
            }
        };
    }

}
