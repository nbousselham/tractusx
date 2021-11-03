#!/bin/sh
docker-compose up --exit-code-from=integration-test --abort-on-container-exit
