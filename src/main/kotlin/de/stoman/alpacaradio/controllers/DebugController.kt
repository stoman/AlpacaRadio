package de.stoman.alpacaradio.controllers

import de.stoman.alpacaradio.model.Video
import de.stoman.alpacaradio.model.VideoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

/** Controller for debugging utilities during development. */
@RestController
@RequestMapping("debug")
class DebugController(@Autowired private val videoRepository: VideoRepository) {
  @RequestMapping("/populate")
  fun populate(): String {
    videoRepository.saveAll(listOf(
      Video(
        id = "dQw4w9WgXcQ",
        title = "Rick Astley - Never Gonna Give You Up",
        start = Duration.ZERO, end = Duration.ofSeconds((3 * 60 + 32).toLong())),
      Video(
        id = "tZwTeyw8c8g",
        title = "Die Ärzte - Himmelblau",
        start = Duration.ZERO, end = Duration.ofSeconds((3 * 60 + 13).toLong()))))
    return "ok"
  }

  @RequestMapping("/list")
  fun list(): String = videoRepository.findAll().joinToString(", ")
}