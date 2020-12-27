package de.stoman.alpacaradio.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

/** Controller to render all HTML pages of the website. */
@Controller
class WebsiteController {
  @GetMapping("/")
  fun watch(model: Model): String = "watch"
}