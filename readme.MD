# Sample REST Service - Movie API

The Movie API is a sample REST service written in Java Spring Boot that provides basic CRUD (Create, Read, Update, Delete) functionality to manage a list of movie entities. This API allows users to interact with movie data through REST-based endpoints.

I created this project to provide a REST service I could use while experimenting with a number of technologies including Linux, Tomcat, AWS and SOAP UI. I also wanted to create a simple API that I could use as a reference for future projects.

The project uses the following technologies:
- Java 17
- Spring Boot 3.1.1
- Maven
- H2 Database

For a sample SOAP web service, see the [Sample SOAP Service - Movie API](https://www.github.com/zjcz/sample-soap-service) project.

## Setup

To get started, clone the repository and navigate to the project directory

```bash
git clone https://github.com/zjcz/sample-rest-service.git
cd sample-rest-service
```

Build the project using Maven

```bash
mvn clean install
```

Run the project using Maven

```bash 
mvn spring-boot:run
```

## Apache Tomcat
The project can produce a WAR file to be installed on Apache Tomcat using the following command:

```bash 
mvn clean package
```

This will create a sample-rest-service.war file in the ~/target directory that can be deployed to TomCat.

Note: As this is a Spring Boot 3.x project it will require Apache Tomcat 10.x or higher.

## Endpoints

### List All Movies

This endpoint allows you to retrieve a list of all movies currently stored in the database.

**Request:**
```
GET http://localhost:8080/movies
```

**Response:**
```json
[
  {
    "id": 1,
    "title": "Movie 1",
    "description": "Movie 1 Description",
    "releaseDate": "2023-01-01T01:10:10",
    "availableOnDvd": true
  },
  {
    "id": 2,
    "title": "Movie 2",
    "description": "Movie 2 Description",
    "releaseDate": "2023-12-31T23:59:59",
    "availableOnDvd": false
  }
]
```

### Get Single Movie

This endpoint allows you to retrieve a specific movie based on its ID.

**Request:**
```
GET http://localhost:8080/movies/1
```

**Response:**
```json
{
  "id": 1,
  "title": "Movie 1",
  "description": "Movie 1 Description",
  "releaseDate": "2023-01-01T01:10:10",
  "availableOnDvd": true
}
```

### Add New Movie

This endpoint allows you to add a new movie to the database.

**Request:**
```
POST http://localhost:8080/movies

{
  "title": "Movie 3",
  "description": "Movie 3 Description",
  "releaseDate": "2023-01-01T01:12:12",
  "availableOnDvd": true
}
```

**Response:**
```json
{
  "id": 3,
  "title": "Movie 3",
  "description": "Movie 3 Description",
  "releaseDate": "2023-01-01T01:12:12",
  "availableOnDvd": true
}
```

### Update Movie

This endpoint allows you to update an existing movie in the database.

**Request:**
```
PUT http://localhost:8080/movies/3

{
  "title": "Movie 3 - Update",
  "description": "Movie 3 Description - Update",
  "releaseDate": "2023-01-01T01:12:12",
  "availableOnDvd": true
}
```

**Response:**
```json
{
  "id": 3,
  "title": "Movie 3 - Update",
  "description": "Movie 3 Description - Update",
  "releaseDate": "2023-01-01T01:12:12",
  "availableOnDvd": true
}
```

### Delete Movie

This endpoint allows you to delete a movie from the database based on its ID.

**Request:**
```
DELETE http://localhost:8080/movies/3
```

**Response:**
```
empty response body
```

## How to Use

To use the Movie REST API, you can use any REST client tool or library that supports sending REST requests. By default the API expects JSON-based REST requests and responds with JSON-based REST responses, as demonstrated in the examples above.  To use XML, simply set the "content-type" and "accept" HTTP headers to "application/xml".

Here's a summary of the steps to interact with the API:

1. Send a HTTP request of the corresponding HTTP type to the Url using the appropriate JSON structure.
2. Receive the JSON response, which will contain the requested movie data.

## Warning!!!

This project was not intended for production use and is not secure. It is intended as a lightweight application for testing and experimentation purposes only.

## ToDo

- Add authentication - Add different authentication methods (Basic, OAuth, etc.)

## References

This project is based on the following tutorials: 
- [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
- [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
