# Spring Boot 3 - JUnit 5 - Actuator  Demo

## Description

This is a demo project about unit test and integration test for Spring Boot Application.

Code: JTSP - 2 - W3D2

## Tech Stack

+ Spring Boot 3
+ Spring Data JPA
+ H2 DB
+ Spring Actuator
+ JUnit 5
+ Mockito
+ JaCoCo
+ Spring Profiles
+ SonarQube (Optional)

## Project Architecture
Controller --> Service --> Repository --> H2

Note: for dev and test env, both use H2 vendor but different databases.


## Note
1. `application-test.properties` is for test env configuration
2. Controller unit test should use `MockMvc` and mocked service layer.
3. Integration test should use `NockMvc` and mocked repository layer since repository access is out of springboot application scope.
4. Use `mvn clean install` or `mvn jacoco:report` to get the report.
5. Add config in `pom.xml` to exclude some package for unit test coverage.
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.7</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>prepare-package</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <excludes>
            <exclude>com/example/springboot3unittestactuator/entity/**</exclude>
            <exclude>com/example/springboot3unittestactuator/Exception/**</exclude>
        </excludes>
    </configuration>
</plugin>
```

## SonarQube (Optional)
Pull the SonarQube Docker image from the Docker Hub repository:

```bash
docker pull sonarqube:latest
```
Run the SonarQube container:
```bash
docker run -d --name sonarqube -p 9000:9000 sonarqube:latest

```

Add plugin in target project `pom.xml`
```xml
<plugin>
    <groupId>org.sonarsource.scanner.maven</groupId>
    <artifactId>sonar-maven-plugin</artifactId>
    <version>3.9.0.2155</version>
</plugin>

```

Access localhost:9000 to new project and set up project. Follow instructions and finally there will be a command
```
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=jstp-w3d2 \
  -Dsonar.projectName='jstp-w3d2' \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=sqp_dc889445377a673c558d41d8cda78196f28dc461
```

You will need token generated in SonarQube UI.

