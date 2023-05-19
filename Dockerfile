FROM openjdk:17-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY build/dependency/BOOT-INF/lib /app/lib
COPY build/dependency/META-INF /app/META-INF
COPY build/dependency/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","de.stoman.alpacaradio.AlpacaRadioApplication"]