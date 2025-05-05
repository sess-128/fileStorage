
# 📦 FileStorage — хранилище файлов с авторизацией и S3

Full-stack проект для хранения и управления файлами с использованием:
- Spring Boot
- PostgreSQL
- Redis
- MinIO (S3-совместимое хранилище)
- Docker Compose
- Swagger
- Liquibase
- Maven

---

## 🚀 Быстрый старт

### 1. Клонируйте репозиторий
```bash
git clone https://github.com/your-username/fileStorage.git
cd fileStorage
```

### 2. Подготовка `.env` файла
Переименуйте `.env.local` в `.env`:
```bash
mv .env.local .env
```

Убедитесь, что все переменные окружения (пароли, логины, URL-ы) в `.env` заполнены корректно.

### 3. Дайте права на исполняемый файл (только для macOS/Linux)

Файл `init.sh` используется для начальной настройки MinIO. Обязательно сделать его исполняемым:

```bash
chmod +x init.sh
```

Если этого не сделать — скрипт настройки MinIO не запустится при старте контейнеров.

---

## 🐳 Запуск в Docker

```bash
docker compose up -d
```

---

## 🧪 Тестирование

Для запуска unit- или integration-тестов, **Redis должен быть запущен отдельно**, так как тестовый контекст не использует docker-compose.

```bash
docker run --name redis-test -p 6379:6379 redis:alpine
```

Затем можно запускать тесты в IDE или через Gradle/Maven.

---

## 📂 Структура

```
.
├── src/                         # Spring backend
├── init.sh                      # Скрипт инициализации MinIO
├── .env                         # Переменные окружения
├── docker-compose.yml
└── README.md
```

---

## 📚 Swagger

Документация будет доступна по адресу:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🛠 Troubleshooting

- **Swagger недоступен** — проверь, что MinIO и backend поднялись без ошибок (`docker compose logs`).
- **`Permission denied` на `init.sh`** — не забудь `chmod +x init.sh` перед запуском Docker.
- **Ошибка авторизации при регистрации** — убедись, что CORS и SecurityConfig правильно настроены.

---

## 🧾 Лицензия
[Сергей Жуков](https://t.me/zhukovsd_it_mentor)
