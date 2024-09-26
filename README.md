# Voluntary IDATT-2105-Fullstack project

# Table of contents
1. [Introduction](#introduction)
2. [Documentation](#documentation)
3. [IDE for frontend](#frontendIDE)
4. [IDE for backend](#backendIDE)
5. [Installation](#installation)
6. [Further development](#Further_development)
7. [Documentation](#documentation)
8. [Reflection](#reflection)

## Introduction
This is a project made by Eilert W. Hansen and Brage H. Kvamme. For our project in IDATT2105 we hade to make a website for selling items. The entire project was made in 9 days. We decided to make "Handel Uten Hemninger", a web store for selling items, much like finn.no. The only difference is that you can sell ANYTHING on this website. Have fun!

#### Task at hand
The product is an e-commerce marketplace like web application where sellers can list their items and
potential buyers can browse the items using various filter mechanisms, can add items into their favorite
lists, and can check out and buy items!

#### Technologies used
* Vue 3 with TypeScript
* Spring Boot
* JPA
* Java
* Maven
* MySQL
* Swagger

## Documentation

To access the Swagger documentation, setting up the backend is necessary. Documentation on how to do that is here: [Setting up backend]()
After starting the backend API, click here: [Swagger](http://192.168.86.40:8080/swagger-ui/index.html#/)

JavaDoc: 

## Recommended IDE Setup for frontend <a name="frontendIDE"></a>

[VSCode](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (and disable Vetur) + [TypeScript Vue Plugin (Volar)](https://marketplace.visualstudio.com/items?itemName=Vue.vscode-typescript-vue-plugin).

## Reccomended IDE Setup for backend <a name="backendIDE"></a>

[Intellij IDEA](https://www.jetbrains.com/idea/).

## Installation and Project Setup <a name="installation"></a>

First clone the project with:
```sh
git clone git@gitlab.stud.idi.ntnu.no:bragehk/NettProggProsjekt.git
```

### Database setup

[Docker](https://docs.docker.com/get-docker/) is required to run the database. After installing docker, run these commands from the starting folder:

```sh
cd MySQL-Server
docker build -t mysql-image .
docker run --name mysql-container -p 3306:3306 -d mysql-image
```

The docker container should now be visible in Docker Desktop. The `MySQL-Server/scrpts/` folder contains database dummy data to make testing easier. Delete this folder if you don't want dummy data.

### Backend setup

Before starting the backend, make sure the database is properly set up.
[Maven](https://maven.apache.org/download.cgi) is required for the backendd to start.

```sh
cd Backend
mvn spring-boot:run
```

### Frontend setup

```sh
npm install
```

### Compile and Hot-Reload for Development

```sh
npm run dev
```

### Type-Check, Compile and Minify for Production

```sh
npm run build
```

### Run Unit Tests with [Vitest](https://vitest.dev/)

```sh
npm run test:unit
```

### Run End-to-End Tests with [Cypress](https://www.cypress.io/)

```sh
npm run test:e2e:dev
```


## Further development <a name="Further_development"></a>

As we were only 2 students working on this project, many more features could have been implemented and improved upon. Here are some of the features that could be implemented in the future:

### Refresh tokens

Right now the project uses Access Tokens with JWT. The token is valid for 60 minutes. After the 60 minutes, the user has to log in again. With a refresh token system, the user could refresh their access token and not have to log in again.

### More testing

When creating tests for the backend, it was hard to set up a testing database, as the project is using a MySQL database running in a docker container. With more time, we could have figured out how to set up the security config and database properly to make more backend tests. Right now there are a lot of half-finished test that does not work. These tests needs to be changed.

In the frontend ......

### Proper messaging system

There was not enough time to implement a proper messaging system. The backend has the database structure for messages and conversations between seller and buyer, but service classes and a controller class is necessary. Right now a buyer can get the contact information of a seller and contact them on either email or phone. When creating a messaging system, using websockets is recommended.

### Improved CI/CD

We have a .gitlab-ci.yml file for making sure the frontend is building correct, and that the test run. Because we didn't have enough time, no useful tests were added. If proper testing was added, we could be sure that the entire project works every time we wanted to push to GitLab. 

## Documentation

The backend is documented with JavaDoc and Swagger. After running the backend locally on your computer, go to http://localhost:8080/swagger-ui/index.html# in your web browser to view the swagger documentation.

## Reflection on our work and final words <a name="reflection"></a>

While we were developing this project, our main focus was on implementing as many features as possible. This lead to the application having almost every feature in the task description. As stated before, only sending messages through the website is what we need to implement. As there was little time to finish the project, not all methods have the code standard that we desire. Almost all methods return a status message depending on the input data and access levels, but some methods have poor exception handling and does not give the developer or user the correct error codre every time. On the other side, most of the methods does have good exception handling and tells the user excacly what the error is.



