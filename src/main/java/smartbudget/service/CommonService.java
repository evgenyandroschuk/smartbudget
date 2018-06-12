package smartbudget.service;

import java.util.List;
import java.util.Map;

public interface CommonService {

    void createReplaceUserParams(int userId, int paramId, double value);

    double getUserParamValue(int userId, int paramId);

    long getMaxIdByNumerator(int id);

    List<Map<String, Object>> getQueryRequest(String query);

    void execute(String query);



}
