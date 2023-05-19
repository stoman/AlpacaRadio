import com.avast.gradle.dockercompose.ComposeExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.7"
	id("io.spring.dependency-management") version "1.1.0"
	id("com.avast.gradle.docker-compose") version "0.16.12"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
}

group = "de.stoman"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

configure<ComposeExtension> {
	useComposeFiles.set(listOf("docker-compose.yml"))
}

tasks.composeUp {
	mustRunAfter("bootBuildImage")
}

tasks.register("runAlpacaRadio") {
	version = "DEV"
	dependsOn("bootBuildImage")
	dependsOn("composeUp")
}

tasks.register("pushToRegistry") {
	dependsOn("bootBuildImage")
		doLast {
			exec {
				executable("docker")
				args("tag", "docker.io/library/alpacaradio:${version}", "registry.stoman.de/alpacaradio:${version}")
			}
			exec {
				executable("docker")
				args("push", "registry.stoman.de/alpacaradio:${version}")
			}
			println("pushed registry.stoman.de/alpacaradio:${version}")
		}
}
