package smartbudget.service.postres.fund;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import smartbudget.model.funds.FundData;

import java.sql.PreparedStatement;
import java.time.LocalDate;
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

    public void setFund(FundData fundData) {
        String insert = "insert into funds(id, user_id, currency_id, amount, price, description, update_date)\n" +
            "values (nextval('funds_seq'), :userId, :currencyId, :amount, :price, :description, now())";
        Map<String, Object> params = ImmutableMap.of(
            "userId", fundData.getUserId(),
            "currencyId", fundData.getCurrency().getId(),
            "amount", fundData.getAmount(),
            "price", fundData.getPrice(),
            "description", fundData.getDescription()
        );
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
