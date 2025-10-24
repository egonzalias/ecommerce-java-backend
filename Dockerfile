# Dockerfile
FROM eclipse-temurin:17-jdk-jammy

ENV JAVA_OPTS=""

WORKDIR /app

COPY target/ecommerce-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
