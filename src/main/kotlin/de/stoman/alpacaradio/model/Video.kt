package de.stoman.alpacaradio.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import java.time.Duration

/** Data class for a video represented in AlpacaRadio's database. */
data class Video(
  @Id val id: String,
  val title: String,
  val start: Duration,
  val end: Duration,
  @DBRef val addedBy: User,
) {
  fun length(): Duration = end.minus(start)
}
