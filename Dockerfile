FROM eclipse-temurin:17
WORKDIR /home
COPY ./target/quiz_application-0.0.1-SNAPSHOT.jar quiz_application.jar
ENTRYPOINT ["java", "-jar", "quiz_application.jar"]