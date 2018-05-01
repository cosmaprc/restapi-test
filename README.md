# ht-rest-api

### Tech

* [Spring Boot](https://projects.spring.io/spring-boot/) -  Production-ready Spring applications

Source code available on GitHub [public repository](https://github.com/cosmaprc/ht-rest-api).
Self-contained runnable release or pre-release jars available on GitHub [public repository releases](https://github.com/cosmaprc/ht-rest-api/releases). For how to run a release or pre-release jar, see the [Building and running a self-contained jar](https://github.com/cosmaprc/ht-rest-api#building-and-running-a-self-contained-jar) section.

### Running with maven

Requires [Apache Maven](https://maven.apache.org/) to run.

Available spring profiles to run:

* dev - in memory h2 db - configuration in src/main/resources/application-dev.yml
* staging - standalone MySQL db - configuration in src/main/resources/application-staging.yml - The schema will have to be created manually but the tables will be created by hibernate using "ddl-auto: update" on first run

Run with the default spring profile:

* Default profile is dev - set default profile in src/main/application.yml

```sh
$ cd ht-rest-api 
$ mvn spring-boot:run
```

Run with a chosen spring profile and overrided application properties:

```sh
$ cd ht-rest-api 
$ mvn spring-boot:run -Drun.profiles=staging -Drun.arguments="--server.port=${server.port},--spring.datasource.url=${spring.datasource.url},--spring.datasource.username=${spring.datasource.username},--spring.datasource.password=${spring.datasource.password}"
```

Example with app defaults:

>  mvn spring-boot:run -Drun.profiles=staging -Drun.arguments="--server.port=9000,--spring.datasource.url=jdbc:mysql://127.0.0.1:3306/testco_tech_db,--spring.datasource.username=htadmin,--spring.datasource.password=admin@ht"


### Building and running a self-contained jar

Requires [Apache Maven](https://maven.apache.org/) to build.

Building:

```sh
$ cd ht-rest-api 
$ mvn package spring-boot:repackage
```

Running with the default spring profile:

```sh
$ cd ht-rest-api 
$ cd target
$ java -jar restapi-0.0.1-SNAPSHOT.jar
```

Run with a chosen spring profile and overrided application properties:

```sh
$ cd ht-rest-api 
$ cd target
$ java -jar restapi-0.0.1-SNAPSHOT.jar --spring.profiles.active=staging --server.port=${server.port} --spring.datasource.url=${spring.datasource.url} --spring.datasource.username=${spring.datasource.username} --spring.datasource.password=${spring.datasource.password}
```

Example with app defaults: 

> java -jar restapi-0.0.1-SNAPSHOT.jar --spring.profiles.active=staging --server.port=9000 --spring.datasource.url=jdbc:mysql://127.0.0.1:3306/testco_tech_db --spring.datasource.username=htadmin --spring.dasource.password=admin@ht



### Swagger UI

* http://localhost:9000/restapi/swagger-ui.html 

### Running unit tests

```sh
$ cd ht-rest-api 
$ mvn test -P test
```

### Running integration tests

```sh
$ cd ht-rest-api 
$ mvn integration-test -P integration
```

### Development

Follow the steps in this article to setup lombok in eclipse/sts: [Setup Lombok in STS](http://codeomitted.com/setup-lombok-with-stseclipse-based-ide/)