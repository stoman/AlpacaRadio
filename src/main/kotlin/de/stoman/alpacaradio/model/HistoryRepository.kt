package de.stoman.alpacaradio.model

import org.springframework.data.mongodb.repository.MongoRepository

/** Repository for accessing [History] objects from the database. */
interface HistoryRepository: MongoRepository<History, String> {
  fun findFirstByOrderByStartedDesc(): History?
}