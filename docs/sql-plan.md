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
parent: Resource? = null
3. Разрешение
User.id
resource.id
availableActions String с 3 буквами (RWE) (R-E)