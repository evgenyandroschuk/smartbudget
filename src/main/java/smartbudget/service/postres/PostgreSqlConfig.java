package smartbudget.service.postres;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import smartbudget.service.CommonRepository;
import smartbudget.service.postres.expenses.ExpensesRepository;
import smartbudget.service.postres.expenses.ExpensesRepositoryImpl;
import smartbudget.service.postres.property.PropertyRepository;
import smartbudget.service.postres.property.PropertyRepositoryImpl;

@Configuration
public class PostgreSqlConfig {

    @Bean
    CommonRepository commonRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new CommonRepositoryImpl(namedParameterJdbcTemplate);
    }

    @Bean
    VehicleRepository vehicleRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new VehicleRepositoryImpl(namedParameterJdbcTemplate);
    }

    @Bean
    PropertyRepository propertyRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new PropertyRepositoryImpl(namedParameterJdbcTemplate);
    }

    @Bean
    public ExpensesRepository expensesRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new ExpensesRepositoryImpl(namedParameterJdbcTemplate);
    }

}
