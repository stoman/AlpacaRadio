package de.stoman.alpacaradio.services

import de.stoman.alpacaradio.model.User
import de.stoman.alpacaradio.model.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.net.URL

/** Service for accessing user objects from the database. */
@Service
class UserService(@Autowired private val userRepository: UserRepository) {
  /** Return the [User] object identified by the given principal and save it to the database. */
  fun currentUser(principal: OAuth2User): User {
    val user: User = when {
      principal.getAttribute<URL>("iss")?.host == "accounts.google.com" -> User(
        id = "google/${principal.getAttribute<String>("email")}",
        oAuthProvider = "google",
        username = principal.getAttribute<String>("email")!!,
        name = principal.getAttribute<String>("name")!!,
        picture = principal.getAttribute<String>("picture")!!
      )
      principal.getAttribute<String>("url")?.startsWith("https://api.github.com") ?: false -> User(
        id = "github/${principal.getAttribute<String>("login")}",
        oAuthProvider = "github",
        username = principal.getAttribute<String>("login")!!,
        name = principal.getAttribute<String>("name")!!,
        picture = principal.getAttribute<String>("avatar_url")!!
      )
      else -> throw RuntimeException("unknown oauth provider")
    }
    return userRepository.save(user)
  }
}