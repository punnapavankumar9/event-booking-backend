# Distributed Event Booking Platform

This repository contains the implementation of a **distributed microservice event booking platform**. The platform is designed to demonstrate modern architectural practices for building scalable, resilient, and event-driven systems using **Spring Boot** and associated technologies.

## Key Features

- **Event-Driven Architecture**: Leveraging Apache Kafka and Apache Pulsar for asynchronous communication between microservices.
- **API Gateway**: Utilizing Spring Cloud Gateway (Reactive) for centralized API management and routing.
- **Resilience Patterns**: Implementing Circuit Breaker to handle service failures gracefully.
- **Transaction Management**: Adopting the SAGA pattern to ensure data consistency across distributed services.
- **Stream Processing**: Using Spring Cloud Stream for building event-driven microservices.

## Technologies Used

- **Apache Kafka**: For message brokering and event streaming.
- **Apache Pulsar**: For delayed event queue (re-verification of abandoned transactions).
- **Spring Boot**: Framework for building microservices.
- **Spring Cloud Gateway (Reactive)**: For API Gateway and request routing.
- **Resilience4j**: For implementing Circuit Breaker pattern.
- **PostgreSQL, MongoDB**: For data storage.
- **MinIO**: For storing images and blobs.

## Goals

- **Scalability**: Ensure the platform can handle high loads of event bookings.
- **Resilience**: Make the system fault-tolerant and recover gracefully from failures.
- **Consistency**: Maintain data integrity in distributed transactions.

## Architectural Diagrams (WIP)

- High-level component design: [Event Booking Architecture](https://whimsical.com/event-booking-5LEHt3SuH1Gr9Q6srVBS8F)

## Running Locally

1. First, ensure these environment variables are set:

```shell
# For email notification
export GMAIL_APP_PASSWORD="xxxx xxxx xxxx xxxx"
export GMAIL_ID=yourmail@mail.com

# Razorpay secret key (for payment integration)
export RAZOR_PAY_SECRET=xxxxxxxxxxxxxxxxxxxxxxxx
export RAZOR_PAY_ID=rzp_test_xxxxxxxxxxxxxxxxxxxxxxxx

# Google OAuth2 integration
export GOOGLE_OAUTH2_CLIENT_ID=xxxxx-xxxxxxxxxxxxxxxxxxxxxxxx.apps.googleusercontent.com
export GOOGLE_OAUTH2_CLIENT_SECRET=xxxxxxxx

# GitHub OAuth2 integration
export GITHUB_OAUTH2_CLIENT_ID=xxxxxxxxxx
export GITHUB_OAUTH2_CLIENT_SECRET=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

# For storing movie images (create an access key at localhost:9001)
export MINIO_ACCESS_KEY=xxxxxxx
export MINIO_SECRET_KEY=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

2. Use docker-compose.yml to start dependencies (databases, Kafka, Pulsar, MinIO):
```shell
$ docker compose up -d
```

3. Start the services in the following order:
  - config-server
  - discovery-server
  - remaining services (except gateway)
  - gateway

## Frontend

- Here is the link to the Angular [frontend](https://github.com/punnapavankumar9/event-booking-frontend)
- Steps to start the frontend:
  ```shell
  npm i
  ng serve
  ```
  The server will be available at `http://localhost:4200`