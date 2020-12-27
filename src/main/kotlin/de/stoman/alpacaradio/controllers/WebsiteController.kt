package de.stoman.alpacaradio.controllers

import de.stoman.alpacaradio.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

/** Controller to render all HTML pages of the website. */
@Controller
class WebsiteController(@Autowired private val userService: UserService) {
  @GetMapping("/")
  fun watch(model: Model, @AuthenticationPrincipal principal: OAuth2User?): String {
    if (principal != null) {
      model["user"] = userService.currentUser(principal)
    }
    return "watch"
  }
}
