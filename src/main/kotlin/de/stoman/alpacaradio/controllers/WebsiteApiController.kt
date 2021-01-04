package de.stoman.alpacaradio.controllers

import de.stoman.alpacaradio.api.WebsiteAddVideoRequest
import de.stoman.alpacaradio.api.WebsiteApiAddVoteRequest
import de.stoman.alpacaradio.api.WebsiteApiCurrentVideoResponse
import de.stoman.alpacaradio.model.Video
import de.stoman.alpacaradio.model.VideoRepository
import de.stoman.alpacaradio.services.HistoryService
import de.stoman.alpacaradio.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException

/** Controller that adds a REST API to be used by website scripts. */
@RestController
@RequestMapping("api")
class WebsiteApiController(
  @Autowired private val historyService: HistoryService,
  @Autowired private val userService: UserService,
  @Autowired private val videoRepository: VideoRepository,
) {
  @GetMapping("/currentVideo")
  fun currentVideo(@AuthenticationPrincipal principal: OAuth2User): WebsiteApiCurrentVideoResponse {
    val user = userService.currentUser(principal)
    val history = historyService.currentlyPlayingVideo()
    return WebsiteApiCurrentVideoResponse(
      videoId = history.video.id,
      videoTitle = history.video.title,
      startFromSeconds = history.getStartVideoFromSeconds(),
      endSeconds = history.video.end.seconds,
      addedByName = history.video.addedBy.name,
      addedByPicture = history.video.addedBy.picture)
  }

  @PostMapping("/addVote")
  fun addVote(@ModelAttribute request: WebsiteApiAddVoteRequest, @AuthenticationPrincipal principal: OAuth2User) {
    val video = videoRepository.findById(request.videoId).orElseThrow {IllegalArgumentException("unknown video id")}
    val voteType = Video.VoteType.valueOf(request.voteType)
    val user = userService.currentUser(principal)
    videoRepository.save(video.addVote(user, voteType))
  }
}
