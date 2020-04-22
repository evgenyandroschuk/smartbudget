package smartbudget.service.postres.fund;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import smartbudget.model.funds.FundData;

import java.time.LocalDate;
import java.util.List;

public class FundDao {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public FundDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<FundData> getFundData(int userId, LocalDate start, LocalDate end) {


        return null;
    }
}
