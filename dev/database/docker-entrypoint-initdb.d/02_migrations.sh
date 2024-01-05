#!/bin/bash
echo "==================== Run migration database ===================="
seed_data_folder="/seed-data"
for file in "$seed_data_folder"/*; do
    if [ -f "$file" ]; then
        echo "=> Executing file: $file"
        # shellcheck disable=SC2002
        cat "$file" | psql -U sbp -d sbp_database
    fi
done
