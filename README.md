# GitHub Repository API 

This project provides a RESTful API that interacts with the GitHub API to list repositories and their branches for a given user. It's designed to fulfill various requirements related to GitHub repository information retrieval.

## Technologies Used

- Java 17
- Spring Boot 3
- WebClient & WebTestClient
- WireMock
- MapStruct
- Lombok
- JUnit
- Jacoco

## Requirements

- Java (version 17)
- Docker

## Installation

1. Clone the repository: `git clone https://github.com/maciej-MKan/GitHub-Repositories-API`
2. Navigate to the project directory: `cd GitHub-Repositories-API`
3. Build the project: `./gradlew build`

## Usage

To run the application:

1. Open a terminal and navigate to the project directory.
2. Start the application: `docker-compose up`

## API Documentation

This endpoint allows you to retrieve a list of repositories owned by a GitHub user and the associated branches for each repository.

 - URL: /api
 - Port: 8089
 - Method: POST

### Request Body:

```json
{
    "login": "github_username"
}
```
### Response:

 - Status: 200 OK
 - Content-Type: application/json
```json
{
    "login": "github_username",
    "repositories": [
        {
            "name": "repository_name",
            "branches": [
                {
                    "name": "branch_name",
                    "sha": "commit_sha"
                }
            ]
        } 
    ]
}
```
### Error Responses:

User Not Found:

 - Status: 404 Not Found
 - Content-Type: application/json
```json
{
    "status": 404,
    "Message": "Not found"
}
```
### Unsupported Media Type:

 - Status: 406 Not Acceptable 
 - Content-Type: application/json
```json
{
    "status": 406,
    "Message": "Requested media type is not supported"
}
```
### Example Usage:
```bash
curl -X POST -H "Content-Type: application/json" -d '{"login": "github_username"}' http://localhost:8089/api
```
```bash
curl -X POST -H "Content-Type: application/json" -H "Accept: application/xml" -d '{"login": "github_username"}' http://localhost:8089/api
```
