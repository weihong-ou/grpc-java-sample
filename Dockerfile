FROM openjdk:12-jdk-alpine
COPY build/libs/server-1.0-SNAPSHOT.jar /app/server.jar
CMD ["java", "-jar", "/app/server.jar"]