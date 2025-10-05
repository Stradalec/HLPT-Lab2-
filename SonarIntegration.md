# Шаги по интеграции SonarCloud
1. Регистрация на сайте SonarQube
2. Загрузка Community Edition, распаковка файла, запуск сервера (StartSonar.bat)
3. Войти в панель управления (на порту 9000)
4. Импортировать проект из удалённого репозитория, скачать SonarScanner (SonarCLI).
5. Создать в корне проекта файл с настройками (sonar-project.properties), такие как sonar.projectKey, sonar.organization, sonar.sources sonar.sourceEncoding, sonar.login
6. Добавить  secrets, создав через настройки в Github 
7. Создать через Github .github/workflows/cool_workflow_name.yml. В него добавить следующее:
```
jobs:
  build:
    runs-on: windows-latest
    env:
      CI_COMMIT_REF_NAME: ${{ github.ref_name }}
      CI_COMMIT_BRANCH: ${{ github.ref_name }}
    steps:
      - name: Checkout code
        uses: actions/checkout@v3
      - name: Run SonarScanner
        run: sonar-scanner
        env:
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
          CI_COMMIT_REF_NAME: ${{ env.CI_COMMIT_REF_NAME }}
          CI_COMMIT_BRANCH: ${{ env.CI_COMMIT_BRANCH }}
```
Где runs-on - операционная система.
CI_COMMIT_REF_NAME, CI_COMMIT_BRANCH - переменные.
Steps - действия
8. Для автоматического анализа изменений добавить в файл с настройками (sonar-project.properties):
```# Уникальный ключ PR
sonar.pullrequest.key=${CI_COMMIT_REF_NAME}
# Текущая ветка PR
sonar.pullrequest.branch=${CI_COMMIT_BRANCH}
# Базовая ветка (например, dev)
sonar.pullrequest.base=dev
# SCM-платформа
sonar.pullrequest.provider=GitHub
```
(Пример честно украден и адаптирован из официальной документации)