package de.stoman.alpacaradio.controllers

import de.stoman.alpacaradio.model.User
import de.stoman.alpacaradio.model.UserRepository
import de.stoman.alpacaradio.model.Video
import de.stoman.alpacaradio.model.VideoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

/** Controller for debugging utilities during development. */
@RestController
@RequestMapping("debug")
class DebugController(
  @Autowired private val userRepository: UserRepository,
  @Autowired private val videoRepository: VideoRepository,
) {
  @RequestMapping("/populate")
  fun populate(): String {
    val user = userRepository.save(
      User(
        id = "test/tester",
        oAuthProvider = "test",
        username = "tester",
        name = "Testy McTestface",
        picture = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e5/Rick_Astley-cropped.jpg/170px-Rick_Astley-cropped.jpg"))
    videoRepository.saveAll(listOf(
      Video(
        id = "dQw4w9WgXcQ",
        title = "Rick Astley - Never Gonna Give You Up",
        start = Duration.ZERO, end = Duration.ofSeconds((3 * 60 + 32).toLong()),
        addedBy = user),
      Video(
        id = "tZwTeyw8c8g",
        title = "Die Ärzte - Himmelblau",
        start = Duration.ZERO, end = Duration.ofSeconds((3 * 60 + 13).toLong()),
        addedBy = user)))
    return "ok"
  }
}
