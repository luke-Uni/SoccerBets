# SoccerBets

This project is a betting system developed as a RESTful server.

## Overview

SoccerBets is a fantasy soccer betting system where users can bet money on simulated soccer events. The system simulates games/events, and users can place bets on the outcome of these events. Keep in mind that the results are not realistic, as this is a fantasy soccer system.

## Features

- **Fantasy Soccer Betting**: Place bets on fictional soccer teams.
- **Simulated Events**: The system automatically simulates events and calculates the outcomes.
- **User Authentication**: Token-based security to validate users.
- **Create and Manage Bets**: Users can create, modify, or delete their bets on existing events.
- **Create and Manage Events**: Authorized users can create and manage soccer events.
- **Team Management**: Create, update, and delete teams participating in events.
- **Retrieve Data**: Retrieve lists of users, teams, bets, and events.

## Technologies Used

- **Java**
- **Spring Boot**
- **Spring Security**
- **RESTful API**
- **Token-Based Authentication**
- **Postman for API Testing**

## Setup and Installation

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd <repository-directory>
```

2. Build the project using Maven:
```bash
mvn clean install
```

3. Run the project:
```bash
mvn spring-boot:run
```

4. Access the API: The API will be accessible at http://localhost:8080/

## Endpoints

### Bets Endpoints

- `POST /bets` - Create a new bet.
- `GET /bets` - Retrieve all bets.
- `GET /bets/current` - Retrieve all current bets.
- `GET /bets/{userName}/{state}` - Retrieve all bets from a specific user.
- `DELETE /bets` - Retract a bet.
- `PATCH /bets` - Overwrite a bet.

### Events Endpoints

- `POST /events/{teamA}/{teamB}` - Create a new event between two teams.
- `DELETE /events/{teamA}/{teamB}` - Delete an existing event.
- `GET /events` - Retrieve all events.
- `GET /events/{teamA}/{teamB}` - Retrieve details of a specific event.
- `GET /events/{eventName}` - Simulate an event and return the winner.
- `GET /events/{eventName}/bets` - Get all bets for a specific event.

### Teams Endpoints

- `POST /teams` - Create a new team.
- `PUT /teams` - Overwrite an existing team.
- `PATCH /teams/{teamName}` - Update specific attributes of a team.
- `DELETE /teams/{teamName}` - Delete an existing team.
- `GET /teams/{sorted}/asc` - Retrieve teams in ascending order.
- `GET /teams/{sorted}/desc` - Retrieve teams in descending order.

### Users Endpoints

- `POST /users` - Create a new user.
- `GET /users` - Retrieve all users.
- `GET /users/{userName}` - Retrieve details of a specific user.
- `DELETE /users/{username}` - Delete a user.

## Security

All endpoints require a valid token passed in the `Authorization` header. Tokens are generated upon user creation and are required for every API call. Unauthorized access will result in a `401 Unauthorized` response.
