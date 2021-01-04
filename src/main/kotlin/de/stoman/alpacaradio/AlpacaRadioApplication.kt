package de.stoman.alpacaradio

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository

@SpringBootApplication
class AlpacaRadioApplication : WebSecurityConfigurerAdapter() {
  override fun configure(http: HttpSecurity) {
    val publicPages =
      arrayOf("/", "/watch", "/listVideos", "/api/currentVideo", "/error", "/js/**", "/css/**", "/img/**")

    http
      .authorizeRequests {
        it.antMatchers(*publicPages).permitAll()
          .anyRequest().authenticated()
      }
      .oauth2Login { it.defaultSuccessUrl("/").loginPage("/signin").permitAll() }
      .logout { it.logoutSuccessUrl("/").permitAll() }
      .csrf { it.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) }
  }
}

fun main(args: Array<String>) {
  runApplication<AlpacaRadioApplication>(*args)
}
