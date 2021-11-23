#!/bin/bash

set -e

echo "===== Creating common package ====="
cd ../common && ./mvnw package

echo "===== Installing common to the repo ====="
./mvnw install:install-file \
	-Dfile=./target/common-0.0.1-SNAPSHOT.jar \
	-DgroupId=dev.zentari \
	-DartifactId=common \
	-Dpackaging=jar \
	-Dversion=0.0.1-SNAPSHOT

echo "===== Creating chat-signal-service package ====="
cd ../chat-signal-service && ./mvnw package

echo "===== Creating file-service package ====="
cd ../file-service && ./mvnw package

echo "===== Creating discovery-server package ====="
cd ../discovery-server && ./mvnw package

echo "===== Creating discovery-api package ====="
cd ../discovery-api && ./mvnw package
