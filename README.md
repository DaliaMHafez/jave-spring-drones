Spring Boot Drones API
This is a simple API for managing a fleet of drones and loading them with medications for delivery.

Prerequisites
To build and run this project locally, you will need the following tools:

Java 8 or higher
Maven 4.0.0
Build and Run
Clone this repository
Open a terminal and navigate to the project root directory
Run mvn clean install to build the project and generate the executable JAR file
Run java -jar target/spring-boot-drones 0.0.1-SNAPSHOT.jar to start the application
By default, the application will start on port 8080.

Test
The project includes unit tests for the API endpoints. To run the tests, navigate to the project root directory and run mvn test.

API Endpoints
The API includes the following endpoints:

GET /drones
Retrieves all drones.

GET /drones/history
Retrieves the battery level history for all drones.

POST /drones
Registers a new drone.

GET /drones/{serialNumber}/medications
Retrieves medications loaded onto a drone.

POST /drones/{serialNumber}/medications
Loads medications onto a drone.

GET /drones/available-for-loading
Retrieves all available drones.

GET /drones/{serialNumber}/battery
Retrieves the battery level of a drone.