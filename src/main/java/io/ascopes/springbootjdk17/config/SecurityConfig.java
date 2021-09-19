package io.ascopes.springbootjdk17.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  public static final String GREET_ROLE = "greet_me";

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //@formatter:off
    http.httpBasic()
          .realmName("Test App")
          .and()
        .authorizeRequests()
          .mvcMatchers(HttpMethod.GET, "/greetings/{name}")
            .hasRole(GREET_ROLE);
    //@formatter:on
  }

  /** Dummy password encoder that does nothing to encode the password. Replace with real backend. */
  @Bean
  @SuppressWarnings("deprecation")
  public PasswordEncoder dummyPasswordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }

  /** Dummy user details service to hold a user to use for testing. Replace with real backend. */
  @Bean
  public UserDetailsService dummyUserDetailsService() {
    UserDetails dummyUser = User
        .withUsername("ashley")
        .password("testpassword")
        .roles(GREET_ROLE)
        .build();

    return new InMemoryUserDetailsManager(dummyUser);
  }
}
