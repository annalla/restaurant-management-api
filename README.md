# restaurant-management-api

The project allows users access to API for manage menu item and bill order in restaurant
## Description

RESTful API for CRUD operation (Create, Retrieve all and by id, Update and Delete) and searching menu by keyword that matches title, description
to manage menu in restaurant.

RESTful API for CRUD operation to manage bill order. Also, Can add or remove menu items and quantities in a specific bill.
## Getting Started

### Dependencies

* JDK 8 or later
* Download [maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)
* Spring framework
* Database: PostgresSQL

### Building

On your command line, you need to change directory where contains pom.xml file.

Build the project into jar file:

  ```sh
$ mvn package
  ```

### Executing program

Execute the project with file jar, make sure database connection is available:

  ```sh
 $ java -jar target/restaurant-management-api-1.0-SNAPSHOT.jar
  ```

## Authors

Dang Thi Hong Xuyen

## Version History

* 1.0
    * Initial Release

