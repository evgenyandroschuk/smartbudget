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
import java.util.LinkedList;
import java.util.List;
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
        List<Currency> currencies = getCurrencies();
        return currencies.stream()
            .filter(t -> t.getId() == id)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Currency not found!"));
    }

    public List<Currency> getCurrencies() {
        return currencyMap.getUnchecked("currencies")
            .orElseThrow(() -> new RuntimeException("No any currencies"));
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

    private LoadingCache<String, Optional<List<Currency>>> currencyMap = CacheBuilder.newBuilder()
        .refreshAfterWrite(2, TimeUnit.HOURS)
        .build(new CacheLoader<String, Optional<List<Currency>>>() {
            @Override
            public Optional<List<Currency>> load(String key) throws Exception {
                return loadCache(key);
            }
        });


    private Optional<List<Currency>> loadCache(String key) {
        String query = "select * from t_currency order by id";
        return namedParameterJdbcTemplate.query(query, rs -> {
            List<Currency> currencies = new LinkedList<>();
            while(rs.next()) {
                Currency currency = new Currency(
                    rs.getInt("id"),
                    rs.getString("description"),
                    rs.getDate("update_date").toLocalDate(),
                    rs.getBigDecimal("price")
                );
                currencies.add(currency);
            }
            return Optional.of(currencies);
        });

    }

}
