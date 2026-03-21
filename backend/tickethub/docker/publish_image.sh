#!/bin/bash

set -e


if [ $# -lt 2 ]; then
    echo "Uso: $0 usuario_de_DockerHub nombre_de_imagen"
    exit 1
fi

USER=$1
IMAGE_NAME=$2

FULL_IMAGE="$USER/$IMAGE_NAME:latest"
echo "Subiendo $FULL_IMAGE a DockerHub..."
docker tag "$IMAGE_NAME" "$FULL_IMAGE"
docker push "$FULL_IMAGE"

echo "¡Imagen publicada con éxito en: https://hub.docker.com/r/$FULL_IMAGE!"