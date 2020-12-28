package de.stoman.alpacaradio.controllers

import de.stoman.alpacaradio.api.WebsiteApiCurrentVideoResponse
import de.stoman.alpacaradio.services.HistoryService
import de.stoman.alpacaradio.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/** Controller that adds a REST API to be used by website scripts. */
@RestController
@RequestMapping("api")
class WebsiteApiController(
  @Autowired private val historyService: HistoryService,
  @Autowired private val userService: UserService,
) {
  @GetMapping("/currentVideo")
  fun currentVideo(@AuthenticationPrincipal principal: OAuth2User): WebsiteApiCurrentVideoResponse {
    val user = userService.currentUser(principal)
    val history = historyService.currentlyPlayingVideo()
    return WebsiteApiCurrentVideoResponse(
      videoId = history.video.id,
      startFromSeconds = history.getStartVideoFromSeconds(),
      endSeconds = history.video.end.seconds,
      addedByName = history.video.addedBy.name,
      addedByPicture = history.video.addedBy.picture)
  }
}
