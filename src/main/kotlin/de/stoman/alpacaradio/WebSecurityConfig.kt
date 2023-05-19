package de.stoman.alpacaradio

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.csrf.CookieCsrfTokenRepository

@Configuration
@EnableWebSecurity
class WebSecurityConfig {
  @Bean
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    return http
      .authorizeHttpRequests {
        it
          .requestMatchers(*publicPages).permitAll()
          .anyRequest().authenticated()
      }
      .oauth2Login { it.defaultSuccessUrl("/").loginPage("/signin").permitAll() }
      .logout { it.logoutSuccessUrl("/").permitAll() }
      .csrf { it.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) }
      .build()
  }

  companion object {
    val publicPages =
      arrayOf("/", "/watchVideos", "/listVideos", "/api/getCurrentVideo", "/error", "/js/**", "/css/**", "/img/**")
  }
}