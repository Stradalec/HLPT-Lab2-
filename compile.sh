#!/bin/bash
kotlinc src/*.kt  -cp "kotlinx-cli-jvm-0.3.6.jar" -include-runtime -d app.jar