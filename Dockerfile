# base img for java 17
FROM eclipse-temurin:17

# work dir
WORKDIR /app

# coppy files
COPY ./build/libs/*.jar app.jar

# run Spring Boot
CMD ["java", "-jar", "app.jar"]
