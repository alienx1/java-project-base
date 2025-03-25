#!/bin/bash

REGISTRY="ghcr.io/alienx1"
IMAGE_NAME="java-project-base"
TAG_DATETIME=$(date +%Y%m%d)
FULL_IMAGE_NAME="$REGISTRY/$IMAGE_NAME:$TAG_DATETIME"
DOCKERFILE_PATH="./docker/Dockerfile"

docker build --no-cache -t "$FULL_IMAGE_NAME" -f "$DOCKERFILE_PATH" .

# docker push "$FULL_IMAGE_NAME"
