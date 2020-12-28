package de.stoman.alpacaradio.controllers

import de.stoman.alpacaradio.api.WebsiteAddVideoRequest
import de.stoman.alpacaradio.model.Video
import de.stoman.alpacaradio.model.VideoRepository
import de.stoman.alpacaradio.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.RedirectView
import java.time.Duration

/** Controller to render all HTML pages of the website. */
@Controller
class WebsiteController(
  @Autowired private val userService: UserService,
  @Autowired private val videoRepository: VideoRepository,
) {
  @GetMapping("/")
  fun watch(model: Model, @AuthenticationPrincipal principal: OAuth2User?): String {
    if (principal != null) {
      model["user"] = userService.currentUser(principal)
    }
    return "watch"
  }

  @GetMapping("/addVideo")
  fun addVideoForm(
    model: Model,
    @AuthenticationPrincipal principal: OAuth2User,
    @RequestParam videoExists: Boolean = false
  ): String {
    model["user"] = userService.currentUser(principal)
    model["videoExists"] = videoExists
    return "add"
  }

  @PostMapping("/addVideo")
  fun addVideoSubmit(
    @ModelAttribute formData: WebsiteAddVideoRequest,
    @AuthenticationPrincipal principal: OAuth2User,
  ): ModelAndView {
    if (videoRepository.existsById(formData.videoId)) {
      return ModelAndView(RedirectView("/addVideo?videoExists=true", true))
    }

    videoRepository.save(Video(
      id = formData.videoId,
      title = formData.title,
      start = Duration.ofSeconds(formData.startSeconds),
      end = Duration.ofSeconds(formData.endSeconds),
      addedBy = userService.currentUser(principal)))
    return ModelAndView(RedirectView("/", true))
  }

  @GetMapping("/listVideos")
  fun listVideos(model: Model): String {
    model["videos"] = videoRepository.findAll(Sort.by("title"))
    return "list"
  }
}
