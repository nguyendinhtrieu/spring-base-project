#!/bin/bash
echo "==================== Running migration database ===================="
seed_data_folder="/seed-data"
for file in "$seed_data_folder"/*; do
    if [ -f "$file" ]; then
        echo "=> Executing file: $file"
        # shellcheck disable=SC2002
        mysql -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE" < "$file"
    fi
done
