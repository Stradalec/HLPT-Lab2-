#!/bin/bash
kotlinc $(find src -name "*.kt")  -cp "libs/sqlite-jdbc-3.50.3.0.jar;libs/kotlinx-cli-jvm-0.3.6.jar" -include-runtime -d app.jar