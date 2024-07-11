FROM arm64v8/openjdk:17-ea-16-jdk
ARG JAR_FILE_PATH=build/libs/ranchat-api-1.0-SNAPSHOT.jar
COPY ${JAR_FILE_PATH} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
