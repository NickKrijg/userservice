FROM maven:3.8.1-jdk-11 as maven
WORKDIR /app
COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B
COPY ./src ./src

RUN mvn package && cp target/userservice-init.jar app.jar

# Rely on Docker's multi-stage build to get a smaller image based on JRE
FROM openjdk:11-jre
WORKDIR /app
COPY --from=maven /app/app.jar ./app.jar

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
