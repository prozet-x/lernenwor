run-environment-dev:
	@bash run_dev_env.sh

stop-environment-dev:
	@docker compose -f docker-compose-dev.yml down

run-dev:
	@./gradlew bootRun -Penv=dev

show-5432-listener:
	@sudo lsof -i :5432 -sTCP:LISTEN -t || echo "No app is listening to 5432 port"