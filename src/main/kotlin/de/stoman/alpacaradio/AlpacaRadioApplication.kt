package de.stoman.alpacaradio

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.csrf.CookieCsrfTokenRepository

@SpringBootApplication
class AlpacaRadioApplication : WebSecurityConfigurerAdapter() {
  override fun configure(http: HttpSecurity) {
    http.authorizeRequests {
      it.antMatchers("/", "/error", "/js/**", "/css/**").permitAll().anyRequest().authenticated()
    }.logout { it.logoutSuccessUrl("/").permitAll() }
      .exceptionHandling { it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) }
      .csrf { it.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) }.oauth2Login()
      .defaultSuccessUrl("/", true)
  }
}

fun main(args: Array<String>) {
  runApplication<AlpacaRadioApplication>(*args)
}
