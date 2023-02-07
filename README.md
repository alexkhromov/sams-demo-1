This project was created as SaM-Solutions internship project.

## Project goal

The goal of this project is to create web application built with Spring Boot 2 and ReactJS, using REST API.
The project provides a place to store questions for Java interviews.
Questions can be filtered by interview level (e.g. Junior). Question repository supports CRUD queries.

## Technologies

- Java 8.191
- MySQL 8.0.13
- Spring Boot 2.2.2
- Tomcat 8.5
- NodeJS 10.16.3
- ReactJS 16.12

## Build

Project can be built using `mvn clean install`. 

**Note: if you have problems with building using bundled `npm` then try to run `npm run build` with your own `npm` 
from `/app` folder.**

## Deployment

### MySQL

Make sure you have an instance of MySQL8 running on `localhost:3306` (path could be changed in application.properties). <br />
You have to create database schema and populate it, you can do it by running SQL scripts from `/db_script` folder, e.g. `source path/to/db_script/schema.sql` and `source path/to/db_script/data.sql`.

### Tomcat

Make sure you have an instance of Tomcat8 (or compatible with tomcat7-maven-plugin) running on `localhost:8080` 
(could be changed in `pom.xml`). Provide valid credentials in Maven `settings.xml` for server `demoServer` and Tomcat 
user with `manager-gui,manager-script` roles. <br />

Deployment can be done by running `mvn tomcat7:redeploy` command or by placing built `demo.war` in your webapps folder.

### App

If everything was done correctly then you should see the app running on `localhost:8080/demo`.

## Planned features

1. Question title translation
2. Action logger
3. Unit testing
4. Question search