# � Ride-Hailing System

Hey there! Welcome to this ride-hailing simulation project. Think of it like Uber or Grab, but running entirely on your local machine. It's a great way to see how these apps work behind the scenes!

## What Does This Do?

This project simulates a complete ride-hailing system where:
- **Riders** can request rides by setting their pickup and destination
- **Drivers** can go online and accept ride requests
- The system automatically matches riders with nearby drivers in real-time

It's built to demonstrate how modern apps handle location tracking, real-time matching, and communication between different parts of the system.

---

## How It Works

The app is split into several parts that work together:

**🎯 Matching Engine**  
This is the brain that finds nearby drivers when you request a ride. It uses Elasticsearch to quickly search for drivers within a certain distance from you.

**📍 Location Tracker**  
Keeps track of where all the drivers are in real-time. Uses Redis (a super-fast cache) so the system doesn't slow down even with lots of location updates.

**📨 Message Queue**  
Think of this as the app's internal postal service. It uses RabbitMQ to send messages between different parts of the system (like "new ride request!" or "driver assigned!").

**💾 Database**  
PostgreSQL stores all the important stuff like trip history and user information.

**🎨 Dashboard**  
A sleek React-based interface where you can play both the rider and driver roles. It has a cool "mission control" vibe!

---

## Getting It Running

Don't worry, it's easier than it looks! Just follow these steps:

### What You'll Need First
- Docker & Docker Compose (to run the databases and services)
- Java 17 or newer & Maven (for the backend)
- Node.js v18 or newer (for the frontend)

### Step 1: Start the Infrastructure
This fires up all the databases and messaging services:
```bash
cd infra
docker-compose up -d
```

### Step 2: Start the Backend Services
Open 4 separate terminal windows and run these commands (one in each):
```bash
mvn spring-boot:run -pl driver-service
mvn spring-boot:run -pl trip-service
mvn spring-boot:run -pl dispatch-service
mvn spring-boot:run -pl rider-service
```

> **Tip:** If you're using an IDE like IntelliJ or Eclipse, you can run all these services from there instead!

### Step 3: Start the Frontend
```bash
cd frontend
npm install
npm run dev
```

Now open your browser and go to `http://localhost:3000` — you're ready to go! 🎉

---

## Let's Try It Out!

Here's how to simulate a complete ride from start to finish:

### 🚗 Be the Driver (Tab 1)
1. Open the **Driver Dashboard**
2. Give yourself a cool callsign like `ALPHA-1` or `SPEEDY-7`
3. Click **Go Online** — your status should turn green
4. Click **Detect Position** to use your current location, or click anywhere on the map
5. Hit **Transmit Coords** to let the system know where you are

### 🙋 Be the Rider (Tab 2)
1. Open a new tab and go to the **Rider Console**
2. Set your **Pickup** location (make sure it's close to where your driver is!)
3. Set your **Destination**
4. Click **⚡ Request Dispatch**
5. Watch the magic happen! The status will change from `SEARCHING` to `ASSIGNED` when the system finds your driver

### 🔄 Want to Start Fresh?
Head to the **System Admin** page and you can clear all the data to run another simulation.

---

## What's Under the Hood?

**Backend:**
- Spring Boot 3 (Java framework)
- Spring Data (for database stuff)
- JUnit 5 (for testing)

**Frontend:**
- React (UI framework)
- Leaflet (for the interactive maps)
- Axios (for talking to the backend)
- CSS Modules (for styling)

**Infrastructure:**
- PostgreSQL 15 (main database)
- Redis 7 (fast cache for locations)
- Elasticsearch 7 (for geo-spatial search)
- RabbitMQ 3 (message queue)

---
