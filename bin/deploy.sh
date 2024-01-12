#!/bin/bash

# Set the environments
export JAVA_HOME=~/.jdks/corretto-17.0.8.1

# Navigate to the parent directory of the script.
cd "$(dirname "$0")/.." || exit

# Clean-up for starting new service
./bin/cleanup.sh

# Build the project using Maven.
./mvnw clean install -DskipTests=true

# Build a Docker image
docker build --tag springbootproject:v1 .

# Run a Docker container
docker run -d --name springbootproject --env-file .env -p 8080:8080 springbootproject:v1
