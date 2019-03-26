package smartbudget.service.postres;

import com.google.common.collect.ImmutableMap;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import smartbudget.service.CommonService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class CommonServiceImpl extends AbstractDao implements CommonService {

    protected CommonServiceImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
    }

    @Override
    public void createOrReplaceUserParams(int userId, int paramId, BigDecimal value) {
        if (value.scale() != 2) {
            throw new IllegalArgumentException("Attrubute value must have scale = 2!");
        }
        String updateQuery = "update t_user_system_params set system_value = :paramValue, update_date = now() " +
            "where user_id = :userId and system_param_id = :paramId";

        String insertQuery = "insert into t_user_system_params (system_value, user_id, system_param_id, update_date) " +
            "values(:paramValue, :userId, :paramId, now())";



        Map<String, Object> params = ImmutableMap.of(
            "userId", userId,
            "paramId", paramId,
            "paramValue", value
        );

        int updatedRows = namedParameterJdbcTemplate.update(updateQuery, params);
        if (updatedRows == 0) {
            namedParameterJdbcTemplate.execute(insertQuery, params, PreparedStatement::execute);
        }
    }

    @Override
    public void createReplaceUserParams(int userId, int paramId, double value) {
        throw new NotImplementedException(); // implemented in createOrReplaceUserParams
    }

    @Override
    public double getUserParamValue(int userId, int paramId) {
        throw new NotImplementedException(); // implemented in getParamValue
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
                 if(rs.next()) {
                     return rs.getBigDecimal("system_value").setScale(2, BigDecimal.ROUND_CEILING);
                 } else {
                     throw new DataNotFoundException();
                 }
             }
         );
    }

    @Override
    public String getUserParamUpdateDateString(int userId, int paramId) {
        String query = "select update_date from t_user_system_params " +
            "where user_id = :userId and system_param_id = :paramId";

        Map<String, Object> params = ImmutableMap.of(
            "userId", userId,
            "paramId", paramId
        );
        Date date = namedParameterJdbcTemplate.query(query, params, rs -> {
            if(rs.next()) {
                return rs.getDate("update_date");
            } else {
                throw new DataNotFoundException("Exception while getting updateDate of system params");
            }
        });
        if (date == null) {
            throw new DataNotFoundException("update_date");
        }
        return date.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Override
    public long getMaxIdByNumerator(int id) {
        // Possible not actual, cause in postgreSql will be used sequence
        throw new NotImplementedException();
    }

    @Override
    public void updateCurrencyCost(int currencyId, double amount) {
        throw new NotImplementedException(); // implemented in updateCurrency()
    }

    @Override
    public void updateCurrency(int currencyId, BigDecimal price) {
        String query = "update currency set price = :price where id = :currencyId";
        Map<String, Object> params = ImmutableMap.of(
            "price", price,
            "currencyId", currencyId
        );
        namedParameterJdbcTemplate.execute(query, params, PreparedStatement::execute);
    }
}
