package de.stoman.alpacaradio.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import java.time.Duration
import java.time.Instant

/** Data class for a video represented in AlpacaRadio's database. */
data class Video(
  @Id val id: String,
  val title: String,
  val start: Duration,
  val end: Duration,
  @DBRef val addedBy: User,
  // Contains the [LAST_PLAYED_SIZE] last times this was played. Ordered descending.
  var lastPlayed: List<Instant> = listOf(),
) {
  fun length(): Duration = end.minus(start)

  fun addHistoryToLastPlayed(history: History): Video {
    val newLastPlayed = (lastPlayed + listOf(history.started)).sortedDescending()
    lastPlayed = newLastPlayed.subList(0, LAST_PLAYED_SIZE.coerceAtMost(newLastPlayed.size))
    return this
  }

  companion object {
    const val LAST_PLAYED_SIZE = 5
  }
}
