# Pandemie-Inc

## Requirements
- Java (version >= 8)
- Maven

## Development

1) `mvn spring-boot:run`


## Building for Release

1) `mvn package`
2) `java -jar target/pandemieinc-0.0.1-SNAPSHOT.jar`

For cleaning: `mvn clean`


## Miscellaneous

If you encounter a problem with your installed maven version, you can use `./mvnw` or `./mvnw.cmd` instead
of `mvn`.


## Docker

We have a `Dockerfile` file for building an image that can be used to run the application on [Docker](https://www.docker.com).
- Build: `docker build -t tubs/pandemieinc .`
- Run: `docker run -p 8080:8080 tubs/pandemieinc`
