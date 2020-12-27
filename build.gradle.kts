import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.4.1"
	id("io.spring.dependency-management") version "1.0.10.RELEASE"
	id("com.avast.gradle.docker-compose") version "0.3.21"
	kotlin("jvm") version "1.4.21"
	kotlin("plugin.spring") version "1.4.21"
}

group = "de.stoman"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}


dockerCompose {
	useComposeFiles = listOf("docker-compose.yml")
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
