# ─────────────────────────────────────────────────────────────────────────────
# Stage 1 — Build
# Uses Maven + JDK 21 to compile and package the fat JAR.
# ─────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

# Copy dependency descriptors first (leverages Docker layer caching —
# dependencies are only re-downloaded when pom.xml changes).
COPY pom.xml .

# Download all dependencies in offline mode
RUN apk add --no-cache maven && \
    mvn dependency:go-offline --no-transfer-progress

# Copy source code and package the application (skip tests in build stage)
COPY src ./src
RUN mvn package -DskipTests --no-transfer-progress

# ─────────────────────────────────────────────────────────────────────────────
# Stage 2 — Runtime
# Slim JRE-only image for the final runnable container.
# ─────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine AS runtime

# Create a non-root user for security best practices
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app

# Copy only the packaged JAR from the build stage
COPY --from=builder /build/target/employee-service-1.0.0.jar app.jar

# Application runs on port 3000 (configured in application.properties)
EXPOSE 3000

# Use exec form to receive OS signals properly (e.g. graceful shutdown)
ENTRYPOINT ["java", "-jar", "app.jar"]
