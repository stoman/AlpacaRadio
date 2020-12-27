package de.stoman.alpacaradio.model

import org.springframework.data.annotation.Id

/** Data class representing a user in AlpacaRadio's database. */
data class User(
  @Id val id: String, // `oAuthProvider/username`
  val oAuthProvider: String,
  val username: String,
  val name: String,
  val picture: String,
)
