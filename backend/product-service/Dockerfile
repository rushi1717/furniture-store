FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENV JAVA_TOOL_OPTIONS="-Xms128m -Xmx256m"

ENTRYPOINT ["java","-jar","app.jar"]
