# Vacation Booking — Back-End API

A Spring Boot REST API for booking vacation packages and their optional
excursions. Customers are stored against a country/division address hierarchy,
build a cart of vacations and add-on excursions, and check out through a
transactional service that prices the order and issues a tracking number.

## What it does

- **Vacation catalog** — vacations carry a title, description, travel fare, and
  image, each with a set of optional excursions priced separately.
- **Customer records** — customers are tied to a division (state/province),
  which in turn belongs to a country, so addresses validate against a real
  geographic hierarchy rather than free text.
- **Cart and checkout** — a cart holds line items for the chosen vacation and
  excursions. `CheckoutServiceImpl` runs the order in a single transaction:
  it reloads the full customer record, forces new cart entities to insert
  rather than merge, stamps timestamps, generates a UUID tracking number, and
  moves the cart's status to `ordered`.
- **Order status** — a `StatusType` enum (`pending`, `ordered`, `cancelled`)
  tracks where a cart sits in its lifecycle.
- **REST resources** — Spring Data REST exposes the repositories under `/api`,
  with a dedicated checkout controller for the order flow.

## Data model

Seven JPA entities plus a status enum:

| Entity | Role |
|---|---|
| `Country` | top of the address hierarchy |
| `Division` | state or province, belongs to a country |
| `Customer` | belongs to a division, owns one cart |
| `Vacation` | a bookable package with a travel fare |
| `Excursion` | an optional add-on to a vacation |
| `Cart` | an order, carrying status and tracking number |
| `CartItem` | a line item within a cart |
| `StatusType` | enum: `pending`, `ordered`, `cancelled` |

## Stack

| | |
|---|---|
| Framework | Spring Boot (Web, Data JPA, Data REST) |
| Database | MySQL 8 |
| ORM | Hibernate / JPA, Lombok for accessors |
| Front end | Angular (provided by the course, included under `LabFiles/`) |
| Build | Maven (wrapper included) |

## Running it

### 1. Create the database

The schema is not generated at runtime (`ddl-auto=none`), so it has to be
created first. The script builds the database, its eight tables, sample data,
and the application's MySQL user:

```bash
mysql -u root -p < "LabFiles/create_and_populate_db.sql"
```

### 2. Start the API

```bash
./mvnw spring-boot:run
```

It serves on port 8080 with REST resources under `/api`.

### 3. Start the front end

The Angular UI shipped with the course lives in `LabFiles/D288 Front End/`:

```bash
cd "LabFiles/D288 Front End"
npm install
ng serve
```

It calls the API at `http://localhost:8080`, so start the back end first.

## Project layout

```
src/main/java/com/example/d288/
├── entities/      # Country, Division, Customer, Vacation,
│                  # Excursion, Cart, CartItem, StatusType
├── dao/           # Spring Data repositories
├── services/      # checkout service, purchase request/response
├── controllers/   # checkout controller
├── config/        # Spring Data REST configuration
└── DataLoader.java

LabFiles/          # course-supplied material: Angular front end,
                   # database script, UML and ERD diagrams
```

## Notes

The Angular front end under `LabFiles/` was provided by the course and could not
be modified — the work in this repository is the Spring Boot back end in `src/`.

The MySQL credentials in `application.properties` are the localhost-only
defaults created by `create_and_populate_db.sql`; they grant access to nothing
beyond a local sample database.
