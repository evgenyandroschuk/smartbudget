package smartbudget.service.postres.fund;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import smartbudget.model.funds.FundData;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FundDao {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private CurrencyDao currencyDao;

    @Autowired
    public FundDao(
        NamedParameterJdbcTemplate namedParameterJdbcTemplate,
        CurrencyDao currencyDao
    ) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.currencyDao = currencyDao;
    }

    public List<FundData> getFundData(int userId, LocalDate start, LocalDate end) {
        String query = "select * from funds where user_id =:userId order by update_date, id desc";
        Map<String, Object> params = ImmutableMap.of("userId", userId);
        List<FundData> result = namedParameterJdbcTemplate.query(query, params, rs -> {
            List<FundData> fundDataList = new LinkedList<>();
            while (rs.next()) {
                FundData data = new FundData(
                    rs.getLong("id"),
                    rs.getInt("user_id"),
                    currencyDao.getCurrencyById(rs.getInt("currency_id")),
                    rs.getBigDecimal("amount"),
                    rs.getBigDecimal("price"),
                    rs.getString("description"),
                    rs.getDate("update_date").toLocalDate()
                );
                fundDataList.add(data);
            }
            return fundDataList;
        });
        return result.stream()
            .filter(t -> t.getLocalDate().isAfter(start) && t.getLocalDate().isBefore(end.plusDays(1)))
            .collect(Collectors.toList());
    }

    public void addFund(FundData fundData) {
        LocalDate updateDate = fundData.getLocalDate() != null ? fundData.getLocalDate() : LocalDate.now();
        String insert = "insert into funds(id, user_id, currency_id, amount, price, description, update_date)\n" +
            "values (nextval('funds_seq'), :userId, :currencyId, :amount, :price, :description, :updateDate)";
        Map<String, Object> params = new HashMap<>();
        params.put("userId", fundData.getUserId());
        params.put("currencyId", fundData.getCurrency().getId());
        params.put("amount", fundData.getAmount());
        params.put("description", fundData.getDescription());
        params.put("price", fundData.getPrice());
        params.put("updateDate", Date.valueOf(updateDate));
        namedParameterJdbcTemplate.execute(insert, params, PreparedStatement::execute);
    }

    public void deleteFund(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null!");
        }
        String delete = "delete from funds where id = :id";
        Map<String, Object> params = ImmutableMap.of("id", id);
        namedParameterJdbcTemplate.execute(delete, params, PreparedStatement::execute);
    }
}
