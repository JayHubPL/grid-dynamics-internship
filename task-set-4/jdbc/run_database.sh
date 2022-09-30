#!/bin/sh

# IMPORTANT!
# Colima must be running on MacOS; use 'colima run'

CONTAINER_NAME="postgres-db"
POSTGRES_PASSWORD="docker"
POSTGRES_DB="users"
PORT=5432

# Stops and removes current database.
# If database isn't already running, docker will show
# errors trying to remove postgres-db, should be ignored
docker stop $CONTAINER_NAME
docker rm $CONTAINER_NAME

# Runs in the background a new database, latest image downloaded from docker.io
# user = postgres (default)
# password = docker (custom, $POSTGRES_PASSWORD)
# url = jdbc:postgresql://localhost:5432/users
docker run --name $CONTAINER_NAME -p $PORT:$PORT -e POSTGRES_DB=$POSTGRES_DB -e POSTGRES_PASSWORD=$POSTGRES_PASSWORD -d postgres