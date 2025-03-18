#!/bin/bash

# List of services
SERVICES=(
  "config-server"
  "discovery"
  "gateway"
  "event-core"
  "event-catalog"
  "identity"
  "order"
  "payment"
  "event-delay-engine"
  "notification"
)

# Copy Dockerfile to each service
for service in "${SERVICES[@]}"; do
  cp commons/Dockerfile "$service/Dockerfile"
  echo "Copied Dockerfile to $service"
done 