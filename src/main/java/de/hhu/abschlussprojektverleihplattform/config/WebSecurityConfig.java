package de.hhu.abschlussprojektverleihplattform.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = AuthenticatedUserService.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void globalSecurityAuthentification (AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user")
                .password("{noop}password")
                .roles("USER");
    }

    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    protected void configure (HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/register**", "/h2-console/**")
                    .permitAll().anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                .csrf()
                    .ignoringAntMatchers("/h2-console/**")//don't apply CSRF protection to /h2-console
                    .and()
                .headers().
                    frameOptions()
                    .sameOrigin()
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
                    .permitAll();
    }

    @Autowired
    public void globalSecurityConfiguration(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    /*
    * TODO:
    *   - Seiten freigeben, die öffentlich einsehbar sind
    *   - Testuser durch Datenbank-Einträge ersetzen
    *   - Thymeleaf-Anpassung
    *   - Controller-Anpassung (Aktuell: Weiterleitung zu Startseite ("/")
    *   - evtl Password-Handling
    *   - Zugriffsrechte (ADMIN, USER) oder getrennte Bereiche (User für Plattform, Admin für Konflikt)
    */
}
