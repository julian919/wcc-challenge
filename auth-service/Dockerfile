FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY . .
RUN --mount=type=cache,target=/root/.m2 mvn -q -pl auth-service -am package -DskipTests

FROM eclipse-temurin:21-jre
COPY --from=build /workspace/auth-service/target/*.jar /app.jar
EXPOSE 8100
ENTRYPOINT ["java", "-jar", "/app.jar"]
