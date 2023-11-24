# Use a imagem Maven oficial para construir o projeto
FROM maven:3.8.4-openjdk-11-slim AS build

WORKDIR /app

COPY . .

# Build do projeto
RUN mvn clean package

# Imagem final com o JRE e o artefato construído
FROM openjdk:11-jre-slim

WORKDIR /app

COPY --from=build /app/target/spring-demo-*.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
