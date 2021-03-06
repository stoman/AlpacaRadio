package de.stoman.alpacaradio.services

import de.stoman.alpacaradio.model.History
import de.stoman.alpacaradio.model.HistoryRepository
import de.stoman.alpacaradio.model.Video
import de.stoman.alpacaradio.model.VideoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.Duration
import java.util.*
import kotlin.math.tanh
import kotlin.random.Random

/** Service to find the video that is currently playing or schedule a new video to be played if there is none. */
@Service
class HistoryService(
  @Autowired private val historyRepository: HistoryRepository,
  @Autowired private val videoRepository: VideoRepository,
  private val clock: Clock = Clock.systemUTC(),
) {
  fun scoreVideo(
    video: Video,
    useBaseScore: Boolean = true, useRecencyScore: Boolean = true, useVotingScore: Boolean = true
  ): Double {
    // Score between 0 and 1. The higher `x`, the higher the score. The biggest increase is around `mean`.
    fun partialScore(x: Double, mean: Double = 0.0, scale: Double = 1.0): Double = 0.5 + 0.5 * tanh((x - mean) / scale)

    var score = 0.0

    if (useBaseScore) {
      score += 0.01
    }

    // Bonus for videos not played recently.
    if (useRecencyScore) {
      score += 0.5 * (Video.LAST_PLAYED_SIZE - video.lastPlayed.size)
      if (video.lastPlayed.isEmpty()) {
        score += 10.0
      }
      score += video.lastPlayed.map { Duration.between(it, clock.instant()).toMinutes() }
        .mapIndexed { i, minutes -> partialScore(minutes.toDouble(), mean = (i + 1) * 3 * 60.0, scale = 60.0) }.sum()
    }

    // Bonus for songs receiving a lot of upvotes.
    if (useVotingScore) {
      score += 2 * partialScore(video.votes.values.map {
        when (it) {
          Video.VoteType.UPVOTE -> 1
          Video.VoteType.DOWNVOTE -> -2
        }
      }.sum().toDouble(), mean = 2.0)
    }

    return score
  }

  fun currentlyPlayingVideo(): History {
    val current: History? = historyRepository.findFirstByOrderByStartedDesc()
    if (current != null && current.started.plus(current.video.length()).isAfter(clock.instant())) {
      // Current video is still playing, go on.
      return current
    }

    val nextVideo: Video = randomSample(videoRepository.findAll().associateWith { scoreVideo(it) })
      ?: throw RuntimeException("video database is empty")
    return History(id = UUID.randomUUID(), video = nextVideo, started = clock.instant())
      .also { historyRepository.save(it) }
      .also { videoRepository.save(nextVideo.addHistoryToLastPlayed(it)) }
  }

  companion object {
    /** Take a random sample from a weighted list. If the map to weights is empty returns null. */
    private fun <T> randomSample(weights: Map<T, Double>): T? {
      if (weights.values.any { it < 0 }) {
        throw IllegalArgumentException("weights have to be positive")
      }
      var v = Random.Default.nextDouble(weights.values.sum())
      for ((t, weight) in weights) {
        v -= weight
        if (v <= 0) {
          return t
        }
      }
      return null
    }
  }
}