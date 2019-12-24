package smartbudget.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableConfigurationProperties(WebSecurityConfig.SecuritySettings.class)
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public WebSecurityConfig(SecuritySettings securitySettings) {
        this.securitySettings = securitySettings;
    }

    SecuritySettings securitySettings;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String role = securitySettings.getRole();

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

        String userName = securitySettings.getUser();
        String password = securitySettings.getPassword();
        String role = securitySettings.getRole();

        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username(userName)
                        .password(password)
                        .roles(role)
                        .build();

        return new InMemoryUserDetailsManager(user);
    }

    @ConfigurationProperties(prefix = "security")
    public static class SecuritySettings {
        private String user;
        private String password;
        private String role;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
