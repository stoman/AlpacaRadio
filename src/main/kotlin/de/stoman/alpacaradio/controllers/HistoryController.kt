package de.stoman.alpacaradio.controllers

import de.stoman.alpacaradio.api.HistoryCurrentResponse
import de.stoman.alpacaradio.model.History
import de.stoman.alpacaradio.services.HistoryService
import de.stoman.alpacaradio.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/** Controller that makes [History] objects available via a REST API. */
@RestController
@RequestMapping("history")
class HistoryController(
  @Autowired private val historyService: HistoryService,
  @Autowired private val userService: UserService,
) {
  @GetMapping("/current")
  fun current(@AuthenticationPrincipal principal: OAuth2User): HistoryCurrentResponse {
    val user = userService.currentUser(principal)
    val history = historyService.currentlyPlayingVideo()
    return HistoryCurrentResponse(
      videoId = history.video.id,
      startFromSeconds = history.getStartVideoFromSeconds(),
      endSeconds = history.video.end.seconds,
      addedByName = history.video.addedBy.name,
      addedByPicture = history.video.addedBy.picture)
  }
}
