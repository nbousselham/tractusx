#!/bin/bash

set -euo pipefail

echo "PostgreSQL host: $POSTGRES_HOST"
echo "PostgreSQL database: $POSTGRES_DB"
echo "PostgreSQL user: $POSTGRES_USER"

cleanup() {
    echo "Cleaning up..."
    docker-compose -f $tdm_dock down  # in case a previous run failed in the middle
    exit 1
}

trap cleanup INT TERM ERR

sql_data_file=data.sql.tmp
tdm_dock=../../../../data/tdm/generator/src/main/docker/docker-compose.yml

export HOST_SECURE=false
export HOST_NAME=localhost
export HOST_PORT=8080
export API_KEY=SPEEDBOAT
docker-compose -f $tdm_dock up -d

echo "Waiting for TDM to start up..."
until curl -f http://localhost:8080 ; do sleep 1; done

./generate-test-data.sh > $sql_data_file
psql -v ON_ERROR_STOP=1 "host=$POSTGRES_HOST dbname=$POSTGRES_DB user=$POSTGRES_USER password=$POSTGRES_PASSWORD" -f $sql_data_file

cleanup
