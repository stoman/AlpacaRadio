package de.stoman.alpacaradio.model

import org.springframework.data.mongodb.repository.MongoRepository

/** Repository for accessing [User] objects from the database. */
interface UserRepository: MongoRepository<User, String>