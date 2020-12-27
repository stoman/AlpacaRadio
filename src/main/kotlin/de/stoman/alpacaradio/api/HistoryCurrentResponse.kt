package de.stoman.alpacaradio.api

import de.stoman.alpacaradio.controllers.HistoryController

/** Response object for [HistoryController]'s `current` method. */
data class HistoryCurrentResponse(
  val videoId: String,
  val startFromSeconds: Long,
  val endSeconds: Long,
  val addedByName: String,
  val addedByPicture: String,
)
