# Spring Boot Multi-Module Project

## Overview
This is a multi-module Spring Boot project structured for modular development, improving maintainability and scalability.

## Project Structure
```
project-root/
│-- application/      # Main application module
│-- domain/          # Business logic and domain models
│-- persistence/     # Data access layer (JPA, repositories, etc.)
│-- infrastructure/  # Configuration and utility components
│-- web/             # REST controllers and API endpoints
│-- docker/          # Docker configurations
│-- mvnw, mvnw.cmd   # Maven wrapper
│-- pom.xml          # Parent POM file managing dependencies and modules
│-- jvm.config       # JVM configurations
```

## Prerequisites
- Java 21
- Maven 3+

## Build and Run
### Build the project
```sh
./mvnw clean install -DskipTests
```

### Run the application on CMD
```sh
./mvnw spring-boot:run -pl web
```
### Run the application on visual studio code
[VS Code Java Debug Extension](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-debug)
config file .vscode/lanch.json
```json
{
    "configurations": [
        {
            "type": "java",
            "name": "Spring Boot-Application<web>",
            "request": "launch",
            "cwd": "${workspaceFolder}",
            "mainClass": "com.ss.web.Application",
            "projectName": "web",
            "javaExec": "path/to/java/bin/java"
        }
    ]
}
```




