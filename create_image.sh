#!/bin/bash
set -e

if [ -z "$1" ]; then
    echo "Error: Debes indicar el nombre de la imagen."
    echo "Uso: ./docker/create_image.sh nombre-de-tu-imagen"
    exit 1
fi

IMAGE_NAME=$1

echo "Construyendo la imagen: $IMAGE_NAME..."

docker build -t "$IMAGE_NAME" -f Dockerfile .

echo "¡Imagen $IMAGE_NAME creada con éxito!"