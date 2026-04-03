# ── Stage 1: Build the jar inside Docker ─────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Copy gradle files first (better layer caching)
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Download dependencies (cached if build.gradle didn't change)
RUN ./gradlew dependencies --no-daemon

# Copy source and build
COPY src src
RUN ./gradlew build -x test --no-daemon

# ── Stage 2: Run the jar ──────────────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"

COPY --from=builder /app/build/libs/demo-module-3-app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080 5005
ENTRYPOINT ["java", "-jar", "app.jar"]