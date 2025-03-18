ARG GRAALVM_VERSION=native-image-community:21
ARG SERVICE_NAME=notification

FROM ghcr.io/graalvm/${GRAALVM_VERSION} AS builder

WORKDIR /build

COPY . .

RUN chmod +x ./mvnw

RUN ./mvnw -Pnative clean package -DskipTests -pl ${SERVICE_NAME} -am

FROM gcr.io/distroless/base-debian12

COPY --from=builder /build/${SERVICE_NAME}/target/${SERVICE_NAME} /app

ENTRYPOINT ["/app"] 