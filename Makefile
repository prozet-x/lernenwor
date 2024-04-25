runenvironment-dev:
	docker compose -f docker-compose-work-environment.yml up

stop-environment-dev:
	docker compose -f docker-compose-work-environment.yml down

run-dev:
	gradle bootRun -Pprod=false -Pspring.profiles.active=dev

show-5342-listener:
	sudo lsof -nP -iTCP:5432 -sTCP:LISTEN