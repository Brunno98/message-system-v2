FROM maven:3.8.8-eclipse-temurin-17-alpine as build
WORKDIR /workspace/app
COPY pom.xml pom.xml
RUN mvn dependency:go-offline
COPY src src
RUN set -x; mvn clean install

FROM amazoncorretto:17.0.9-alpine3.18 as final
WORKDIR /app
COPY --from=build /workspace/app/target/*.jar /app/*.jar
ENTRYPOINT ["java", "-jar", "/app/*.jar" ]