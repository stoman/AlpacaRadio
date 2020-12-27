package de.stoman.alpacaradio.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.util.*

/** Data  class for a history object representing a video started at a certain time. */
data class History(
  @Id val id: UUID,
  @DBRef val video: Video,
  val started: Instant,
) {
  fun getStartVideoFromSeconds(clock: Clock = Clock.systemUTC()): Long =
    Duration.between(started, clock.instant()).plus(video.start).seconds.coerceAtMost(video.end.seconds)
}
