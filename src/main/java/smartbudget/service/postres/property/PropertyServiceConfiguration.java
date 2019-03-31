package smartbudget.service.postres.property;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class PropertyServiceConfiguration {

    @Bean
    PropertyService propertyService(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new PropertyServiceImpl(namedParameterJdbcTemplate);
    }

}
