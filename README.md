# HLPT-Lab2-
Запуск компилятора:
kotlinc app.kt -cp "kotlinx-cli-jvm-0.3.6.jar" -d app.jar
Запуск файла:
java -cp "app.jar;libs/*" AppKt --login user --password pass --action read --resource A.B.C --volume 10
Пример рабочего входа:
java -cp "app.jar;libs/*" AppKt --login alice --password qwerty  --action read --resource A.B.C --volume 10