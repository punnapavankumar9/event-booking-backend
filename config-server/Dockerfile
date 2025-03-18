ARG GRAALVM_VERSION
ARG SERVICE_NAME

FROM ghcr.io/graalvm/${GRAALVM_VERSION} AS builder

WORKDIR /build

COPY . .

RUN mvn -Pnative clean package -DskipTests -pl ${SERVICE_NAME} -am

FROM gcr.io/distroless/base-debian12

COPY --from=builder /build/${SERVICE_NAME}/target/${SERVICE_NAME} /app

ENTRYPOINT ["/app"] 