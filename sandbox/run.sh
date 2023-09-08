
docker network create elastic
docker network create kafka

# Criar os docker volumes
docker volume create es01
docker volume create kafka01
docker volume create kconnect01

docker-compose -f elk/docker-compose.yaml up -d elasticsearch
docker-compose -f kafka/docker-compose.yaml up -d
docker-compose -f services/docker-compose.yaml up -d
