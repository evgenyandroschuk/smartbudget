package smartbudget.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class ApplicationProperties implements AppProperties {


    @Autowired
    Environment environment;

    public String getProperty(String key) {
        return environment.getProperty(key);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


}
