package de.stoman.alpacaradio.api

/** Data class representing the data submitted using the form. */
data class WebsiteAddVideoRequest(val videoId: String, val title: String, val startSeconds: Long, val endSeconds: Long)