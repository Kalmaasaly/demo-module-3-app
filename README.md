# 📚 Book Management App + AI Bot

A Spring Boot app to manage books, authors, and categories — with an AI chatbot powered by **Ollama llama3** running locally.

---

## ✅ Requirements

Before you start, make sure you have installed:

- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [Git](https://git-scm.com/)

That's it! No need to install Java or Gradle.

---

## 🚀 How to Run

### 1. Clone the project
```bash
git clone https://github.com/Kalmaasaly/demo-module-3-app.git
cd demo-module-3-app
```

### 2. Build the jar
```bash
./gradlew build -x test
```

### 3. Start everything with Docker
```bash
docker-compose up --build -d
```

This starts 3 containers:
| Container | What it does |
|---|---|
| `book-db` | MariaDB database |
| `book-ollama` | Ollama AI model server |
| `demo-module-3-app` | Spring Boot app |

### 4. Pull the AI model (first time only)
```bash
docker exec book-ollama ollama pull llama3
```
> ⚠️ This downloads ~4GB. Only needed once — the model is saved permanently.

### 5. Open the app
| Page | URL |
|---|---|
| 🤖 AI Chat Bot | http://localhost:8080/book-bot.html |
| 📚 Books API | http://localhost:8080/api/v1/books |
| 🖊️ Authors API | http://localhost:8080/api/v1/authors |
| 🗂️ Categories API | http://localhost:8080/api/v1/categories |

---

## 📝 Add Sample Data (Postman or curl)

**Add a category:**
```bash
curl -X POST http://localhost:8080/api/v1/categories \
  -H "Content-Type: application/json" \
  -d "{\"name\": \"Fiction\"}"
```

**Add an author:**
```bash
curl -X POST http://localhost:8080/api/v1/authors \
  -H "Content-Type: application/json" \
  -d "{\"name\": \"George Orwell\", \"bio\": \"English novelist\"}"
```

**Add a book:**
```bash
curl -X POST http://localhost:8080/api/v1/books \
  -H "Content-Type: application/json" \
  -d "{\"title\": \"1984\", \"authorId\": 1, \"categoryNames\": [\"Fiction\"]}"
```

> 💡 On Windows use the curl commands above with `\"` instead of `'`

---

## 🛑 How to Stop

```bash
docker-compose down
```

---

## 🔄 How to Restart

```bash
docker-compose up -d
```

> No need to pull llama3 again — the model is already saved.

---

## 🤖 How the AI Bot Works

```
You ask a question
       ↓
Spring Boot fetches your real data from the database
       ↓
Sends it to Ollama llama3 (running locally)
       ↓
AI answers based only on YOUR data
```

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Spring Boot 3.4.4 | Backend framework |
| Spring AI | AI integration |
| Ollama + llama3 | Local AI model |
| Spring Data JPA | Database access |
| MariaDB | Database |
| Docker Compose | Run everything together |

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── controller/    # REST API endpoints + ChatController
│   │   └── service/       # Business logic
│   │   └── entity/        # Book, Author, Category
│   │   └── repository/    # Database access
│   └── resources/
│       ├── static/
│       │   └── book-bot.html      # AI Chat page
│       ├── application.yml        # Active profile config
│       ├── application-dev.yml    # Dev config
│       └── application-prod.yml   # Prod config
├── docker-compose.yml
└── Dockerfile
```
