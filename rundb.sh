#!/usr/bin/env bash
set -euo pipefail

# Run a postgres docker container and initialize it with db/postgres-schema.sql
# Usage: ./rundb.sh [container_name] [host_port]

CONTAINER_NAME=${1:-todo-postgres}
HOST_PORT=${2:-5432}
USER=${DB_USER:-todo}
PASSWORD=${DB_PASSWORD:-todo}
DB=${DB_NAME:-todo}
IMAGE=${PG_IMAGE:-postgres:15}

SCRIPT_HOST_PATH="$(pwd)/db/postgres-schema.sql"

if ! command -v docker >/dev/null 2>&1; then
  echo "docker is required but not found in PATH"
  exit 1
fi

if [ ! -f "$SCRIPT_HOST_PATH" ]; then
  echo "Schema file not found: $SCRIPT_HOST_PATH"
  exit 1
fi

# Remove existing container if present
if docker ps -a --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
  echo "Removing existing container ${CONTAINER_NAME}"
  docker rm -f "${CONTAINER_NAME}"
fi

echo "Starting Postgres container ${CONTAINER_NAME} (image: ${IMAGE})"
docker run --name "${CONTAINER_NAME}" \
  -e POSTGRES_USER="${USER}" \
  -e POSTGRES_PASSWORD="${PASSWORD}" \
  -e POSTGRES_DB="${DB}" \
  -p "${HOST_PORT}":5432 \
  -v "$SCRIPT_HOST_PATH":/docker-entrypoint-initdb.d/init.sql:ro \
  -d "${IMAGE}"

# Wait for postgres to be ready
echo "Waiting for Postgres to become available..."
for i in $(seq 1 30); do
  if docker exec "${CONTAINER_NAME}" pg_isready -U "${USER}" >/dev/null 2>&1; then
    echo "Postgres is ready"
    break
  fi
  echo -n "."
  sleep 1
done

echo
printf "Postgres running. JDBC URL: jdbc:postgresql://localhost:%s/%s\nUser: %s\nPassword: %s\n" "$HOST_PORT" "$DB" "$USER" "$PASSWORD"

echo "You can stop the DB with: docker rm -f ${CONTAINER_NAME}"
