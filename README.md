# spring-base-project

The `spring-base-project` is a foundational Spring Boot project template designed to expedite the setup process for
various Spring Boot applications. It integrates essential functionalities and best practices, streamlining the initial
setup and configuration for new projects.

# For Development

## Configuring the HTTP Client for Personal Use:
#### 1. Create a Private Configuration File:
- Navigate to the `http` folder.
- Create a file named `http-client.private.env.json`.
#### 2. Personalize Your Environment:
- Mirror the structure from [http-client.env.json](http%2Fhttp-client.env.json) to the new file.
- Customize the values according to your personal settings.
#### 3. Multiple Environments (Optional):
- Configure additional environments if needed.
#### 4. Exclude from Version Control:
- Ensure `http-client.private.env.json` is added to the [.gitignore](.gitignore) file.
#### Remember:
- Safeguard personal information.
- Keep sensitive data out of version control.
- Verify the `.gitignore` file excludes private configurations.
