package de.stoman.alpacaradio.model

import org.springframework.data.mongodb.repository.MongoRepository

/** Repository for accessing [Video] objects from the database. */
interface VideoRepository : MongoRepository<Video, String>
