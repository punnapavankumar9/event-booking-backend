# Build stage
FROM ghcr.io/graalvm/graalvm-jdk:22.3.3 AS builder

# Install native-image
RUN gu install native-image

# Set working directory
WORKDIR /build

# Copy the entire project
COPY . .

# Build the application with native-image
RUN mvn -Pnative clean package -DskipTests -pl ${SERVICE_NAME} -am

# Runtime stage
FROM gcr.io/distroless/base-debian12

# Copy the native executable
COPY --from=builder /build/${SERVICE_NAME}/target/${SERVICE_NAME} /app

# Set the entrypoint
ENTRYPOINT ["/app"] 