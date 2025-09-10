# HLPT-Lab2-
Запуск компилятора:
kotlinc app.kt -cp "kotlin-stdlib.jar;kotlinx-cli-jvm-0.3.6.jar" -d app.jar
Запуск файла:
java -cp "app.jar;kotlinx-cli-jvm-0.3.6.jar;kotlin-reflect-1.7.10.jar" AppKt --login user --password pass