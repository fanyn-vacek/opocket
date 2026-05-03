CZ:

Projekt O:Pocket je webový systém pro správu výsledků a registrací v orientačním běhu.


Funkční požadavky:

    F1: Autentizace: Registrace a přihlášení uživatele do systému.
    F2: Organizace závodů: Vytváření, úprava a mazání závodů (Admin).
    F3: Správa kategorií: Definování parametrů tratí (délka, převýšení) pod konkrétním závodem.
    F4: Registrační systém: Přihlašování uživatelů do kategorií a jejich automatické odhlašování při smazání kategorie.
    F5: Výsledkový servis: Evidence časů a statusů (OK, DSQ, DNF) a automatický výpočet pořadí.
    F6: Profil běžce: Zobrazení historie startů s dosaženými výsledky.


Use Case Model:

UC1

    Registrace na závod
    aktér: Běžec
    popis: Uživatel si vybere kategorii a přihlásí se na budoucí závod.


UC2

    Správa výsledků
    aktér: Organizátor
    popis: Admin zadá výsledné časy běžců v detailu závodu.


UC3

    Správa výsledků
    aktér: Běžec
    popis: Uživatel v profilu vidí své minulé výsledky a vypočtený rank.


UC4

    Správa závodu
    aktér: Organizátor
    popis: Admin definuje nový závod a jeho kategorie.



Nefunkční požadavky:


    Zabezpečení: V aktuální verzi jsou hesla ukládána v původní podobě.

    Uživatelské rozhraní: Aplikace je navržena jako Desktop-first. UI je optimalizováno pro standardní stolní prohlížeče.

    Dostupnost: Systém využívá transakční zpracování SQLite pro zajištění integrity dat při souběžných registracích.


EN:

The O:Pocket project is a web-based system for managing results and registrations in orienteering.


Functional Requirements:

    F1: Authentication: User registration and login to the system.
    F2: Race Organization: Creating, editing, and deleting races (Admin).
    F3: Category Management: Defining course parameters (length, climb) under a specific race.
    F4: Registration System: Registering users to categories and automatically unregistering them when a category is deleted.
    F5: Results Service: Recording times and statuses (OK, DSQ, DNF) and automatically calculating rankings.
    F6: Runner Profile: Displaying start history with achieved results.


Use Case Model:

UC1

    Registration for a race
    actor: Runner
    description: The user selects a category and registers for an upcoming race.


UC2

    Results management
    actor: Organizer
    description: The admin enters the runners' final times in the race details.


UC3

    Results management
    actor: Runner
    description: The user sees their past results and calculated rank in their profile.


UC4

    Race management
    actor: Organizer
    description: The admin defines a new race and its categories.



Non-functional Requirements:


    Security: In the current version, passwords are stored in plain text.

    User Interface: The application is designed as Desktop-first. The UI is optimized for standard desktop browsers.

    Availability: The system uses SQLite transactional processing to ensure data integrity during concurrent registrations.