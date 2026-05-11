FROM eclipse-temurin:21-jre
WORKDIR /app
COPY build/libs/chat-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/ssl/kafka.server.truststore.jks ssl/kafka.server.truststore.jks
ENTRYPOINT ["java", "-jar", "app.jar"]
