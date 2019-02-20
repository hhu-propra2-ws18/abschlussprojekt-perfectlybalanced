package de.hhu.abschlussprojektverleihplattform.security.config;

import de.hhu.abschlussprojektverleihplattform.security.AuthenticatedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = AuthenticatedUserService.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticatedUserService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // hier werden Seiten alle Seiten ausser Login
                // eingetragen, die für jeden Besucher sichtbar sind
                .antMatchers("/", "/register**", "/h2-console/**")
                    .permitAll()
                .antMatchers("/profile/deposit**")
                    .hasRole("USER")
                // nur Admin-Berechtigung
                /*.antMatchers("/admin")
                    .hasRole("ADMIN")*/
                .anyRequest()
                    .authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/profile")
                    .permitAll()
                    .and()
                .csrf()
                    // hebelt Schutzfunktion fuer die H2-Konsole aus

                    .ignoringAntMatchers("/h2-console/**", "/lendingrequests/**")
                    .and()
                .headers()
                    .frameOptions()
                    .sameOrigin()
                    .and()
                .logout()
                    //.logoutSuccessUrl("/login")
                    .permitAll();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "**/favicon.ico");
    }

    @Autowired
    public void globalSecurityConfiguration(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}
