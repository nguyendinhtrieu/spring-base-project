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

## 4. Validate the Application

To validate the entire application, run the following command:

```shell
 ./mvnw clean validate
```

#### To validate specific tasks:

#### PMD Analysis

To generate `PMD` and `CPD` reports, use the following command:

```shell
./mvnw pmd:pmd pmd:cpd
```

## 5. Setting Up AWS Service Environment

For local development, we utilize `localstack` as the AWS service environment. The `localstack` service is configured in
the [docker-compose.yml](docker-compose.yml) file.

### Configuring `localstack` for AWS CLI

To use AWS CLI with `localstack` as the default profile, follow these steps:

#### 1. Start `localstack` Service:

- Ensure `localstack` is up and running by starting the service.

#### 2. Install the AWS CLI:

- Install the AWS CLI on your local machine.

#### 3. Create AWS Configuration File (`~/.aws/config`):

- Create a configuration file for the default profile at `~/.aws/config` with the following content:
    ```ini
    [default]
    region = ap-southeast-1
    endpoint_url = http://localhost:4566
    ```

#### 4. Create AWS Credentials File (`~/.aws/credentials`):

- Create a credentials file for the default profile at `~/.aws/credentials` with the following content (keys' values
  are not important for default AWS using localstack, so you can use any values you want):
    ```ini
    [default]
    aws_access_key_id = secret_key
    aws_secret_access_key = access_key
    ```

### Configuration Properties

The AWS service environment is specified using the `application.aws.serviceEnvironment` property in
the [application.yaml](src%2Fmain%2Fresources%2Fapplication.yaml) file. Possible values for this property are:

- `localstack`: Utilizes LocalStack for AWS services in a local development environment.
- `default`: Uses default AWS configurations.
- `credential`: Uses specific AWS credentials.
- `mock`: For mocking AWS S3 in unit testing.

### Environment Variables

There are additional environment variables related to AWS service settings:

- `APPLICATION_AWS_REGION`: Sets the AWS region for the application.
- `APPLICATION_AWS_ACCESS_KEY`, `APPLICATION_AWS_SECRET_KEY`: AWS access key and secret key for the application. You do
  not have to provide these environment variables if you are using the default AWS configuration for deployment, as AWS
  will handle them automatically. For localstack, you can provide any values since they are not required.
- `APPLICATION_AWS_BUCKET`: The AWS S3 bucket for the application.
