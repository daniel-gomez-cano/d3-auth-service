# ============================================================
# Dockerfile — d3-auth-service (Spring Boot + Keycloak client)
# Java 21 · Spring Boot 3.5.x
# NOTA: Keycloak corre en su propio contenedor (ver docker-compose).
#       Este servicio es el backend Spring que expone /auth/login
#       y se comunica con Keycloak como cliente OAuth2.
# ============================================================


FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn -q dependency:go-offline

COPY src ./src
RUN mvn -q -DskipTests package


FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*.jar /app/app.jar

# Puerto del auth-service (Spring Boot)
EXPOSE 8081

ENV JAVA_OPTS="-Xms128m -Xmx256m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
