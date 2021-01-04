package de.stoman.alpacaradio.api

import de.stoman.alpacaradio.controllers.WebsiteApiController

/** Response object for [WebsiteApiController]'s `current` method. */
data class WebsiteApiGetCurrentVideoResponse(
  val videoId: String,
  val videoTitle: String,
  val startFromSeconds: Long,
  val endSeconds: Long,
  val addedByName: String,
  val addedByPicture: String,
  val userVote: String,
)
