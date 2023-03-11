# Overview
<a href="https://spring.io/" target="_blank">
  <img src="https://upload.wikimedia.org/wikipedia/commons/4/44/Spring_Framework_Logo_2018.svg" alt="springboot" height="50"/>
</a>
<p>A simple HTTP REST API built with <b>Spring Boot</b>. (learning Spring Boot)</p>

# Authentication
This project uses JSON Web Token (JWT) for authentication. To access protected endpoints, you need to include a JWT as a Bearer Token in the request header.

# Endpoints
BASE PATH PREFIX: **/api/v1/**

## Auth 
PATH PREFIX: **/auth**

| Method | URI template  | URI Param | Query Param                        | RequestBody                                     | Description         | Example                |
|--------|---------------|-----------|------------------------------------|-------------------------------------------------|---------------------|------------------------|
| GET    | /authenticate | -         | -                                  | [RegisterRequest](#registerrequest)             | Signin (get JWT)    | [Click](#authenticate) |
| POST   | /register | -         | -                                  | [AuthenticationRequest](#authenticationrequest) | Signup              | [Click](#register)     |
| GET    | /validate | -         | *token*: email confirmation token; | -                                               | Validate user email | [Click](#validate)     |

## User
PATH PREFIX: **/admin/users**

&ast; ROLE_ADMIN required

| Method | URI template           | URI Param                  | Query Param                            | RequestBody | Description                 | Example                 |
|--------|------------------------|----------------------------|----------------------------------------|-------------|-----------------------------|-------------------------|
| GET    |                        | -                          | *page*: page index; *size*: page size; | -           | Get all users               | [Click](#GetAllUsers)   |
| GET    | /{username}            | *username*: user username; | -                                      | -           | Get user by username        | [Click](#GetUser)       |
| DELETE | /{username}            | *username*: user username; | -                                      | -           | Delete user by username     | [Click](#DeleteUser)    |
| PUT    | /{username}/make-admin | *username*: user username; | -                                      | -           | Make user admin by username | [Click](#MakeUserAdmin) |


# RequestBody

## RegisterRequest
```json
{
  "firstname": "string",
  "lastname": "string",
  "username": "string",
  "email": "string",
  "password": "string"
}
```
## AuthenticationRequest
```json
{
  "username": "string",
  "password": "string"
}
```
# Response Example

## Authenticate

### Ex1: success
```json
{
  "token": "eyJhbG...AYKgrP8LBPwRdT9_MqVgBNxdXI"
}
```
### Ex2: error - invalid credentials
```json
{
  "status": "BAD_REQUEST",
  "message": "Invalid username or password."
}
```
### Ex3: error - bad request
```json
{
  "status": "BAD_REQUEST",
  "message": {
    "password": [
      "The field is required."
    ],
    "username": [
      "The field is required."
    ]
  }
}
```

## Register

### Ex1: success
```json
{
  "message": "User successfully registered. Please check your email to confirm your account."
}
```

### Ex2: error - email already taken
```json
{
  "status": "BAD_REQUEST",
  "message": "User EMAIL already taken."
}
```

### Ex3: error - missing required fields
```json
{
  "status": "BAD_REQUEST",
  "message": {
    "firstname": [
      "The field is required."
    ], 
    "lastname": [
      "The field is required."
    ]
  }
}
```

## GetAllUsers
### Ex1: success
```json
[
  {
    "username": "admin",
    "uri": "http://localhost:8080/api/v1/admin/users/admin"
  },
  {
    "username": "baba01",
    "uri": "http://localhost:8080/api/v1/admin/users/baba01"
  }
]
```

## GetUser
### Ex1: success
```json
{
  "firstname": "alh",
  "lastname": "baba",
  "username": "baba01",
  "email": "s.baba@gmail.com",
  "roles": [
    "ROLE_USER"
  ],
  "timestamp": "10-03-2023 22:30:28",
  "enabled": false
}
```

### Ex2: error - user not found
```json
{
  "status": "BAD_REQUEST",
  "message": "USER not found."
}
```
## DeleteUser
### Ex1: success
```json
{
  "message": "User with username <Baba01> has been successfully deleted."
}
```


## MakeUserAdmin

### Ex1: success
```json
{
  "message": "User with username <Baba01> is now admin."
}
```

# Features

- Maven
- Spring Boot (3.0.2)
- Java (17)
- MySQL

- - -

- JWT (with SignatureAlgorithm=HS256)
- Springdoc
- ModelMapper
- Lombok

- - -

- Controller-Service-Repository pattern
- MessageSource
- Pagination
- User email validation with token
- Custom validation messages
- Custom exception handler
- Event and EventListener
- Constructor injection
- Validation
- DTO