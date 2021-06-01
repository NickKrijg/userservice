FROM openjdk:11
EXPOSE 8089
WORKDIR /app
COPY target/userservice-init.jar .

ENTRYPOINT ["java", "-jar", "userservice-init.jar"]


FROM maven:3.8.1-jdk-11 as maven
WORKDIR /app
COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B
COPY ./src ./src

RUN mvn package && cp target/userservice-hoi.jar app.jar

# Rely on Docker's multi-stage build to get a smaller image based on JRE
FROM openjdk:11
WORKDIR /app
COPY --from=maven /app/app.jar ./app.jar

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
