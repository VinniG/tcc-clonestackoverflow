# Imagem base
FROM maven:3.8.4-openjdk-11

# Copiar o código-fonte para o diretório de trabalho
COPY . /app

# Mudar para o diretório do aplicativo
WORKDIR /app

# Configurar a versão do Java para 11
RUN update-alternatives --install /usr/bin/java java /usr/lib/jvm/java-11-openjdk-amd64/bin/java 1
RUN update-alternatives --set java /usr/lib/jvm/java-11-openjdk-amd64/bin/java

# Build do projeto
RUN mvn clean package -Dcheckstyle.skip=true

# Imagem final com o JRE e o artefato construído
FROM openjdk:11-jre-slim
COPY --from=0 /app/target/spring-demo-*.jar /app/app.jar
WORKDIR /app
CMD ["java", "-jar", "app.jar"]
