FROM adoptopenjdk:11-jre-hotspot
COPY "target/auth-service.jar" auth-service.jar
ENTRYPOINT ["java", "-jar", "auth-service.jar"]