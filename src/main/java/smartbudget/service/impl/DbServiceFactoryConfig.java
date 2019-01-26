package smartbudget.service.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import smartbudget.service.DbServiceFactoryImpl;
import smartbudget.service.services.DbServiceFactory;
import smartbudget.util.AppProperties;

@Configuration
public class DbServiceFactoryConfig {

    @Bean
    DbServiceFactory dbServiceFactory(AppProperties appProperties) {
        return new DbServiceFactoryImpl(appProperties);
    }

}
