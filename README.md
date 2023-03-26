# Voluntary IDATT-2105-Fullstack project

# Table of contents
1. [Introduction](#introduction)
2. [Documentation](#documentation)
3. [IDE for frontend](#frontendIDE)
4. [IDE for backend](#backendIDE)
5. [Installation](#installation)
6. []()

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

[Docker] is required to run the database. After installing docker, run these commands from the starting folder:

```sh
cd Backend
docker build -t mysql-image .
docker run --name mysql-container -p 3306:3306 -d mysql-image
```

The docker container should now be visible in Docker Desktop. The `MySQL-Server/scrpts/` folder contains database dummy data to make testing easier. Delete this folder if you don't want dummy data.

### Backend setup

```sh
cd MySQL-Server
mvn spring-boot:run
```

### Frontend setup

Eilert du skriver denne

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
