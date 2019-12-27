package smartbudget.service;

import java.math.BigDecimal;

public interface CommonRepository {

    void createOrReplaceUserParams(int userId, int paramId, BigDecimal value);

    BigDecimal getParamValue(int userId, int paramId);

    String getUserParamUpdateDateString(int userId, int paramId);

    void updateCurrency(int currencyId, BigDecimal price);
}
