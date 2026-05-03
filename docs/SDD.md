CZ:

Architektura:

Aplikace je postavena na frameworku Spring Boot s využitím architektury MVC.


Controller: Zpracování HTTP požadavků a řízení toku dat.

Model (Entity): Mapování databáze pomocí Hibernate/JPA.

View: Server-side rendering pomocí šablonovacího systému Thymeleaf.



Datový model:

User: ID, Jméno, Email, Heslo, Role.

Event: ID, Název, Datum (uloženo jako Timestamp/Long), Lokalita.

Category: ID, Název, Délka, Převýšení, vazba na Event.

Registration: Vazba na User a Category, durationInSeconds (výsledek), status.



Implementační detaily:

Výpočet pořadí: Pro optimální správu databáze je pořadí (Rank) počítáno dynamicky v Javě pomocí SQL dotazu count. Data jsou přenášena do frontendu skrze PastRegistrationDTO.


EN:

Architecture:

The application is built on the Spring Boot framework using the MVC architecture.


Controller: Processing HTTP requests and controlling data flow.

Model (Entity): Database mapping using Hibernate/JPA.

View: Server-side rendering using the Thymeleaf templating engine.



Data Model:

User: ID, Name, Email, Password, Role.

Event: ID, Name, Date (stored as Timestamp/Long), Location.

Category: ID, Name, Length, Climb, relationship to Event.

Registration: Relationship to User and Category, durationInSeconds (result), status.



Implementation Details:

Rank Calculation: For optimal database management, the Rank is calculated dynamically in Java using an SQL count query. Data is transferred to the frontend via PastRegistrationDTO.