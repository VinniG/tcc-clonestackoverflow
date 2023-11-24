# Imagem base para build
FROM maven:3.8.4-openjdk-12 as build
COPY . /app
WORKDIR /app
RUN mvn clean package -Dcheckstyle.skip=true

# Imagem final com o JRE e o artefato construído
FROM adoptopenjdk:12-jre-hotspot
COPY --from=build /app/target/spring-demo-*.jar /app/app.jar
WORKDIR /app
CMD ["java", "-jar", "app.jar"]
