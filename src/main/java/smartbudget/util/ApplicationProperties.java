package smartbudget.util;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class ApplicationProperties implements AppProperties {

    private final Environment environment;

    public ApplicationProperties(Environment environment) {
        this.environment = environment;
    }

    public String getProperty(String key) {
        return environment.getProperty(key);
    }



}
