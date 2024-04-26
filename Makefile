run-environment-dev:
	@docker compose -f docker-compose-dev.yml up

stop-environment-dev:
	@docker compose -f docker-compose-dev.yml down

run-dev:
	@./gradlew bootRun -Penv=dev

sr:
	@sudo lsof -i :5432 -sTCP:LISTEN -t || echo "No app listening to 5432 port"