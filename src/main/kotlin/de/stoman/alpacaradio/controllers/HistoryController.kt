package de.stoman.alpacaradio.controllers

import de.stoman.alpacaradio.api.HistoryCurrentResponse
import de.stoman.alpacaradio.model.History
import de.stoman.alpacaradio.services.HistoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/** Controller that makes [History] objects available via a REST API. */
@RestController
@RequestMapping("history")
class HistoryController(@Autowired private val historyService: HistoryService) {
  @GetMapping("/current")
  fun current(): HistoryCurrentResponse {
    val history = historyService.currentlyPlayingVideo()
    return HistoryCurrentResponse(
      videoId = history.video.id,
      startFromSeconds = history.getStartVideoFromSeconds(),
      endSeconds = history.video.end.seconds)
  }
}