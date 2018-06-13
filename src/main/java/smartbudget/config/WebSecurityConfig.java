package smartbudget.config;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AppProperties properties;
@Override
    protected void configure(HttpSecurity http) throws Exception {
        String role = properties.getProperty("app.security.role");

        http
                .csrf().disable()
                .authorizeRequests()
                .mvcMatchers("/", "/home", "/budget/fund/").hasAuthority(role)
                .mvcMatchers("/**")
                .permitAll()
                .anyRequest().hasAuthority(role)
                .and()
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
