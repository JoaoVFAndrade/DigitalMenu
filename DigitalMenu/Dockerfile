# Etapa de build com Maven
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copia o pom.xml e baixa dependências primeiro (cache build)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o resto do código e compila
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa de runtime
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copia o JAR gerado na etapa anterior
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
