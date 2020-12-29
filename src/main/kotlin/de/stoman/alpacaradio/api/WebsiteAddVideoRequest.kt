package de.stoman.alpacaradio.api

/** Data class representing the data submitted using the form to add a new video. */
data class WebsiteAddVideoRequest(val videoId: String, val title: String, val startSeconds: Long, val endSeconds: Long)