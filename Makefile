run-environment-dev:
	docker compose -f docker-compose-dev.yml up

stop-environment-dev:
	docker compose -f docker-compose-dev.yml down

run-dev:
	./gradlew bootRun -Penv=dev

show-5342-listener:
	sudo lsof -nP -iTCP:5432 -sTCP:LISTEN