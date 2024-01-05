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
