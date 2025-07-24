#  Tennis Game Kata – Java Spring Boot Implementation

This project is a clean, extensible implementation of a **Tennis Scoring System** using **Java 17**, **Spring Boot 3**, and **Swagger (OpenAPI)**. It handles tennis game logic including point tracking, advantage/deuce states, and game finalization, while remaining easy to extend for real-world use.

---
## VISION 

In this project i have not been limited to the kata, as i understand the goal is to 
to showcase my coding skills, my design choices and the quality of my code. 

This was a rapid take at the kata, from a vision that i would like a game for singles/doubles with future features
such as tournaments, history, stats, trophies etc. 


---

##  Features

-  Create a tennis game between two teams (singles or doubles)
-  Record point events, update score, and handle deuce/advantage logic
-  Get current game score in readable format (e.g., `15 - 30`, `Deuce`, `Advantage Team A`)
-  RESTful API endpoints with validation and Swagger documentation
-  Unit tests with junit


---
##  API DOCUMENTATION

http://localhost:8080/swagger-ui.html



## GETTING STARTED

- git clone https://github.com/your-username/tennis-kata.git 
- cd tennis-kata
- ./mvnw spring-boot:run


THERE ARE 2 FILES : match_scenario.ps1 or match_scenario.sh 

you can run either file in order to simulate a full Match

1 Match -> 3 Sets -> 6 games each set. 


