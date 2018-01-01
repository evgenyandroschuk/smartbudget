package smartbudget;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Created by evgenyandroshchuk on 19.12.17.
 */
@SpringBootApplication
@EnableConfigurationProperties
public class Application {

    public static void main(String [] args) {
        SpringApplication.run(Application.class, args);
    }
}
