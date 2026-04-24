# 🗳️ Smart Queue Management System for Polling Stations

A full-stack web application to manage and monitor polling booth queues in real time.

---

## 📋 Table of Contents
- [About](#about)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Setup Instructions](#setup-instructions)
- [API Endpoints](#api-endpoints)
- [User Roles](#user-roles)
- [Pages](#pages)

---

## 📌 About
This system helps voters and election officials manage polling booth queues efficiently.
Voters can view real-time queue status, get virtual tokens, and receive email alerts
when queues drop below their preferred level.

---

## ✨ Features
- 🔐 JWT Authentication with Role-based access (ADMIN and VOTER)
- 🏛️ Multi-booth support with real-time queue tracking
- 🎫 Virtual Queue Token System with sequential numbering
- 📧 Email notifications via EmailJS
- 📊 Live analytics (peak queue, average queue, total updates)
- 🟢🟡🔴 Crowd level detection (Low, Medium, High)
- ⏱️ Estimated wait time calculation
- 🔔 Custom queue alert system
- 📈 Live queue trend chart
- 🔄 Auto refresh every 30 seconds

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Frontend | HTML, CSS, JavaScript |
| Backend | Java 17, Spring Boot 3 |
| Database | MongoDB |
| Security | JWT Authentication |
| Build Tool | Maven |
| Email Service | EmailJS |
| API Testing | Postman |

---

## ⚙️ Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.8 or higher
- MongoDB 6.0 or higher
- Git

### Step 1 - Clone the repository
```bash
git clone https://github.com/yourusername/smart-queue-management.git
```

### Step 2 - Navigate to project folder
```bash
cd queue-management
```

### Step 3 - Start MongoDB
```bash
net start MongoDB
```

### Step 4 - Run the backend
```bash
mvn spring-boot:run
```

### Step 5 - Open Frontend
Open `REGISTER/index.html` in your browser
Or use Live Server extension in VS Code

---

## 🔌 API Endpoints

### Auth APIs
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/api/auth/register` | Register voter or admin | No |
| POST | `/api/auth/login` | Login and get JWT token | No |

### Booth APIs
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| GET | `/api/booths` | Get all active booths | No |
| GET | `/api/booths/{id}` | Get booth by ID | No |
| GET | `/api/booths/{id}/history` | Get booth queue history | No |

### Admin APIs
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/api/admin/booths` | Create new booth | ADMIN |
| PUT | `/api/admin/queue/update` | Update queue length | ADMIN |

### Token APIs
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/api/tokens/generate` | Generate voter token | Yes |
| POST | `/api/tokens/reset/{id}` | Reset booth tokens | Yes |

### Alert APIs
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/api/alerts` | Save alert | Yes |
| GET | `/api/alerts/{userId}` | Get user alerts | Yes |
| DELETE | `/api/alerts/{id}` | Delete alert | Yes |

---

## 👥 User Roles

### ADMIN
- Login via `admin.html`
- Create and manage polling booths
- Update queue lengths in real time
- View live analytics and charts
- Switch between multiple booths

### VOTER
- Login via `index.html`
- View all polling booths with crowd levels
- Get virtual queue token
- Set custom queue alerts
- Receive email notifications
- See estimated wait times

---

## 📱 Pages

| Page | Description |
|---|---|
| `index.html` | Voter login and registration |
| `admin.html` | Admin login page |
| `adDashboard.html` | Admin dashboard with queue control |
| `dashboard.html` | Voter dashboard with booth cards and token system |

---

## 🗄️ Database Collections

| Collection | Description |
|---|---|
| `users` | Stores voter and admin accounts |
| `polling_booth` | Stores booth details and queue data |
| `queue_record` | Stores history of all queue updates |
| `alerts` | Stores voter alert preferences |

---

## 🔐 Test Credentials
Admin:
Email: admin@test.com
Password: admin123
Voter:
Email: voter1@test.com
Password: voter123

---

## 📊 Project Structure

```
smart-queue-management/
│
├── queue-management/              # Spring Boot Backend
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/polling/queue/management/
│   │       │       ├── controller/    # REST API Controllers
│   │       │       ├── service/       # Business Logic
│   │       │       ├── model/         # MongoDB Models
│   │       │       ├── repository/    # MongoDB Repositories
│   │       │       ├── dto/           # Data Transfer Objects
│   │       │       ├── config/        # Security & CORS Config
│   │       │       └── util/          # JWT Utility
│   │       └── resources/
│   │           └── application.properties
│   └── pom.xml
│
└── Register/                      # Frontend
    ├── index.html                 # Voter Login & Register
    ├── admin.html                 # Admin Login
    ├── adDashboard.html           # Admin Dashboard
    ├── dashboard.html             # Voter Dashboard
    └── style.css                  # Stylesheet
```
---

## 🚀 How It Works

Admin creates polling booths
Voters register and login
Voters see all booths with real-time queue data
Voters select a booth and get a unique token number
Admin updates queue as voters arrive and leave
Voter dashboard auto-refreshes every 30 seconds
When queue drops below voter's alert level
→ Email notification is sent automatically


---

## 👩‍💻 Built By
SANJANASREE V 