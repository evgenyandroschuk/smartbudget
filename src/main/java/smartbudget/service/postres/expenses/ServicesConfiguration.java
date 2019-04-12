package smartbudget.service.postres.expenses;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import smartbudget.service.CommonRepository;
import smartbudget.service.postres.DateProvider;

@Configuration
public class ServicesConfiguration {

    @Bean
    ExpensesDataService expensesDataService(
        ExpensesRepository expensesRepository,
        CommonRepository commonRepository,
        DateProvider dateProvider
    ) {
        return new ExpensesDataServiceImpl(expensesRepository, commonRepository, dateProvider);
    }

}
