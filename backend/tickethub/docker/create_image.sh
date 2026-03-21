#!/bin/bash


if [ -z "$1" ]; then
    echo "Error: Debes indicar el nombre de la imagen."
    echo "Uso: ./docker/create_image.sh nombre-de-tu-imagen"
    exit 1
fi

IMAGE_NAME=$1

echo "Construyendo la imagen: $IMAGE_NAME..."

export DOCKER_BUILDKIT=1

docker build -t "$IMAGE_NAME" -f docker/Dockerfile .

if [ $? -eq 0 ]; then
    echo "¡Imagen $IMAGE_NAME creada con éxito!"
else
    echo "Error: Hubo un fallo al construir la imagen."
    exit 1
fi