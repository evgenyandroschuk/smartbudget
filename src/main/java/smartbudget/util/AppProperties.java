package smartbudget.util;

import org.springframework.core.env.Environment;

public interface AppProperties {

    String getProperty(String key);
    void setEnvironment(Environment environment);
}
