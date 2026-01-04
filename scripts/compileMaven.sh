#!/bin/bash
./mvnw flyway:migrate
./mvnw clean compile

./mvnw package -DskipTests