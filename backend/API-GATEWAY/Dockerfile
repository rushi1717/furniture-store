# -------- BUILD --------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# -------- RUN --------
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENV JAVA_TOOL_OPTIONS="-Xms128m -Xmx256m"

ENTRYPOINT ["java","-jar","app.jar"]
