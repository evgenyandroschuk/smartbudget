package smartbudget.service.postres;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import smartbudget.service.CommonService;

@Configuration
public class PostgreSqlConfig {

    @Bean
    CommonService commonService(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new CommonServiceImpl(namedParameterJdbcTemplate);
    }

    @Bean
    VehicleServiceVersioned vehicleServiceVersioned(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new VehicleServiceImpl(namedParameterJdbcTemplate);
    }

}
