package smartbudget.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import smartbudget.util.AppProperties;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public WebSecurityConfig(AppProperties properties) {
        this.properties = properties;
    }

    AppProperties properties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String role = properties.getProperty("app.security.role");

        http
            .authorizeRequests()
            .antMatchers("/**")
            .hasRole(role).and()
            //.formLogin() В отличии от httpBasic выкидывает на страницу логина и пароля
            .httpBasic()
        ;
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {

        String userName = properties.getProperty("app.security.user");
        String password = properties.getProperty("app.security.password");
        String role = properties.getProperty("app.security.role");

        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username(userName)
                        .password(password)
                        .roles(role)
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
}
