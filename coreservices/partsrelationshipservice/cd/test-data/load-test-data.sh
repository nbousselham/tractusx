#!/bin/bash

set -euo pipefail

echo "PostgreSQL host: $POSTGRES_HOST"
echo "PostgreSQL database: $POSTGRES_DB"
echo "PostgreSQL user: $POSTGRES_USER"

shutdown_docker_compose() {
    echo "Cleaning up..."
    docker-compose -f $tdm_dock down  # in case a previous run failed in the middle
}

shutdown_on_error() {
    shutdown_docker_compose
    exit 1
}

trap shutdown_on_error INT TERM ERR

./generate-test-data.sh > $sql_data_file
psql -v ON_ERROR_STOP=1 "host=$POSTGRES_HOST dbname=$POSTGRES_DB user=$POSTGRES_USER password=$POSTGRES_PASSWORD" -f $sql_data_file

shutdown_docker_compose
