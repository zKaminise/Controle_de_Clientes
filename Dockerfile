# Etapa de build
FROM ubuntu:latest AS build

# Atualiza os pacotes e instala dependências necessárias
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    maven

# Define o diretório de trabalho
WORKDIR /app

# Copia o código-fonte para o contêiner
COPY . .

# Executa o build do projeto
RUN mvn clean install -DskipTests

# Etapa de runtime
FROM openjdk:17-jdk-slim

# Instala bibliotecas necessárias para JasperReports
RUN apt-get update && apt-get install -y \
    libfreetype6 \
    libfontconfig1 \
    && rm -rf /var/lib/apt/lists/*

# Expõe a porta do servidor
EXPOSE 8080

# Copia o artefato gerado para o contêiner
COPY --from=build /app/target/PsicoManagerProject-0.0.1-SNAPSHOT.jar /app/app.jar

# Define o ponto de entrada
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
