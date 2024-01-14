# spring-base-project

The `spring-base-project` is a foundational Spring Boot project template designed to expedite the setup process for
various Spring Boot applications. It integrates essential functionalities and best practices, streamlining the initial
setup and configuration for new projects.

# For Development

## 1. Configuring the HTTP Client for Personal Use:
#### Create a Private Configuration File:
- Navigate to the `http` folder.
- Create a file named `http-client.private.env.json`.
#### Personalize Your Environment:
- Mirror the structure from [http-client.env.json](http%2Fhttp-client.env.json) to the new file.
- Customize the values according to your personal settings.
#### Multiple Environments (Optional):
- Configure additional environments if needed.
#### Exclude from Version Control:
- Ensure `http-client.private.env.json` is added to the [.gitignore](.gitignore) file.
#### Remember:
- Safeguard personal information.
- Keep sensitive data out of version control.
- Verify the `.gitignore` file excludes private configurations.

## 2. Setting Up the database and services for Local Development
### To start the database
    docker-compose -f docker-compose.yml up --build -d
### To stop the database
    docker-compose down
### Resetting the Database (Caution: Data Loss)
If you need to completely reset the database, perform the following steps:
#### Step 1. Stop Docker Containers:
   ```
   docker-compose down
   ```
#### Step 2. Remove Database Data:
   ```
   sudo rm -rf dev/ignore/database/db-data
   ```
#### Note:
- Be cautious when using this command as it deletes all database data. This action is irreversible and will result in complete data loss.
- You can modify the migration database process by editing the `seed-data` mount configuration in the [docker-compose.yml](docker-compose.yml) file under `services.db.volumes`. Update the folder path or settings to use specific migration data or scripts.
- If you are using Flyway to migrate the database, you don't need to mount `docker-entrypoint-initdb.d` and `seed-data`
  folders to set up the initial database. Let Flyway handle it for you.

## 3. Important note for migration using Flyway

- The `MIGRATE_DATABASE` environment variable is a flag that specifies whether Flyway should migrate the database when
  you start the application.
- By default, the `MIGRATE_DATABASE` environment variable is set to `false`.
- If you add a new version to migrate the database inside the resources
  folder [`db.migration`](src%2Fmain%2Fresources%2Fdb%2Fmigration), you need to set `MIGRATE_DATABASE=true` otherwise,
  the application will fail to start.

#### Spotless check & apply
```shell
./mvnw spotless:check
./mvnw spotless:apply
```

Reference: https://help.eclipse.org/latest/index.jsp?topic=%2Forg.eclipse.jdt.doc.isv%2Freference%2Fapi%2Forg%2Feclipse%2Fjdt%2Fcore%2Fformatter%2FDefaultCodeFormatterConstants.html
