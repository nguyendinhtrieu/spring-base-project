#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE sbp_database;
    GRANT ALL PRIVILEGES ON DATABASE sbp_database TO sbp;
EOSQL
