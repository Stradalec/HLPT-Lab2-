# HLPT-Lab2-
Авторы программы: Калинин Андрей, Струк Степан
Запуск:
Открыть GitBASH, затем использовать команду:
cd C:/Путь/До/Папки/С этой/Программой
После выполнить запуск компилятора:
./compile.sh
Теперь доступен запуск файла (Пример ниже)
./start.sh --login alice --password qwerty  --action read --resource A.B.C --volume 10
Также можно запустить файл с тестами:
./test.sh
В связи с тем, что Sonar за отсутствие тестов бьёт палками, добавляю сюда команды для создания тестов:
kotlinc app.kt -classpath "libs/junit-platform-console-standalone.jar;libs/kotlinx-cli-jvm-0.3.6.jar" -d test.jar
java -jar libs/junit-platform-console-standalone.jar --class-path test.jar;libs/kotlin-stdlib.jar --scan-class-path
