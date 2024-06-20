# Mini Banking App

This application is a simple banking system. You can create accounts, update account details and perform transactions. The application features a RESTful API backend using Spring Boot and PostgreSQL as the database. The frontend is built using React.

## Features
- Account creation
- Funds transfers between accounts
- Account transaction history retrieval
- Secure authentication system and user-specific operations

# Project setup

## Prerequisites
- Ensure you have Node.js installed (version 18.x or later)
- Also ensure you have npm (Node Package Manager) installed (comes with Node.js)
- Make sure that you have Java JDK 17 or later installed
- PostgreSQL (version 9.x or later) should be set up and running
- Apache Maven must be installed 

## Installation

First, clone the repo: https://github.com/BerkantDuman/mini-banking-app.git

### Backend Setup

1. Navigate to the backend directory

    ``` bash
    cd app

2. set the database credentials:
    ``` bash
    spring.datasource.url=jdbc:postgresql://localhost:5433/{YOUR_DATABASE_NAME}
    spring.datasource.username={YOUR_DATABASE_USERNAME}
    spring.datasource.password={YOUR_DATABASE_PASSWORD}

3. Build the project
    ``` bash
    mvn clean install

4. And run the project
    ```
    mvn spring-boot:run


### Frontend Setup

1. Navigate to the frontend directory

    ```bash
    cd ../mini-banking-ui

2. Install the necessary packages
    ```bash
    npm install

3. Run the project
    ``` bash
    npm start


## API Reference

For Swagger: http://localhost:8080/swagger-ui/index.html 

For Api Docs: http://localhost:8080/v3/api-docs



## Application Images

![image](./github/assets/Screenshot%202024-06-17%20at%2014.36.34.png)

![image](./github/assets/Screenshot%202024-06-17%20at%2014.36.59.png)

![image](./github/assets/Screenshot%202024-06-17%20at%2014.37.42.png)
