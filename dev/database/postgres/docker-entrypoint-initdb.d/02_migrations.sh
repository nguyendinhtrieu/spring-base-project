#!/bin/bash
echo "==================== Running migration database ===================="
seed_data_folder="/seed-data"
for file in "$seed_data_folder"/*; do
    if [ -f "$file" ]; then
        echo "=> Executing file: $file"
        # shellcheck disable=SC2002
        cat "$file" | psql -U "$POSTGRES_USER" -d "$POSTGRES_DB"
    fi
done
