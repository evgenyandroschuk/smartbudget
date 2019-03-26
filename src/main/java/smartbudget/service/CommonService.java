package smartbudget.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CommonService {

    void createOrReplaceUserParams(int userId, int paramId, BigDecimal value);

    void createReplaceUserParams(int userId, int paramId, double value);

    double getUserParamValue(int userId, int paramId);

    BigDecimal getParamValue(int userId, int paramId);

    String getUserParamUpdateDateString(int userId, int paramId);

    long getMaxIdByNumerator(int id);

    void updateCurrencyCost(int currencyId, double amount);


    void updateCurrency(int currencyId, BigDecimal price);
}
