# Сущности
1. Пользователь
id - Uniq int
login -  String
salt - String
hash - String
2. Ресурс
id - Uniq
name: String,
maxVolume: Int = 10,
parentId: Int = null
3. Разрешение
User.id
resource.id
availableActions String с 3 буквами (RWE) (R-E)

Предположительная схема:
```mermaid
erDiagram
    users ||--o{ permissions : "1:N"
    resources ||--o{ permissions : "1:N"

    users {
        int id PK
        varchar login
        varchar salt
        varchar hash
    }

    resources {
        int id PK
        varchar name
        int maxVolume
        int parentId FK
    }

    permissions {
        int userId PK, FK
        int resourceId PK, FK
        varchar availableActions
    }
```
Структура таблиц:
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(255) NOT NULL UNIQUE,
    salt VARCHAR(255) NOT NULL,
    hash VARCHAR(255) NOT NULL
);

CREATE TABLE resources (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    maxVolume INT DEFAULT 10,
    parentId INT,
    FOREIGN KEY (parentId) REFERENCES resources(id) ON DELETE CASCADE
);

CREATE TABLE permissions (
    userId INT NOT NULL,
    resourceId INT NOT NULL,
    availableActions VARCHAR(3) NOT NULL CHECK (availableActions GLOB '[R-][W-][E-]'), //Вот эта штука сделана, как проверка "R или -"
    PRIMARY KEY (userId, resourceId),
    FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (resourceId) REFERENCES resources(id) ON DELETE CASCADE
);