FROM ghcr.io/graalvm/graalvm-jdk:22.3.3 AS builder

RUN gu install native-image

WORKDIR /build

COPY . .

RUN mvn -Pnative clean package -DskipTests -pl ${SERVICE_NAME} -am

FROM gcr.io/distroless/base-debian12

COPY --from=builder /build/${SERVICE_NAME}/target/${SERVICE_NAME} /app

ENTRYPOINT ["/app"] 