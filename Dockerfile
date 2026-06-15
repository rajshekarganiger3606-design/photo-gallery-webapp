# ── Stage 1: Build ──────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-11 AS builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

# ── Stage 2: Run ────────────────────────────────────────────────────────────
FROM tomcat:9.0-jdk11-temurin

# Remove default Tomcat webapps so app runs at root path "/"
RUN rm -rf /usr/local/tomcat/webapps/*

# Deploy the WAR as ROOT so it serves at "/" instead of "/photo-gallery-webapp/"
COPY --from=builder /app/target/photo-gallery-webapp.war /usr/local/tomcat/webapps/ROOT.war

# Expose Tomcat port
EXPOSE 8080

CMD ["catalina.sh", "run"]