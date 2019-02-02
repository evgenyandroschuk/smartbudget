package smartbudget.service;

import java.util.List;
import java.util.Map;

public interface CommonService {

    void createReplaceUserParams(int userId, int paramId, double value);

    double getUserParamValue(int userId, int paramId);

    String getUserParamUpdateDate(int userId, int paramId);

    long getMaxIdByNumerator(int id);

    void execute(String query);

    void updateCurrencyCost(int currencyId, double amount);



}
