# Imagem base para build
FROM maven:3.8.4-openjdk-11 as build
COPY . /app
WORKDIR /app
RUN mvn clean package -Dcheckstyle.skip=true -Dmaven.compiler.source=11 -Dmaven.compiler.target=11

# Imagem final com o JRE e o artefato construído
FROM adoptopenjdk:12-jre-hotspot
COPY --from=build /app/target/spring-demo-*.jar /app/app.jar
WORKDIR /app
CMD ["java", "-jar", "app.jar"]
