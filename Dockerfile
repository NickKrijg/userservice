FROM openjdk:11
COPY target/userservice-hoi.jar userservice-hoi.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "userservice-hoi.jar"]
