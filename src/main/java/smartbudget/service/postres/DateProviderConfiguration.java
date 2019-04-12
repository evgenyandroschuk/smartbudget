package smartbudget.service.postres;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DateProviderConfiguration {

    @Bean
    DateProvider dateProvider() {
        return LocalDate::now;
    }

}
