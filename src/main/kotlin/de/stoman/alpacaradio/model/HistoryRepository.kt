package de.stoman.alpacaradio.model

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

/** Repository for accessing [History] objects from the database. */
interface HistoryRepository : MongoRepository<History, UUID> {
  fun findFirstByOrderByStartedDesc(): History?
}
