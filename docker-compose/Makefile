TAG = "Makefile"

DOCKER = docker
DOCKER_HOST_IP := $(shell ipconfig getifaddr en0)

##
## Compose
##

.PHONY: compose.prepare
compose.prepare:
	@ echo "[$(TAG)] ($(shell TZ=UTC date -u '+%H:%M:%S')) - Preparing docker-compose"
	@ echo "-----------------------------------------\n"
	@ echo "export DOCKER_HOST_IP=$(DOCKER_HOST_IP)"
	@ echo "\n-----------------------------------------"
	@ echo ""

.PHONY: compose.mysql
compose.mysql: compose.prepare
	@ echo "[$(TAG)] ($(shell TZ=UTC date -u '+%H:%M:%S')) - Running docker-compose"
	@ docker stop $(docker ps -a -q) || true
	@ docker rm -f $(docker ps -a -q) || true
	@ docker volume rm $(docker volume ls -f dangling=true -q) || true
	@ docker-compose -f docker-compose.mysql.yml rm -fsv || true
	@ DOCKER_HOST_IP=$(DOCKER_HOST_IP) docker-compose \
		-f docker-compose.mysql.yml \
		up

.PHONY: compose.mysql.linux
compose.mysql.linux: compose.prepare
	@ echo "[$(TAG)] ($(shell TZ=UTC date -u '+%H:%M:%S')) - Running docker-compose"
	@ docker stop $(docker ps -a -q) || true
	@ docker rm -f $(docker ps -a -q) || true
	@ docker volume rm $(docker volume ls -f dangling=true -q) || true
	@ docker-compose -f docker-compose.mysql.linux.yml rm -fsv || true
	@ DOCKER_HOST_IP=$(DOCKER_HOST_IP) docker-compose \
		-f docker-compose.mysql.linux.yml \
		up

.PHONY: compose.mysql.linux.daemon
compose.mysql.linux.daemon: compose.prepare
	@ echo "[$(TAG)] ($(shell TZ=UTC date -u '+%H:%M:%S')) - Running docker-compose"
	@ docker stop $(docker ps -a -q) || true
	@ docker rm -f $(docker ps -a -q) || true
	@ docker volume rm $(docker volume ls -f dangling=true -q) || true
	@ docker-compose -f docker-compose.mysql.linux.yml rm -fsv || true
	@ DOCKER_HOST_IP=$(DOCKER_HOST_IP) docker-compose \
		-f docker-compose.mysql.linux.yml \
		up -d

.PHONY: compose.redis
compose.redis: compose.prepare
	@ echo "[$(TAG)] ($(shell TZ=UTC date -u '+%H:%M:%S')) - Running docker-compose"
	@ docker stop $(docker ps -a -q) || true
	@ docker rm -f $(docker ps -a -q) || true
	@ docker volume rm $(docker volume ls -f dangling=true -q) || true
	@ docker-compose -f docker-compose.redis.yml rm -fsv || true
	@ DOCKER_HOST_IP=$(DOCKER_HOST_IP) docker-compose \
		-f docker-compose.redis.yml \
		up


.PHONY: compose.clean
compose.clean:
	@ echo "[$(TAG)] ($(shell TZ=UTC date -u '+%H:%M:%S')) - Starting: Cleaning docker resources"
	@ echo "-----------------------------------------\n"
	@ docker stop `docker ps -a -q` || true
	@ docker rm -f `docker ps -a -q` || true
	@ docker rmi -f `docker images --quiet --filter "dangling=true"` || true
	@ docker volume rm `docker volume ls -f dangling=true -q` || true
	@ rm -rf ./docker-volumes
	@ docker network rm `docker network ls -q` || true
	@ echo ""
	@ rm -rf metastore_db
	@ echo "\n-----------------------------------------"
	@ echo "[$(TAG)] ($(shell TZ=UTC date -u '+%H:%M:%S')) - Finished: Cleaning docker resources"

