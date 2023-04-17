FROM openjdk:8
ADD target/*.jar app.jar
ENTRYPOINT ["java", "-jar","app.jar"]
EXPOSE 8080