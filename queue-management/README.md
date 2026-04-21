# 🗳️ Smart Queue Management System for Polling Stations

A full-stack web application to manage and monitor polling booth queues in real time.

## 📋 Table of Contents
- [About](#about)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Setup Instructions](#setup-instructions)
- [API Endpoints](#api-endpoints)
- [Screenshots](#screenshots)

## 📌 About
This system helps voters and election officials manage polling booth queues efficiently. 
Voters can view real-time queue status, get virtual tokens, and receive email alerts 
when queues drop below their preferred level.

## ✨ Features
- 🔐 JWT Authentication (Role-based: ADMIN & VOTER)
- 🏛️ Multi-booth support with real-time queue tracking
- 🎫 Virtual Queue Token System
- 📧 Email notifications via EmailJS
- 📊 Live analytics (peak queue, average queue, total updates)
- 🟢🟡🔴 Crowd level detection (Low/Medium/High)
- ⏱️ Estimated wait time calculation
- 🔔 Custom queue alert system
- 📈 Live queue trend chart

## 🛠️ Tech Stack
| Layer | Technology |
|---|---|
| Frontend | HTML, CSS, JavaScript |
| Backend | Java, Spring Boot |
| Database | MongoDB |
| Security | JWT Authentication |
| Build Tool | Maven |
| Email | EmailJS |

## ⚙️ Setup Instructions

### Prerequisites
- Java 17+
- Maven 3.8+
- MongoDB 6.0+
- Git

### Backend Setup
1. Clone the repository
```bash
   git clone https://github.com/SANJANASREE-V/smart-queue-management.git
```

2. Navigate to project folder
```bash
   cd queue-management
```

3. Start MongoDB
```bash
   net start MongoDB
```

4. Run the application
```bash
   mvn spring-boot:run
```

5. Backend runs on `http://localhost:8080`

### Frontend Setup
1. Navigate to frontend folder
```bash
   cd REGISTER
```

2. Open `index.html` in browser
   - Or use Live Server extension in VS Code

## 🔌 API Endpoints

### Auth APIs
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register voter/admin |
| POST | `/api/auth/login` | Login and get JWT token |

### Booth APIs
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/booths` | Get all active booths |
| GET | `/api/booths/{id}` | Get booth by ID |
| GET | `/api/booths/{id}/history` | Get booth queue history |

### Admin APIs
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/admin/booths` | Create new booth |
| PUT | `/api/admin/queue/update` | Update queue length |

### Token APIs
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/tokens/generate` | Generate voter token |
| POST | `/api/tokens/reset/{id}` | Reset booth tokens |

### Alert APIs
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/alerts` | Save alert |
| GET | `/api/alerts/{userId}` | Get user alerts |
| DELETE | `/api/alerts/{id}` | Delete alert |

## 👥 User Roles
- **ADMIN** — Can create booths, update queue lengths, view analytics
- **VOTER** — Can view booth queues, get tokens, set alerts

## 📱 Pages
- `index.html` — Voter login and registration
- `admin.html` — Admin login
- `adDashboard.html` — Admin dashboard with queue control
- `dashboard.html` — Voter dashboard with booth cards and token system

## 🔐 Default Test Credentials
Admin:
Email: admin@test.com
Password: admin123
Voter:
Email: voter1@test.com
Password: voter123
