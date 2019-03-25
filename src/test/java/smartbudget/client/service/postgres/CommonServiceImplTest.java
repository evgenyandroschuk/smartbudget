package smartbudget.client.service.postgres;

import com.google.common.collect.ImmutableMap;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.any;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
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

    @AfterMethod
    public void resetMocks() {
        reset(namedParameterJdbcTemplate);
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

    @Test(dataProvider = "createParamSuccessProvider")
    public void testCreateParamSuccess(
        int userId, int paramId, BigDecimal value, int updatedRows, int countOfExecute
    ) {
        String updateQuery = "update t_user_system_params set system_value = :paramValue, update_date = now() " +
            "where user_id = :userId and system_param_id = :paramId";

        String insertQuery = "insert into t_user_system_params (system_value, user_id, system_param_id, update_date) " +
            "values(:paramValue, :userId, :paramId, now())";

        Map<String, Object> params = ImmutableMap.of(
            "userId", userId,
            "paramId", paramId,
            "paramValue", value
        );

        when(namedParameterJdbcTemplate.update(updateQuery, params)).thenReturn(updatedRows);

        when(namedParameterJdbcTemplate.execute(
            eq(insertQuery), eq(params), (PreparedStatementCallback<Boolean>) any(PreparedStatementCallback.class))).thenReturn(true);

        commonService.createOrReplaceUserParams(userId, paramId, value);

        verify(namedParameterJdbcTemplate).update(updateQuery, params);

        verify(namedParameterJdbcTemplate, times(countOfExecute)).execute(
            eq(insertQuery),
            eq(params),
            (PreparedStatementCallback<Boolean>) any(PreparedStatementCallback.class)
        );
    }

    @DataProvider
    private Object[][] createParamSuccessProvider() {
        return new Object[][] {
            {
                1,
                12,
                BigDecimal.valueOf(10000, 2),
                0, // updatedRows
                1 // count of execute
            },
            {
                1,
                12,
                BigDecimal.valueOf(10000, 2),
                1, // updatedRows
                0 // count of execute
            }
        };
    }

    @Test(
        dataProvider = "createParamErrorProvider",
        expectedExceptions = IllegalArgumentException.class,
        expectedExceptionsMessageRegExp = "Attrubute value must have scale = 2!"
    )
    public void testCreateParamError(int userId, int paramId, BigDecimal value) {
        commonService.createOrReplaceUserParams(userId, paramId, value);
    }

    @DataProvider
    public Object[][] createParamErrorProvider()  {
        return new Object[][] {
            {
                1, 12, BigDecimal.valueOf(100002, 1)
            },
            {
                1, 12, BigDecimal.valueOf(100002, 3)
            }
        };
    }

}
