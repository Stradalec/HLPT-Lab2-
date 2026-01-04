#!/bin/bash

UNAME_OUT="$(uname -s)"
case "${UNAME_OUT}" in
    MINGW*|MSYS*|CYGWIN*)
        CP_SEP=';'
        ;;
    *)
        CP_SEP=':'
        ;;
esac


kotlinc $(find src -name "*.kt")  -cp "libs/sqlite-jdbc-3.50.3.0.jar${CP_SEP}libs/kotlinx-cli-jvm-0.3.6.jar" -include-runtime -d app.jar