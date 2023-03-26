FROM openjdk:11

WORKDIR /app

COPY ./target/fp-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["/usr/bin/java", "-jar", "-Dspring.profiles.active=prod", "/fp-0.0.1-SNAPSHOT.jar"]