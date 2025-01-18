# Distributed Event Booking Platform(WIP)

This repository contains the implementation of a **distributed microservice event booking platform**. The platform is
designed to demonstrate modern architectural practices for building scalable, resilient, and event-driven systems using
**Spring Boot** and associated technologies.

## Key Features

- **Event-Driven Architecture**: Leveraging Apache Kafka for asynchronous communication between microservices.
- **API Gateway**: Utilizing Spring Cloud Gateway for centralized API management and routing.
- **Resilience Patterns**: Implementing Circuit Breaker to handle service failures gracefully.
- **Transaction Management**: Adopting the SAGA pattern to ensure data consistency across distributed services.
- **Stream Processing**: Using Spring Cloud Stream for building event-driven microservices.

## Technologies Used

- **Apache Kafka**: For message brokering and event streaming.
- **Spring Boot**: Framework for building microservices.
- **Spring Cloud Gateway**: For API Gateway and request routing.
- **Spring Cloud Stream**: For simplifying message-driven microservices.
- **Resilience4j**: For implementing Circuit Breaker pattern.

## Goals

- **Scalability**: Ensure the platform can handle high loads of event bookings.
- **Resilience**: Make the system fault-tolerant and recover gracefully from failures.
- **Consistency**: Maintain data integrity in distributed transactions.

## Architectural Diagrams(WIP)

- High level component design https://whimsical.com/event-booking-5LEHt3SuH1Gr9Q6srVBS8F