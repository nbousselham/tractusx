#!/bin/sh
DOCKER_BUILDKIT=1
docker-compose up --exit-code-from=integration-test --abort-on-container-exit
