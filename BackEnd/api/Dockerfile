# =======================================================
# ESTÁGIO 1: O "BUILDER" - Constrói a nossa aplicação
# =======================================================
# Usa a imagem que você solicitou, com JDK 21 e Maven.
# 'AS builder' dá um nome a este estágio para que possamos nos referir a ele mais tarde.
FROM maven:3.9-eclipse-temurin-21 AS builder

# Define o diretório de trabalho dentro do container.
WORKDIR /app

# Otimização: Copia e baixa as dependências primeiro.
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

# Copia o restante do código-fonte.
COPY src ./src

# O comando mais importante: compila, testa (opcionalmente) e empacota a aplicação
# em um arquivo .jar. A flag -DskipTests acelera o build.
RUN ./mvnw package -DskipTests

# =======================================================
# ESTÁGIO 2: O "FINAL" - Cria a imagem final e leve
# =======================================================
# Começamos com uma imagem base leve que só tem o Java Runtime Environment (JRE).
FROM eclipse-temurin:21-jre-jammy

# Define o diretório de trabalho.
WORKDIR /app

# A mágica do multi-stage: Copia APENAS o arquivo .jar do estágio 'builder'
# para a nossa imagem final. Renomeamos para 'app.jar' para facilitar.
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta 8080 para que possamos nos conectar à nossa aplicação.
EXPOSE 8080

# O comando final para executar a aplicação quando o container iniciar.
CMD ["java", "-jar", "app.jar"]