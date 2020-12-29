package de.stoman.alpacaradio.api

import de.stoman.alpacaradio.model.Video

/** Data class representing a request to add a vote on a video. */
data class WebsiteApiAddVoteRequest(val videoId: String, val voteType: String)
