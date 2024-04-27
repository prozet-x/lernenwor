#!/bin/bash

# Определяем IP-адрес
IP=$(hostname -I | awk '{print $1}')

# Экспортируем переменную окружения
export REACT_APP_BACKEND_URL="http://${IP}:8080"

# Запускаем Docker Compose
docker compose -f docker-compose-dev.yml up