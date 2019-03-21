package smartbudget.service.postres;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public abstract class AbstractDao {

    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    protected AbstractDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


}
