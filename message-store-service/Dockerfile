FROM amazoncorretto:17.0.9-alpine3.18 as build
WORKDIR /workspace/app
COPY mvnw mvnw
# clean up mvnw file from Windows modification
RUN sed -i 's/\r$//' mvnw
COPY .mvn .mvn
COPY pom.xml pom.xml
RUN ./mvnw dependency:go-offline
COPY src src
RUN ./mvnw clean install

FROM amazoncorretto:17.0.9-alpine3.18 as final
WORKDIR /app
COPY --from=build /workspace/app/target/*.jar /app/*.jar
ENTRYPOINT ["java", "-jar", "/app/*.jar" ]