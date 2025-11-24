# HLPT-Lab2-
Авторы программы: Калинин Андрей, Струк Степан
## Гайд на запуск:

Открыть **GitBASH**, затем использовать команду:

    cd C:/Путь/До/Папки/С этой/Программой

После выполнить запуск компилятора:

    ./scripts//build.sh

Теперь доступен запуск файла (Пример ниже)

    ./scripts//start.sh --login alice --password qwerty  --action read --resource A.B.C --volume 10

Также можно запустить файл с тестами:

    ./scripts//test.sh

Запуск инициализации БД:

    ./scripts/setDatabase.sh

Команда запуска тестов из консоли:

    java -jar libs/junit-platform-console-standalone.jar --class-path app.jar --select-class ResourceTests

(Вместо ResourceTests можно назначить другой класс)

**Делать только после выполнения compileWithTests.sh!**
