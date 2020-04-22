package smartbudget.service.postres.fund;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import smartbudget.model.funds.Currency;

import java.sql.PreparedStatement;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class CurrencyDao {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public CurrencyDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    public Currency getCurrencyById(Integer id) {
        return currencyMap.getUnchecked(id).orElseThrow(
            () -> new RuntimeException("Currency not found!")
        );
    }

    public void setCurrency(Currency currency) {
        String update = "update t_currency \n" +
            "set description = :description, update_date = now(), price = :price \n" +
            "where id = :id";
        Map<String, Object> params = ImmutableMap.of(
            "id", currency.getId(),
            "description", currency.getDescription(),
            "price", currency.getPrice()
        );
        namedParameterJdbcTemplate.execute(update, params, PreparedStatement::execute);
    }

    private LoadingCache<Integer, Optional<Currency>> currencyMap = CacheBuilder.newBuilder()
        .refreshAfterWrite(2, TimeUnit.HOURS)
        .build(new CacheLoader<Integer, Optional<Currency>>() {
            @Override
            public Optional<Currency> load(Integer key) throws Exception {
                return loadCache(key);
            }
        });


    private Optional<Currency> loadCache(Integer id) {
        String query = "select * from t_currency where id = :id";
        return namedParameterJdbcTemplate.query(
            query,
            ImmutableMap.of("id", id),
            rs -> {
                Currency currency = null;
                if (rs.next()) {
                    currency = new Currency(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getDate("update_date").toLocalDate(),
                        rs.getBigDecimal("price"));

                }
                return Optional.ofNullable(currency);
            });
    }

}
