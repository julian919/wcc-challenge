FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY . .
RUN --mount=type=cache,target=/root/.m2 mvn -q -pl postcodes-service -am package -DskipTests

FROM eclipse-temurin:21-jre
COPY --from=build /workspace/postcodes-service/target/*.jar /app.jar
EXPOSE 8200
ENTRYPOINT ["java", "-jar", "/app.jar"]
