FROM maven:3.9-eclipse-temurin-20 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:20-jre
WORKDIR /app
COPY --from=build /app/target/nosql-spark-lab-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
