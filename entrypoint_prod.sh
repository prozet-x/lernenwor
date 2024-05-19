#!/bin/sh

# Чтение секретов из файлов, путь к которым указан в переменных окружения
export POSTGRES_USER=$(cat "$POSTGRES_USER_FILE")
export POSTGRES_PASSWORD=$(cat "$POSTGRES_PASSWORD_FILE")

# Запуск Spring Boot приложения
exec java -jar /app/app.jar --spring.profiles.active=prod