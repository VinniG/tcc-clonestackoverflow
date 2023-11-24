# Imagem base para build
FROM maven:3.8.4-openjdk-11 as build
COPY . /app
WORKDIR /app

# Configurar a versão do Java para 11
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
RUN mvn clean package -Dcheckstyle.skip=true

# Imagem final com o JRE e o artefato construído
FROM adoptopenjdk:12-jre-hotspot
COPY --from=build /app/target/spring-demo-*.jar /app/app.jar
WORKDIR /app
CMD ["java", "-jar", "app.jar"]
