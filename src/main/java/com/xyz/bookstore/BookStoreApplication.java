package com.xyz.bookstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@SpringBootApplication
public class BookStoreApplication {

  public static void main(final String[] args) {
    SpringApplication.run(BookStoreApplication.class, args);
  }

  @EnableWebSecurity
  public static class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
      http
          .httpBasic()
          .and()
          .authorizeRequests()
          .antMatchers(GET, "/api/books/**").permitAll()
          .antMatchers(POST, "/api/books").hasRole("ADMIN")
          .anyRequest().authenticated()
          .and().csrf().disable();
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
      auth.inMemoryAuthentication()
          .withUser("admin").password("{noop}admin").roles("ADMIN");
    }
  }
}
