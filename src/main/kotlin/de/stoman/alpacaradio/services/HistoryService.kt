package de.stoman.alpacaradio.services

import de.stoman.alpacaradio.model.History
import de.stoman.alpacaradio.model.HistoryRepository
import de.stoman.alpacaradio.model.Video
import de.stoman.alpacaradio.model.VideoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Clock
import java.util.*
import kotlin.random.Random

/** Service to find the video that is currently playing or schedule a new video to be played if there is none. */
@Service
class HistoryService(
  @Autowired private val historyRepository: HistoryRepository,
  @Autowired private val videoRepository: VideoRepository,
  private val clock: Clock = Clock.systemUTC(),
) {
  private fun scoreVideo(video: Video): Double = Random.Default.nextDouble()

  fun currentlyPlayingVideo(): History {
    val current: History? = historyRepository.findFirstByOrderByStartedDesc()
    if (current != null && current.started.plus(current.video.length()).isAfter(clock.instant())) {
      return current
    }

    val nextVideo: Video =
      videoRepository.findAll().maxByOrNull { scoreVideo(it) } ?: throw RuntimeException("video database is empty")
    return History(id = UUID.randomUUID(), video = nextVideo, started = clock.instant())
      .also { historyRepository.insert(it) }
  }
}