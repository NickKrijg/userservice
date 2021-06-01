FROM openjdk:11
EXPOSE 8089
WORKDIR /app
COPY target/userservice-hoi.jar .

ENTRYPOINT ["java", "-jar", "userservice-hoi.jar"]
