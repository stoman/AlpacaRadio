package de.stoman.alpacaradio.controllers

import de.stoman.alpacaradio.api.WebsiteApiAddVoteRequest
import de.stoman.alpacaradio.api.WebsiteApiGetCurrentVideoResponse
import de.stoman.alpacaradio.model.Video
import de.stoman.alpacaradio.model.VideoRepository
import de.stoman.alpacaradio.services.HistoryService
import de.stoman.alpacaradio.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

/** Controller that adds a REST API to be used by website scripts. */
@RestController
@RequestMapping("api")
class WebsiteApiController(
  @Autowired private val historyService: HistoryService,
  @Autowired private val userService: UserService,
  @Autowired private val videoRepository: VideoRepository,
) {
  @GetMapping("/getCurrentVideo")
  fun getCurrentVideo(@AuthenticationPrincipal principal: OAuth2User?): WebsiteApiGetCurrentVideoResponse {
    val history = historyService.currentlyPlayingVideo()
    return WebsiteApiGetCurrentVideoResponse(
      videoId = history.video.id,
      videoTitle = history.video.title,
      startFromSeconds = history.getStartVideoFromSeconds(),
      endSeconds = history.video.end.seconds,
      addedByName = history.video.addedBy.name,
      addedByPicture = history.video.addedBy.picture,
      userVote = principal?.let { history.video.votes[userService.currentUser(it).id]?.name } ?: "")
  }

  @PostMapping("/addVote", consumes = ["application/json"])
  fun addVote(@RequestBody request: WebsiteApiAddVoteRequest, @AuthenticationPrincipal principal: OAuth2User) {
    val video = videoRepository.findById(request.videoId)
      .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found") }
    val user = userService.currentUser(principal)
    try {
      val voteType: Video.VoteType? = if (request.voteType.isBlank()) null else Video.VoteType.valueOf(request.voteType)
      videoRepository.save(video.addVote(user, voteType))
    } catch (e: IllegalArgumentException) {
      throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Vote type not found")
    }
  }
}
