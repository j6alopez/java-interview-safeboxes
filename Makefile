.PHONY: down up test

down:
	docker compose down

up:
	docker compose run -d -p "8080:8080" java-skeleton-api gradle clean build bootRun -x test

test:
	docker compose run --rm --no-deps -p "8080:8080" java-skeleton-api gradle build test

coverage: test