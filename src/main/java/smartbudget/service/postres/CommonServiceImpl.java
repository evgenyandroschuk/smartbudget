package smartbudget.service.postres;

import com.google.common.collect.ImmutableMap;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import smartbudget.service.CommonService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;
import java.util.Map;

public class CommonServiceImpl extends AbstractDao implements CommonService {

    protected CommonServiceImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
    }

    @Override
    public void createReplaceUserParams(int userId, int paramId, double value) {
        throw new NotImplementedException();
    }

    @Override
    public double getUserParamValue(int userId, int paramId) {
        throw new NotImplementedException();
    }

    @Override
    public BigDecimal getParamValue(int userId, int paramId) {
        String query = "select system_value from  t_user_system_params where user_id = :userId and system_param_id = :paramId";
        Map<String, Object> namedParameters = ImmutableMap.of(
            "userId", userId,
            "paramId", paramId
        );

        return namedParameterJdbcTemplate.query(
             query,
             namedParameters,
             rs -> {
                 BigDecimal bigDecimal = rs.getBigDecimal("system_value");
                 return bigDecimal.setScale(2, BigDecimal.ROUND_CEILING);
             }
         );
    }


    @Override
    public String getUserParamUpdateDate(int userId, int paramId) {
        throw new NotImplementedException();
    }

    @Override
    public long getMaxIdByNumerator(int id) {
        throw new NotImplementedException();
    }

    @Override
    public void updateCurrencyCost(int currencyId, double amount) {
        throw new NotImplementedException();
    }
}
