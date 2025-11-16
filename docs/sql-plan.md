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
