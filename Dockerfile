# ---- Etapa 1: Build ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copiar primero los archivos de Maven para aprovechar la caché de capas.
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copiar el código de fuente del backend y compilar.
COPY src/ src/
RUN ./mvnw clean package -DskipTests

# ---- Etapa 2: Runtime ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]