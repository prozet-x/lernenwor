re:
	docker compose -f docker-compose-work-environment.yml up

ce:
	docker compose -f docker-compose-work-environment.yml down

show-5342-listener:
	sudo lsof -nP -iTCP:5432 -sTCP:LISTEN