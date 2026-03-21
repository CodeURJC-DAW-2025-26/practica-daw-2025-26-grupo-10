#!/bin/bash
set -e

if [ -z "$1" ]; then
    echo "Uso: $0 usuario_de_DockerHub"
    exit 1
fi

USER=$1
docker tag ${USER}/tickethub-app:latest ${USER}/tickethub-app:compose-ready
docker push ${USER}/tickethub-app:compose-ready

echo "¡docker-compose listo! Para ejecutar: DOCKER_USER=$USER docker compose up"